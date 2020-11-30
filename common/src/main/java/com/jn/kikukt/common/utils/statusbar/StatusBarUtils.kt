package com.jn.kikukt.common.utils.statusbar

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.LinearLayout
import androidx.annotation.ColorInt
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.drawerlayout.widget.DrawerLayout
import com.jn.common.R
import com.jn.kikukt.common.utils.statusbar.StatusBarUtils.DEFAULT_STATUS_BAR_ALPHA
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.util.*


/**
 * Author：Stevie.Chen Time：2019/7/15
 * Class Comment：状态栏工具类
 */
object StatusBarUtils {

    internal const val DEFAULT_STATUS_BAR_ALPHA = 112
    internal val FAKE_STATUS_BAR_VIEW_ID = R.id.statusbarutil_fake_status_bar_view
    internal val FAKE_TRANSLUCENT_VIEW_ID = R.id.statusbarutil_translucent_view
    internal const val TAG_KEY_HAVE_SET_OFFSET = -123

    ///////////////////////////////////////////////////////////////////////////////////

    @TargetApi(Build.VERSION_CODES.KITKAT)
    internal fun clearPreviousSetting(activity: Activity) {
        val decorView = activity.window.decorView as ViewGroup
        val fakeStatusBarView = decorView.findViewById<View>(FAKE_STATUS_BAR_VIEW_ID)
        if (fakeStatusBarView != null) {
            decorView.removeView(fakeStatusBarView)
            val rootView =
                (activity.findViewById<View>(android.R.id.content) as ViewGroup).getChildAt(0) as ViewGroup
            rootView.setPadding(0, 0, 0, 0)
        }
    }

    /**
     * 添加半透明矩形条
     *
     * @param activity       需要设置的 activity
     * @param statusBarAlpha 透明值
     */
    internal fun addTranslucentView(activity: Activity, statusBarAlpha: Int) {
        val contentView = activity.findViewById<View>(android.R.id.content) as ViewGroup
        val fakeTranslucentView = contentView.findViewById<View>(FAKE_TRANSLUCENT_VIEW_ID)
        if (fakeTranslucentView != null) {
            if (fakeTranslucentView.visibility == View.GONE) {
                fakeTranslucentView.visibility = View.VISIBLE
            }
            fakeTranslucentView.setBackgroundColor(Color.argb(statusBarAlpha, 0, 0, 0))
        } else {
            contentView.addView(createTranslucentStatusBarView(activity, statusBarAlpha))
        }
    }

    /**
     * 生成一个和状态栏大小相同的彩色矩形条
     *
     * @param activity 需要设置的 activity
     * @param color    状态栏颜色值
     * @return 状态栏矩形条
     */
    internal fun createStatusBarView(activity: Activity, @ColorInt color: Int): View {
        return createStatusBarView(activity, color, 0)
    }

