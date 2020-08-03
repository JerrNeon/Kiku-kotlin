package com.jn.kikukt.net.rxjava.manager

import com.jn.kikukt.net.retrofit.body.DownloadResponseBody
import com.jn.kikukt.net.retrofit.callback.ProgressListener
import okhttp3.Interceptor

/**
 * Author：Stevie.Chen Time：2020/1/15
 * Class Comment：Retrofit下载
 */
class RetrofitDownloadManager(val listener: ProgressListener?) : IRetrofitManager {

    override val interceptor: Interceptor?
        get() = Interceptor { chain ->
            val originalResponse = chain.proceed(chain.request())
            val responseBody = originalResponse.body
            originalResponse.newBuilder()
                .body(
                    if (responseBody != null) DownloadResponseBody(
                        responseBody,
                        progressListener
                    ) else null
                )
                .build()
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