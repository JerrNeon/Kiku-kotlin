package com.jn.kikukt.common.utils

import android.app.Application
import android.content.Context

/**
 * Author：Stevie.Chen Time：2019/7/15
 * Class Comment：
 */
object ContextUtils {
    lateinit var application: Application
    val context: Context
        get() = application.applicationContext
}