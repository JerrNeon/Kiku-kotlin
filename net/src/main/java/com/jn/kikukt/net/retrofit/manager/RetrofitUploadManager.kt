package com.jn.kikukt.net.retrofit.manager

import com.jn.kikukt.net.retrofit.body.UploadRequestBody
import com.jn.kikukt.net.retrofit.callback.ProgressListener
import okhttp3.Interceptor

/**
 * Author：Stevie.Chen Time：2019/7/15
 * Class Comment：Retrofit上传
 */
class RetrofitUploadManager(base_url: String, val listener: ProgressListener?) : RetrofitRequestManager(base_url) {

    override fun createInterceptor(): Interceptor? {
        return Interceptor { chain ->
            val originalRequest = chain.request()
            chain.proceed(
                originalRequest
                    .newBuilder()
                    .post(UploadRequestBody(originalRequest.body()!!, getProgressListener()))
                    .build()
            )
        }
    }

    override fun getProgressListener(): ProgressListener? {
        return listener
    }

    override fun getConnectTimeout(): Int {
        return super.getConnectTimeout() * 2
    }

    override fun getReadTimeout(): Int {
        return super.getReadTimeout() * 2
    }

    override fun getWriteTimeout(): Int {
        return super.getWriteTimeout() * 2
    }
}