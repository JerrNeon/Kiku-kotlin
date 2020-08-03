package com.jn.examplekotlin.request.rxjava

import com.jn.examplekotlin.BuildConfig
import com.jn.kikukt.net.rxjava.manager.IRetrofitManager

/**
 * Author：Stevie.Chen Time：2019/9/12
 * Class Comment：
 */
object ApiManager : IRetrofitManager {

    val service by lazy {
        create(Api::class.java, BuildConfig.BASE_URL)
    }
}