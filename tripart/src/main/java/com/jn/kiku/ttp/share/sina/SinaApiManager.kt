package com.jn.kiku.ttp.share.sina

import com.jn.kikukt.net.coroutines.manager.IRetrofitManager

/**
 * Author：Stevie.Chen Time：2020/9/8
 * Class Comment：
 */
object SinaApiManager : IRetrofitManager {

    val service by lazy { create(SinaApiService::class.java, SinaApiService.SinaBaseUrl) }
}