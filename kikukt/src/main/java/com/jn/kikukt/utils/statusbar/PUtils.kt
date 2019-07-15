package com.jn.kikukt.utils.statusbar

import android.app.Activity
import android.content.Context
import android.os.Build
import android.util.Log
import android.view.DisplayCutout
import android.view.WindowManager

/**
 * Author：Stevie.Chen Time：2019/7/15
 * Class Comment：Android P适配
 */
object PUtils {

    private const val VIVO_NOTCH = 0x00000020//是否有刘海
    private const val VIVO_FILLET = 0x00000008//是否有圆角

    /**
     * @param activity Activity
     *
     * LAYOUT_IN_DISPLAY_CUTOUT_MODE_DEFAULT：
     * 只有当DisplayCutout完全包含在系统栏中时，才允许窗口延伸到DisplayCutout区域。
     * 否则，窗口布局不与DisplayCutout区域重叠。
     *
     * LAYOUT_IN_DISPLAY_CUTOUT_MODE_NEVER：
     * 该窗口决不允许与DisplayCutout区域重叠。
     *
     * LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES：
     * 该窗口始终允许延伸到屏幕短边上的DisplayCutout区域。
     *
     * LAYOUT_IN_DISPLAY_CUTOUT_MODE_DEFAULT：模式在全屏显示下跟LAYOUT_IN_DISPLAY_CUTOUT_MODE_NEVER一样。
     * LAYOUT_IN_DISPLAY_CUTOUT_MODE_NEVER：模式不会让屏幕到延申刘海区域中，会留出一片黑色区域。
     * LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES：模式会让屏幕到延申刘海区域中。
     */
    fun setNotchScreen(activity: Activity) {
        val window = activity.window
        val lp = window.attributes
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            lp.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_DEFAULT
        }
        //lp.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_NEVER;
        //lp.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        window.attributes = lp
    }

    fun setDisplayCutoutInfoListener(activity: Activity) {
        val window = activity.window
        val decorView = window.decorView
        decorView.post(object : Runnable {
            override fun run() {
                var displayCutout: DisplayCutout? = null
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    displayCutout = decorView.rootWindowInsets.displayCutout
                    Log.e("TAG", "安全区域距离屏幕左边的距离 SafeInsetLeft:" + displayCutout!!.safeInsetLeft)
                    Log.e("TAG", "安全区域距离屏幕右部的距离 SafeInsetRight:" + displayCutout.safeInsetRight)
                    Log.e("TAG", "安全区域距离屏幕顶部的距离 SafeInsetTop:" + displayCutout.safeInsetTop)
                    Log.e("TAG", "安全区域距离屏幕底部的距离 SafeInsetBottom:" + displayCutout.safeInsetBottom)
                    val rectList = displayCutout.boundingRects
                    if (rectList != null && rectList.size > 0) {
                        val rect = rectList[0]
                        if (rect != null)
                            Log.e(javaClass.simpleName, "刘海屏区域：$rect")
                    }
                }
            }
        })
    }

    /**
     * 华为手机是否有刘海屏
     *
     * @param context
     * @return true为有刘海，false则没有
     */
    fun hasNotchAtHuawei(context: Context): Boolean {
        var ret = false
        try {
            val classLoader = context.classLoader
            val HwNotchSizeUtil = classLoader.loadClass("com.huawei.android.util.HwNotchSizeUtil")
            val get = HwNotchSizeUtil.getMethod("hasNotchInScreen")
            ret = get.invoke(HwNotchSizeUtil) as Boolean
        } catch (e: ClassNotFoundException) {
            Log.e("Notch", "hasNotchAtHuawei ClassNotFoundException")
        } catch (e: NoSuchMethodException) {
            Log.e("Notch", "hasNotchAtHuawei NoSuchMethodException")
        } catch (e: Exception) {
            Log.e("Notch", "hasNotchAtHuawei Exception")
        } finally {
            return ret
        }
    }

    /**
     * 华为手机获取刘海尺寸：width、height
     *
     * @param context
     * @return int[0]值为刘海宽度 int[1]值为刘海高度
     */
    fun getNotchSizeAtHuawei(context: Context): IntArray {
        var ret = intArrayOf(0, 0)
        try {
            val cl = context.classLoader
            val HwNotchSizeUtil = cl.loadClass("com.huawei.android.util.HwNotchSizeUtil")
            val get = HwNotchSizeUtil.getMethod("getNotchSize")
            ret = get.invoke(HwNotchSizeUtil) as IntArray
        } catch (e: ClassNotFoundException) {
            Log.e("Notch", "getNotchSizeAtHuawei ClassNotFoundException")
        } catch (e: NoSuchMethodException) {
            Log.e("Notch", "getNotchSizeAtHuawei NoSuchMethodException")
        } catch (e: Exception) {
            Log.e("Notch", "getNotchSizeAtHuawei Exception")
        } finally {
            return ret
        }
    }

    /**
     * ViVo手机是否有刘海屏
     *
     * @param context
     * @return true为有刘海，false则没有
     */
    fun hasNotchAtViVo(context: Context): Boolean {
        var ret = false
        try {
            val classLoader = context.classLoader
            val FtFeature = classLoader.loadClass("android.util.FtFeature")
            val method = FtFeature.getMethod("isFeatureSupport", Int::class.javaPrimitiveType)
            ret = method.invoke(FtFeature, VIVO_NOTCH) as Boolean
        } catch (e: ClassNotFoundException) {
            Log.e("Notch", "hasNotchAtVoio ClassNotFoundException")
        } catch (e: NoSuchMethodException) {
            Log.e("Notch", "hasNotchAtVoio NoSuchMethodException")
        } catch (e: Exception) {
            Log.e("Notch", "hasNotchAtVoio Exception")
        } finally {
            return ret
        }
    }

    /**
     * ViVo手机获取刘海尺寸：width、height
     * vivo不提供接口获取刘海尺寸，目前vivo的刘海宽为100dp,高为27dp。
     *
     * @return int[0]值为刘海宽度 int[1]值为刘海高度
     */
    fun getNotchSizeAtViVo(): IntArray {
        return intArrayOf(100, 20)
    }

    /**
     * OPPO手机是否有刘海屏
     *
     * @param context
     * @return true为有刘海，false则没有
     */
    fun hasNotchInScreenAtOPPO(context: Context): Boolean {
        return context.packageManager.hasSystemFeature("com.oppo.feature.screen.heteromorphism")
    }

    /**
     * 获取OPPO手机刘海尺寸：width、height
     * OPPO不提供接口获取刘海尺寸，目前OPPO的刘海区域则都是宽度为324px, 高度为80px
     *
     * @return int[0]值为刘海宽度 int[1]值为刘海高度
     */
    fun getNotchSizeAtOPPO(): IntArray {
        return intArrayOf(324, 80)
    }
}