    /**
     * 生成一个和状态栏大小相同的半透明矩形条
     *
     * @param activity 需要设置的activity
     * @param color    状态栏颜色值
     * @param alpha    透明值
     * @return 状态栏矩形条
     */
    internal fun createStatusBarView(activity: Activity, @ColorInt color: Int, alpha: Int): View {
        // 绘制一个和状态栏一样高的矩形
        val statusBarView = View(activity)
        val params = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            getStatusBarHeight(activity)
        )
        statusBarView.layoutParams = params
        statusBarView.setBackgroundColor(calculateStatusColor(color, alpha))
        statusBarView.id = FAKE_STATUS_BAR_VIEW_ID
        return statusBarView
    }

    /**
     * 设置根布局参数
     */
    internal fun setRootView(activity: Activity) {
        val parent = activity.findViewById<View>(android.R.id.content) as? ViewGroup
        parent?.let {
            var i = 0
            val count = it.childCount
            while (i < count) {
                val childView = it.getChildAt(i)
                if (childView is ViewGroup) {
                    childView.setFitsSystemWindows(true)
                    childView.clipToPadding = true
                }
                i++
            }
        }
    }

    /**
     * 设置透明
     */
    internal fun setTransparentForWindow(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.window.statusBarColor = Color.TRANSPARENT
            activity.window
                .decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            activity.window
                .setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                )
        }
    }

    /**
     * 使状态栏透明
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    internal fun transparentStatusBar(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            activity.window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            activity.window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
            activity.window.statusBarColor = Color.TRANSPARENT
        } else {
            activity.window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        }
    }

    /**
     * 创建半透明矩形 View
     *
     * @param alpha 透明值
     * @return 半透明 View
     */
    private fun createTranslucentStatusBarView(activity: Activity, alpha: Int): View {
        // 绘制一个和状态栏一样高的矩形
        val statusBarView = View(activity)
        val params = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            getStatusBarHeight(activity)
        )
        statusBarView.layoutParams = params
        statusBarView.setBackgroundColor(Color.argb(alpha, 0, 0, 0))
        statusBarView.id = FAKE_TRANSLUCENT_VIEW_ID
        return statusBarView
    }

    /**
     * 设置 DrawerLayout 属性
     *
     * @param drawerLayout              DrawerLayout
     * @param drawerLayoutContentLayout DrawerLayout 的内容布局
     */
    internal fun setDrawerLayoutProperty(
        drawerLayout: DrawerLayout,
        drawerLayoutContentLayout: ViewGroup
    ) {
        val drawer = drawerLayout.getChildAt(1) as ViewGroup
        drawerLayout.fitsSystemWindows = false
        drawerLayoutContentLayout.fitsSystemWindows = false
        drawerLayoutContentLayout.clipToPadding = true
        drawer.fitsSystemWindows = false
    }

    /**
     * 获取状态栏高度
     *
     * @param context context
     * @return 状态栏高度
     */
    internal fun getStatusBarHeight(context: Context): Int {
        // 获得状态栏高度
        val resourceId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
        return context.resources.getDimensionPixelSize(resourceId)
    }

    /**
     * 计算状态栏颜色
     *
     * @param color color值
     * @param alpha alpha值
     * @return 最终的状态栏颜色
     */
    internal fun calculateStatusColor(@ColorInt color: Int, alpha: Int): Int {
        if (alpha == 0) {
            return color
        }
        val a = 1 - alpha / 255f
        var red = color shr 16 and 0xff
        var green = color shr 8 and 0xff
        var blue = color and 0xff
        red = (red * a + 0.5).toInt()
        green = (green * a + 0.5).toInt()
        blue = (blue * a + 0.5).toInt()
        return 0xff shl 24 or (red shl 16) or (green shl 8) or blue
    }

    /**
     * 设置状态栏图标为深色和魅族特定的文字风格
     * 可以用来判断是否为Flyme用户
     * @param window 需要设置的窗口
     * @param dark 是否把状态栏字体及图标颜色设置为深色
     * @return  boolean 成功执行返回true
     */
    internal fun fLymeSetStatusBarLightMode(window: Window?, dark: Boolean): Boolean {
        var result = false
        if (window != null) {
            try {
                val lp = window.attributes
                val darkFlag = WindowManager.LayoutParams::class.java
                    .getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON")
                val meizuFlags = WindowManager.LayoutParams::class.java
                    .getDeclaredField("meizuFlags")
                darkFlag.isAccessible = true
                meizuFlags.isAccessible = true
                val bit = darkFlag.getInt(null)
                var value = meizuFlags.getInt(lp)
                if (dark) {
                    value = value or bit
                } else {
                    value = value and bit.inv()
                }
                meizuFlags.setInt(lp, value)
                window.attributes = lp
                result = true
            } catch (e: Exception) {

            }

        }
        return result
    }

    /**
     * 设置状态栏字体图标为深色，需要MIUIV6以上
     * @param window 需要设置的窗口
     * @param dark 是否把状态栏字体及图标颜色设置为深色
     * @return  boolean 成功执行返回true
     */
    @SuppressLint("PrivateApi")
    internal fun mIUISetStatusBarLightMode(window: Window?, dark: Boolean): Boolean {
        var result = false
        if (window != null) {
            val clazz = window.javaClass
            try {
                val darkModeFlag: Int
                val layoutParams = Class.forName("android.view.MiuiWindowManager\$LayoutParams")
                val field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE")
                darkModeFlag = field.getInt(layoutParams)
                val extraFlagField =
                    clazz.getMethod(
                        "setExtraFlags",
                        Int::class.javaPrimitiveType,
                        Int::class.javaPrimitiveType
                    )
                if (dark) {
                    extraFlagField.invoke(window, darkModeFlag, darkModeFlag)//状态栏透明且黑色字体
                } else {
                    extraFlagField.invoke(window, 0, darkModeFlag)//清除黑色字体
                }
                result = true
            } catch (e: Exception) {

            }

        }
        return result
    }
}

/**
 * Created by HaiyuKing
 * Used 判断手机ROM,检测ROM是MIUI、EMUI还是Flyme
 * 参考资料：https://www.jianshu.com/p/ba9347a5a05a
 */
object RomUtil {
    private const val TAG = "Rom"

