package com.jn.examplekotlin

import com.jn.kikukt.RootApplication

/**
 * Author：Stevie.Chen Time：2019/9/12
 * Class Comment：
 */
class BaseApplication : RootApplication() {

    override fun isLOG_DEBUG(): Boolean = BuildConfig.DEBUG

    override fun onCreate() {
        super.onCreate()
        initActivityManager()
        initRetrofit(BuildConfig.BASE_URL)
        initUtilsManager("kiku_kotlin")
    }
}