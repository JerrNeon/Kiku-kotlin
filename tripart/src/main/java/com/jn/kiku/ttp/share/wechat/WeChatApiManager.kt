package com.jn.kiku.ttp.share.wechat

import com.jn.kikukt.net.coroutines.manager.IRetrofitManager

/**
 * Author：Stevie.Chen Time：2020/9/8
 * Class Comment：
 */
object WeChatApiManager : IRetrofitManager {

    val service by lazy { create(WeChatApiService::class.java, WeChatApiService.WeChatBaseUrl) }
}