    private const val ROM_MIUI = "MIUI"
    private const val ROM_EMUI = "EMUI"
    private const val ROM_FLYME = "FLYME"
    private const val ROM_OPPO = "OPPO"
    private const val ROM_SMARTISAN = "SMARTISAN"
    private const val ROM_VIVO = "VIVO"
    private const val ROM_QIKU = "QIKU"

    private const val KEY_VERSION_MIUI = "ro.miui.ui.version.name"
    private const val KEY_VERSION_EMUI = "ro.build.version.emui"
    private const val KEY_VERSION_OPPO = "ro.build.version.opporom"
    private const val KEY_VERSION_SMARTISAN = "ro.smartisan.version"
    private const val KEY_VERSION_VIVO = "ro.vivo.os.version"
    private var sName: String? = null
    private var sVersion: String? = null

    //华为
    val isEmui: Boolean
        get() = check(ROM_EMUI)

    //小米
    val isMiui: Boolean
        get() = check(ROM_MIUI)

    //vivo
    val isVivo: Boolean
        get() = check(ROM_VIVO)

    //oppo
    val isOppo: Boolean
        get() = check(ROM_OPPO)

    //魅族
    val isFlyme: Boolean
        get() = check(ROM_FLYME)

    //360手机
    fun is360(): Boolean {
        return check(ROM_QIKU) || check("360")
    }

    val isSmartisan: Boolean
        get() = check(ROM_SMARTISAN)

    val name: String?
        get() {
            if (sName == null) {
                check("")
            }
            return sName
        }

    val version: String?
        get() {
            if (sVersion == null) {
                check("")
            }
            return sVersion
        }

    private fun check(rom: String): Boolean {
        if (sName != null) {
            return sName == rom
        }
        if (!TextUtils.isEmpty(
                getProp(KEY_VERSION_MIUI).also { sVersion = it }
            )
        ) {
            sName = ROM_MIUI
        } else if (!TextUtils.isEmpty(
                getProp(KEY_VERSION_EMUI).also { sVersion = it }
            )
        ) {
            sName = ROM_EMUI
        } else if (!TextUtils.isEmpty(
                getProp(KEY_VERSION_OPPO).also { sVersion = it }
            )
        ) {
            sName = ROM_OPPO
        } else if (!TextUtils.isEmpty(
                getProp(KEY_VERSION_VIVO).also { sVersion = it }
            )
        ) {
            sName = ROM_VIVO
        } else if (!TextUtils.isEmpty(
                getProp(KEY_VERSION_SMARTISAN).also { sVersion = it }
            )
        ) {
            sName = ROM_SMARTISAN
        } else {
            sVersion = Build.DISPLAY
            if (sVersion?.toUpperCase(Locale.getDefault())?.contains(ROM_FLYME) == true) {
                sName = ROM_FLYME
            } else {
                sVersion = Build.UNKNOWN
                sName = Build.MANUFACTURER.toUpperCase()
            }
        }
        return sName == rom
    }

