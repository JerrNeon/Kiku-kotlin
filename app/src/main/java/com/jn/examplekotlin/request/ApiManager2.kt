package com.jn.examplekotlin.request

import com.jn.examplekotlin.BuildConfig
import com.jn.kikukt.net.coroutines.IBaseRetrofitManager

/**
 * Author：Stevie.Chen Time：2020/1/15
 * Class Comment：
 */
object ApiManager2 : IBaseRetrofitManager {

    val service by lazy { create(Api::class.java, BuildConfig.BASE_URL) }
}