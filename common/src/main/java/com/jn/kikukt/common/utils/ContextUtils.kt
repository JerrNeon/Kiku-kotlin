package com.jn.kikukt.common.utils

import android.app.Application
import android.content.Context

/**
 * Author：Stevie.Chen Time：2019/7/15
 * Class Comment：
 */
object ContextUtils {

    private var application: Application? = null

    fun init(application: Application) {
        this.application = application
    }

    fun getApplication(): Application = application!!

    fun getContext(): Context = application!!.applicationContext
}