    private fun getProp(name: String): String? {
        var line: String? = null
        var input: BufferedReader? = null
        try {
            val p = Runtime.getRuntime().exec("getprop $name")
            input = BufferedReader(InputStreamReader(p.inputStream), 1024)
            line = input.readLine()
            input.close()
        } catch (ex: Exception) {
            Log.e(TAG, "Unable to read prop $name", ex)
            return null
        } finally {
            if (input != null) {
                try {
                    input.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
        return line
    }
}

/**
 * 设置状态栏纯色 不加半透明效果
 * @param color    状态栏颜色值
 */
fun Activity.setColorNoTranslucent(@ColorInt color: Int) {
    setColor(color, 0)
}

/**
 * 设置状态栏颜色
 * @param color          状态栏颜色值
 * @param statusBarAlpha 状态栏透明度
 */
fun Activity.setColor(
    @ColorInt color: Int,
    statusBarAlpha: Int = StatusBarUtils.DEFAULT_STATUS_BAR_ALPHA
) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = StatusBarUtils.calculateStatusColor(color, statusBarAlpha)
    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        val decorView = window.decorView as ViewGroup
        val fakeStatusBarView = decorView.findViewById<View>(StatusBarUtils.FAKE_STATUS_BAR_VIEW_ID)
        if (fakeStatusBarView != null) {
            if (fakeStatusBarView.visibility == View.GONE) {
                fakeStatusBarView.visibility = View.VISIBLE
            }
            fakeStatusBarView.setBackgroundColor(
                StatusBarUtils.calculateStatusColor(
                    color,
                    statusBarAlpha
                )
            )
        } else {
            decorView.addView(StatusBarUtils.createStatusBarView(this, color, statusBarAlpha))
        }
        StatusBarUtils.setRootView(this)
    }
}

/**
 * 为滑动返回界面设置状态栏颜色
 *
 * @param color          状态栏颜色值
 * @param statusBarAlpha 状态栏透明度
 */
fun Activity.setColorForSwipeBack(
    @ColorInt color: Int,
    statusBarAlpha: Int = StatusBarUtils.DEFAULT_STATUS_BAR_ALPHA
) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
        val contentView = findViewById<View>(android.R.id.content) as ViewGroup
        val rootView = contentView.getChildAt(0)
        val statusBarHeight = StatusBarUtils.getStatusBarHeight(this)
        if (rootView != null && rootView is CoordinatorLayout) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                rootView.fitsSystemWindows = false
                contentView.setBackgroundColor(
                    StatusBarUtils.calculateStatusColor(
                        color,
                        statusBarAlpha
                    )
                )
                val isNeedRequestLayout = contentView.paddingTop < statusBarHeight
                if (isNeedRequestLayout) {
                    contentView.setPadding(0, statusBarHeight, 0, 0)
                    rootView.post { rootView.requestLayout() }
                }
            } else {
                rootView.setStatusBarBackgroundColor(
                    StatusBarUtils.calculateStatusColor(
                        color,
                        statusBarAlpha
                    )
                )
            }
        } else {
            contentView.setPadding(0, statusBarHeight, 0, 0)
            contentView.setBackgroundColor(
                StatusBarUtils.calculateStatusColor(
                    color,
                    statusBarAlpha
                )
            )
        }
        StatusBarUtils.setTransparentForWindow(this)
    }
}

/**
 * 使状态栏半透明
 * 适用于图片作为背景的界面,此时需要图片填充到状态栏
 * @param statusBarAlpha 状态栏透明度
 */
fun Activity.setTranslucent(statusBarAlpha: Int = StatusBarUtils.DEFAULT_STATUS_BAR_ALPHA) {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
        return
    }
    setTransparent()
    StatusBarUtils.addTranslucentView(this, statusBarAlpha)
}

/**
 * 针对根布局是 CoordinatorLayout, 使状态栏半透明
 * 适用于图片作为背景的界面,此时需要图片填充到状态栏
 * @param statusBarAlpha 状态栏透明度
 */
fun Activity.setTranslucentForCoordinatorLayout(statusBarAlpha: Int) {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
        return
    }
    StatusBarUtils.transparentStatusBar(this)
    StatusBarUtils.addTranslucentView(this, statusBarAlpha)
}

/**
 * 设置状态栏全透明
 */
fun Activity.setTransparent() {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
        return
    }
    //transparentStatusBar(activity);
    StatusBarUtils.setTransparentForWindow(this)
    StatusBarUtils.setRootView(this)
}

/**
 * 为DrawerLayout 布局设置状态栏变色
 *
 * @param drawerLayout   DrawerLayout
 * @param color          状态栏颜色值
 * @param statusBarAlpha 状态栏透明度
 */
fun Activity.setColorForDrawerLayout(
    drawerLayout: DrawerLayout, @ColorInt color: Int,
    statusBarAlpha: Int = StatusBarUtils.DEFAULT_STATUS_BAR_ALPHA
) {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
        return
    }
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = Color.TRANSPARENT
    } else {
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
    }
    // 生成一个状态栏大小的矩形
    // 添加 statusBarView 到布局中
    val contentLayout = drawerLayout.getChildAt(0) as ViewGroup
    val fakeStatusBarView = contentLayout.findViewById<View>(StatusBarUtils.FAKE_STATUS_BAR_VIEW_ID)
    if (fakeStatusBarView != null) {
        if (fakeStatusBarView.visibility == View.GONE) {
            fakeStatusBarView.visibility = View.VISIBLE
        }
        fakeStatusBarView.setBackgroundColor(color)
    } else {
        contentLayout.addView(StatusBarUtils.createStatusBarView(this, color), 0)
    }
    // 内容布局不是 LinearLayout 时,设置padding top
    if (contentLayout !is LinearLayout && contentLayout.getChildAt(1) != null) {
        contentLayout.getChildAt(1)
            .setPadding(
                contentLayout.paddingLeft,
                StatusBarUtils.getStatusBarHeight(this) + contentLayout.paddingTop,
                contentLayout.paddingRight,
                contentLayout.paddingBottom
            )
    }
    // 设置属性
    StatusBarUtils.setDrawerLayoutProperty(drawerLayout, contentLayout)
    StatusBarUtils.addTranslucentView(this, statusBarAlpha)
}

