package com.jn.examplekotlin.request

import com.jn.examplekotlin.BuildConfig
import com.jn.kikukt.net.retrofit.manager.RetrofitRequestManager

/**
 * Author：Stevie.Chen Time：2019/9/12
 * Class Comment：
 */
class ApiManager {

    private var mApi: Api? = null

    companion object {
        val instance: ApiManager by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) { ApiManager() }
    }

    fun getApiService(): Api {
        if (mApi == null) {
            val manager = RetrofitRequestManager(BuildConfig.BASE_URL)
            val retrofit = manager.createRetrofit()
            mApi = retrofit.create(Api::class.java)
        }
        return mApi!!
    }
}