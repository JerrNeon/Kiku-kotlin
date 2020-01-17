package com.jn.kikukt.net.coroutines

import com.jn.kikukt.net.RetrofitApiService
import com.jn.kikukt.net.retrofit.body.UploadRequestBody
import com.jn.kikukt.net.retrofit.callback.ProgressListener
import okhttp3.Interceptor

/**
 * Author：Stevie.Chen Time：2020/1/15
 * Class Comment：
 */
class RetrofitUploadManager(val baseUrl: String, val listener: ProgressListener?) :
    IBaseRetrofitManager {

    val service by lazy { create(RetrofitApiService::class.java, baseUrl) }

    override val interceptor: Interceptor?
        get() = Interceptor { chain ->
            val originalRequest = chain.request()
            chain.proceed(
                originalRequest
                    .newBuilder()
                    .post(UploadRequestBody(originalRequest.body!!, progressListener))
                    .build()
            )
        }

    override val progressListener: ProgressListener?
        get() = listener

    override val connectTimeout: Long
        get() = super.connectTimeout * 2

    override val readTimeout: Long
        get() = super.readTimeout * 2

    override val writeTimeout: Long
        get() = super.writeTimeout * 2
}