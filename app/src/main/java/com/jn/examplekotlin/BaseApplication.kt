package com.jn.examplekotlin

import com.jn.kikukt.RootApplication

/**
 * Author：Stevie.Chen Time：2019/9/12
 * Class Comment：
 */
class BaseApplication : RootApplication() {

    override fun onCreate() {
        super.onCreate()
        initActivityManager()
        initContextUtils()
        initWebViewUtils()
        initLogUtils("kiku_kotlin")
    }
}