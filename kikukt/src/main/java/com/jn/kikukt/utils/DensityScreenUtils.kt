package com.jn.kikukt.utils

import android.app.Activity
import android.app.Application
import android.content.ComponentCallbacks
import android.content.res.Configuration
import kotlin.math.sqrt

/**
 * Author：Stevie.Chen Time：2019/7/15
 * Class Comment：屏幕适配 ， 通过修改Density中的值
 * <P>
 * densityDpi ：像素密度
 * density：dpi / 160;
 * scaledDensity：字体的缩放因子
 * </p>
 */
object DensityScreenUtils {

    private var sNonCompatDensity: Float = 0f
    private var sNonCompatScaledDensity: Float = 0f

    fun setDensity(activity: Activity, application: Application, screenWidthDp: Float) {
        val appDisplayMetrics = application.resources.displayMetrics

        if (sNonCompatDensity == 0f) {
            sNonCompatDensity = appDisplayMetrics.density
            sNonCompatScaledDensity = appDisplayMetrics.scaledDensity
            application.registerComponentCallbacks(object : ComponentCallbacks {
                override fun onConfigurationChanged(newConfig: Configuration?) {
                    //用户修改了系统字体大小
                    if (newConfig != null && newConfig.fontScale > 0) {
                        sNonCompatScaledDensity = application.resources.displayMetrics.scaledDensity
                    }
                }

                override fun onLowMemory() {

                }
            })
        }

        //appDisplayMetrics.widthPixels：屏幕宽度(px)
        val targetDensity = appDisplayMetrics.widthPixels / screenWidthDp
        val targetScaledDensity = targetDensity * (sNonCompatScaledDensity / sNonCompatDensity)
        val targetDensityDpi = (160 * targetDensity).toInt()

        appDisplayMetrics.density = targetDensity
        appDisplayMetrics.scaledDensity = targetScaledDensity
        appDisplayMetrics.densityDpi = targetDensityDpi

        val activityDisplayMetrics = activity.resources.displayMetrics
        activityDisplayMetrics.density = targetDensity
        activityDisplayMetrics.scaledDensity = targetScaledDensity
        activityDisplayMetrics.densityDpi = targetDensityDpi
    }

    /**
     * 获取屏幕宽度(dp)
     *
     * @param screenWidth  屏幕宽度(px)
     * @param screenHeight 屏幕高度(px)
     * @param screenSize   屏幕尺寸(英寸)
     * @return
     */
    fun getScreenWidthDp(screenWidth: Int, screenHeight: Int, screenSize: Float): Float {
        val densityDpi =
            (sqrt((screenWidth * screenWidth + screenHeight * screenHeight).toDouble()) / screenSize).toFloat()
        val density = densityDpi / 160
        return screenWidth / density
    }

}