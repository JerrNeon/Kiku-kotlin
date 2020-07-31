package com.jn.examplekotlin.request.coroutines

import com.jn.examplekotlin.BuildConfig
import com.jn.kikukt.net.coroutines.manager.IBaseRetrofitManager

/**
 * Author：Stevie.Chen Time：2020/1/15
 * Class Comment：
 */
object ApiManager : IBaseRetrofitManager {

    val service by lazy { create(Api::class.java, BuildConfig.BASE_URL) }
}