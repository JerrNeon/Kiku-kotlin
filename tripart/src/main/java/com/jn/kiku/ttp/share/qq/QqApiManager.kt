package com.jn.kiku.ttp.share.qq

import com.jn.kikukt.net.coroutines.manager.IRetrofitManager

/**
 * Author：Stevie.Chen Time：2020/9/8
 * Class Comment：
 */
object QqApiManager : IRetrofitManager {

    val service by lazy { create(QqApiService::class.java, QqApiService.QQBaseUrl) }
}