package com.jn.kikukt.retrofit.manager

import com.jn.kikukt.retrofit.body.DownloadResponseBody
import com.jn.kikukt.retrofit.callback.ProgressListener
import okhttp3.Interceptor

/**
 * Author：Stevie.Chen Time：2019/7/15
 * Class Comment：Retrofit下载
 */
class RetrofitDownloadManager(base_url: String, val listener: ProgressListener?) : RetrofitRequestManager(base_url) {

    override fun createInterceptor(): Interceptor? {
        return Interceptor { chain ->
            val originalResponse = chain.proceed(chain.request())
            originalResponse.newBuilder()
                .body(DownloadResponseBody(originalResponse.body()!!, getProgressListener()!!))
                .build()
        }
    }

    override fun getProgressListener(): ProgressListener? {
        return listener
    }

    override fun getConnectTimeout(): Int {
        return super.getConnectTimeout() * 3
    }

    override fun getReadTimeout(): Int {
        return super.getReadTimeout() * 3
    }

    override fun getWriteTimeout(): Int {
        return super.getWriteTimeout() * 3
    }
}