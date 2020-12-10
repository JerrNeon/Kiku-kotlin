package com.jn.kikukt.utils

import android.content.Context
import androidx.startup.Initializer
import com.jn.kikukt.BuildConfig
import com.jn.kikukt.common.exception.CrashHandler

/**
 * Author：Stevie.Chen Time：2020/12/10
 * Class Comment：core初始化
 */
class CoreInitializer : Initializer<Unit> {
    override fun create(context: Context) {
        context.let {
            if (!BuildConfig.DEBUG)
                CrashHandler.instance.init(it)
            WebViewUtils.initX5Environment(it)
        }
    }

    override fun dependencies(): MutableList<Class<out Initializer<*>>> {
        return mutableListOf()
    }
}