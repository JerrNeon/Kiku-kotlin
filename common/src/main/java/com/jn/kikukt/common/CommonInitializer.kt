package com.jn.kikukt.common

import android.content.Context
import androidx.startup.Initializer
import com.jn.kikukt.common.utils.ContextUtils

/**
 * Author：Stevie.Chen Time：2020/12/10
 * Class Comment：common初始化
 */
class CommonInitializer : Initializer<Unit> {

    override fun create(context: Context) {
        //ContextUtils.context = context
    }

    override fun dependencies(): MutableList<Class<out Initializer<*>>> {
        return mutableListOf()
    }
}