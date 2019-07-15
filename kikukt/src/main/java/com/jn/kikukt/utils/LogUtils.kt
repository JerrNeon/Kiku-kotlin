package com.jn.kikukt.utils

import android.util.Log
import com.jn.kikukt.utils.LogUtils.LOG_ENABLE
import com.jn.kikukt.utils.LogUtils.TAG

/**
 * Author：Stevie.Chen Time：2019/7/12
 * Class Comment：
 */
object LogUtils {
    var LOG_ENABLE: Boolean = true//这里建议根据buildType来区分是否打印日志，一般只在Debug环境下打印
    var TAG: String = "LogUtils"
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
