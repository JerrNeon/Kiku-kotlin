package com.jn.kikukt.common

import android.app.Activity
import android.app.Application
import android.content.ComponentCallbacks
import android.content.res.Configuration
import androidx.annotation.IntDef
import android.util.DisplayMetrics
import java.text.DecimalFormat

/**
 * Author：Stevie.Chen Time：2019/7/15
 * Class Comment：
 */
class DensityManager {

    private var DESIGN_WIDTH = 375//对应设计图屏幕宽度为750px
    private var DESIGN_HEIGHT = 667//对应设计图屏幕高度为1334px

    private var appDensity: Float = 0f//像素密度比例
    private var appScaledDensity: Float = 0f//缩放因子
    private var appDisplayMetrics: DisplayMetrics? = null//屏幕密度尺寸转换类

    @IntDef(
        WIDTH,
        HEIGHT
    )
    @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
    internal annotation class Orientation

    companion object {
        private const val WIDTH = 1//宽
        private const val HEIGHT = 2//高

        val instance: DensityManager by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            DensityManager()
        }
    }

    /**
     * 初始化Density
     * 默认以750 x 1334的设计图来
     *
     * @param application Application
     */
    fun initDensity(application: Application) {
        initDensity(application, 0, 0)
    }

    /**
     * 初始化Density
     *
     * @param application  Application
     * @param designWidth  设计图宽度(单位：dp)
     * @param designHeight 设计图高度(单位：dp)
     */
    fun initDensity(application: Application, designWidth: Int, designHeight: Int) {
        //获取application的DisplayMetrics
        appDisplayMetrics = application.resources.displayMetrics

        if (appDensity == 0f) {
            //初始化的时候赋值
            appDensity = appDisplayMetrics!!.density
            appScaledDensity = appDisplayMetrics!!.scaledDensity

            //添加字体变化的监听
            application.registerComponentCallbacks(object : ComponentCallbacks {
                override fun onConfigurationChanged(newConfig: Configuration?) {
                    //字体改变后,将appScaledDensity重新赋值
                    if (newConfig != null && newConfig.fontScale > 0) {
                        appScaledDensity = application.resources.displayMetrics.scaledDensity
                    }
                }

                override fun onLowMemory() {}
            })
        }
        if (designWidth > 0)
            DESIGN_WIDTH = designWidth
        if (designHeight > 0)
            DESIGN_HEIGHT = designHeight
    }

    /**
     * 设置Activity默认的适配(基于屏幕宽度)
     *
     * @param activity Activity
     */
    fun setDensity(activity: Activity) {
        setAppOrientation(activity, WIDTH)
    }

    /**
     * 设置Activity基于某个方向的适配
     *
     * @param activity    Activity
     * @param orientation Orientation
     */
    fun setDensity(activity: Activity, @Orientation orientation: Int) {
        setAppOrientation(activity, orientation)
    }

    /**
     * 设置Activity中的独立像素比、缩放因子及像素比例
     *
     * @param activity    Activity
     * @param orientation Orientation(WIDTH | HEIGHT)
     */
    private fun setAppOrientation(activity: Activity?, @Orientation orientation: Int) {
        val targetDensity = getTargetDensity(orientation)
        val targetScaledDensity = targetDensity * (appScaledDensity / appDensity)
        val targetDensityDpi = (160 * targetDensity).toInt()

        /**
         *
         * 最后在这里将修改过后的值赋给系统参数
         *
         * 只修改Activity的density值
         */

        val activityDisplayMetrics = activity!!.resources.displayMetrics
        activityDisplayMetrics.density = targetDensity
        activityDisplayMetrics.scaledDensity = targetScaledDensity
        activityDisplayMetrics.densityDpi = targetDensityDpi
    }

    /**
     * 获取最终的像素比例
     *
     * @param orientation Orientation(WIDTH | HEIGHT)
     * @return Float
     */
    private fun getTargetDensity(@Orientation orientation: Int): Float {
        var targetDensity = 0f
        try {
            val division: Double?
            //根据带入参数选择不同的适配方向
            division = if (orientation == WIDTH) {
                division(appDisplayMetrics?.widthPixels ?: 0, DESIGN_WIDTH)
            } else {
                division(appDisplayMetrics?.heightPixels ?: 0, DESIGN_HEIGHT)
            }
            //由于手机的长宽不尽相同,肯定会有除不尽的情况,有失精度,所以在这里把所得结果做了一个保留两位小数的操作
            val df = DecimalFormat("0.00")
            val s = df.format(division)
            targetDensity = java.lang.Float.parseFloat(s)
        } catch (e: NumberFormatException) {
            e.printStackTrace()
        }

        return targetDensity
    }

    /**
     * 获取两个数的商
     *
     * @param a 除数
     * @param b 被除数
     * @return
     */
    private fun division(a: Int, b: Int): Double {
        var div = 0.0
        if (b != 0) {
            div = (a / b).toDouble()
        }
        return div
    }
}