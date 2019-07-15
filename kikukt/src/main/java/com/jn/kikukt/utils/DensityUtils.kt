package com.jn.kikukt.utils

import android.app.Activity
import android.content.Context
import android.util.DisplayMetrics
import android.util.TypedValue

/**
 * Author：Stevie.Chen Time：2019/7/12
 * Class Comment：
 */

/**
 * dp转px
 */
fun Context.dp2px(dpVal: Float): Float {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dpVal,
        resources.displayMetrics
    )
}

/**
 * px转dp
 */
fun Context.px2dp(pxVal: Float): Float {
    val scale = resources.displayMetrics.density
    return pxVal / scale
}

/**
 * sp转px
 */
fun Context.sp2px(spVal: Float): Float {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_SP,
        spVal,
        resources.displayMetrics
    )
}

/**
 * px转sp
 */
fun Context.px2sp(pxVal: Float): Float {
    return pxVal / resources.displayMetrics.scaledDensity
}

/**
 * 获取dp
 */
fun Context.getIntDip(value: Float): Int {
    return getFloatDip(value).toInt()
}

/**
 * 获取dp
 */
fun Context.getFloatDip(value: Float): Float {
    return resources.displayMetrics.density * value
}

/**
 * 根据收集的分辨率指定字体的大小
 */
fun Context.pixelsToDip(): Int {
    val dm = DisplayMetrics()
    if (this is Activity)
        this.windowManager.defaultDisplay.getMetrics(dm)
    var screenWidth = dm.widthPixels
    val screenHeight = dm.heightPixels
    screenWidth = if (screenWidth > screenHeight) screenWidth else screenHeight
    /**
     * 1. 在视图的 onsizechanged里获取视图宽度，一般情况下默认宽度是320，所以计算一个缩放比率
    rate = (float) w/320   w是实际宽度
    2.然后在设置字体尺寸时 paint.setTextSize((int)(8*rate));   8是在分辨率宽为320 下需要设置的字体大小
    实际字体大小 = 默认字体大小 x  rate
     */
    val rate = (3 * screenWidth.toFloat() / 320).toInt() //我自己测试这个倍数比较适合，当然你可以测试后再修改
    return if (rate < 15) 15 else rate //字体太小也不好看的
}