/**
 * 为 DrawerLayout 布局设置状态栏透明
 * @param drawerLayout DrawerLayout
 */
fun Activity.setTranslucentForDrawerLayout(
    drawerLayout: DrawerLayout,
    statusBarAlpha: Int = StatusBarUtils.DEFAULT_STATUS_BAR_ALPHA
) {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
        return
    }
    setTransparentForDrawerLayout(drawerLayout)
    StatusBarUtils.addTranslucentView(this, statusBarAlpha)
}

/**
 * 为 DrawerLayout 布局设置状态栏透明
 *
 * @param drawerLayout DrawerLayout
 */
fun Activity.setTransparentForDrawerLayout(drawerLayout: DrawerLayout) {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
        return
    }
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = Color.TRANSPARENT
    } else {
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
    }
    val contentLayout = drawerLayout.getChildAt(0) as ViewGroup
    // 内容布局不是 LinearLayout 时,设置padding top
    if (contentLayout !is LinearLayout && contentLayout.getChildAt(1) != null) {
        contentLayout.getChildAt(1).setPadding(0, StatusBarUtils.getStatusBarHeight(this), 0, 0)
    }
    // 设置属性
    StatusBarUtils.setDrawerLayoutProperty(drawerLayout, contentLayout)
}

/**
 * 为头部是 ImageView 的界面设置状态栏透明
 *
 * @param statusBarAlpha 状态栏透明度
 * @param needOffsetView 需要向下偏移的 View
 */
fun Activity.setTranslucentForImageView(
    statusBarAlpha: Int = StatusBarUtils.DEFAULT_STATUS_BAR_ALPHA,
    needOffsetView: View?
) {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
        return
    }
    StatusBarUtils.setTransparentForWindow(this)
    StatusBarUtils.addTranslucentView(this, statusBarAlpha)
    if (needOffsetView != null) {
        val haveSetOffset = needOffsetView.getTag(StatusBarUtils.TAG_KEY_HAVE_SET_OFFSET)
        if (haveSetOffset != null && haveSetOffset as Boolean) {
            return
        }
        val layoutParams = needOffsetView.layoutParams as ViewGroup.MarginLayoutParams
        layoutParams.setMargins(
            layoutParams.leftMargin,
            layoutParams.topMargin + StatusBarUtils.getStatusBarHeight(this),
            layoutParams.rightMargin,
            layoutParams.bottomMargin
        )
        needOffsetView.setTag(StatusBarUtils.TAG_KEY_HAVE_SET_OFFSET, true)
    }
}

/**
 * 为 fragment 头部是 ImageView 的设置状态栏透明
 *
 * @param statusBarAlpha 状态栏透明度
 * @param needOffsetView 需要向下偏移的 View
 */
fun Activity.setTranslucentForImageViewInFragment(
    statusBarAlpha: Int = DEFAULT_STATUS_BAR_ALPHA,
    needOffsetView: View
) {
    setTranslucentForImageView(statusBarAlpha, needOffsetView)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
        StatusBarUtils.clearPreviousSetting(this)
    }
}

/**
 * 隐藏伪状态栏 View
 */
fun Activity.hideFakeStatusBarView() {
    val decorView = window.decorView as ViewGroup
    val fakeStatusBarView = decorView.findViewById<View>(StatusBarUtils.FAKE_STATUS_BAR_VIEW_ID)
    if (fakeStatusBarView != null) {
        fakeStatusBarView.visibility = View.GONE
    }
    val fakeTranslucentView = decorView.findViewById<View>(StatusBarUtils.FAKE_TRANSLUCENT_VIEW_ID)
    if (fakeTranslucentView != null) {
        fakeTranslucentView.visibility = View.GONE
    }
}

/**
 * 已知系统类型时，设置状态栏黑色字体图标。
 * 适配4.4以上版本MIUIV、Flyme和6.0以上版本其他Android
 */
fun Activity.setStatusBarMode(isDark: Boolean = false) {
    if (StatusBarUtils.mIUISetStatusBarLightMode(window, true)) {
        StatusBarUtils.mIUISetStatusBarLightMode(window, isDark)
    } else if (StatusBarUtils.fLymeSetStatusBarLightMode(window, true)) {
        StatusBarUtils.fLymeSetStatusBarLightMode(window, isDark)
    } else {
        if (isDark) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                window.decorView.systemUiVisibility =
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            } else {
                window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            }
        } else {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
        }
    }
}