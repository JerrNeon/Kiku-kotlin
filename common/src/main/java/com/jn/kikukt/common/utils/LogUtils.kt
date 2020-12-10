package com.jn.kikukt.common.utils

import android.util.Log
import com.jn.common.BuildConfig
import com.jn.kikukt.common.utils.LogUtils.LOG_ENABLE
import com.jn.kikukt.common.utils.LogUtils.TAG

/**
 * Author：Stevie.Chen Time：2019/7/12
 * Class Comment：
 */
object LogUtils {
    internal var LOG_ENABLE: Boolean = true//这里建议根据buildType来区分是否打印日志，一般只在Debug环境下打印
    internal var TAG: String = "LogUtils"

    /**
     * 初始化Log
     *
     * @param logEnable 是否开启日志，建议debug开启日志信息，release关闭日志信息
     * @param tagName   Tag名称
     */
    fun init(tagName: String = "LogUtils", logEnable: Boolean = BuildConfig.DEBUG) {
        TAG = tagName
        LOG_ENABLE = logEnable
    }
}

fun String.logI() {
    logI(TAG)
}

fun String.logI(tag: String) {
    if (LOG_ENABLE)
        Log.i(tag, this)
}

fun String.logD() {
    logD(TAG)
}

fun String.logD(tag: String) {
    if (LOG_ENABLE)
        Log.d(tag, this)
}

fun String.logW() {
    logW(TAG)
}

fun String.logW(tag: String) {
    if (LOG_ENABLE)
        Log.w(tag, this)
}

fun String.logE() {
    logE(TAG)
}

fun String.logE(tag: String) {
    if (LOG_ENABLE)
        Log.d(tag, this)
}

fun String.loV() {
    logV(TAG)
}

fun String.logV(tag: String) {
    if (LOG_ENABLE)
        Log.d(tag, this)
}

fun Throwable.log() {
    log(TAG)
}

fun Throwable.log(tag: String) {
    if (LOG_ENABLE)
        Log.e(tag, Log.getStackTraceString(this))
}
