package com.jn.kikukt.net.retrofit.manager

import android.os.Build
import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.jn.kikukt.net.BuildConfig
import com.jn.kikukt.net.retrofit.callback.ProgressListener
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Modifier
import java.util.concurrent.TimeUnit

/**
 * Author：Stevie.Chen Time：2019/7/15
 * Class Comment：Retrofit请求
 */
open class RetrofitRequestManager(val base_url: String) : IRetrofitManage {

    private val TAG = Retrofit::class.java.simpleName

    override fun createRetrofit(): Retrofit {
        return createRetrofitBuilder().build()
    }

    override fun createRetrofitBuilder(): Retrofit.Builder {
        val builder = Retrofit.Builder()
            .baseUrl(base_url)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        val okHttpClientBuilder = createOkHttpClient()
        if (okHttpClientBuilder != null)
            builder.client(okHttpClientBuilder.build())
//        builder.addConverterFactory(
//            Json.nonstrict.asConverterFactory(
//                MediaTypeConstants.JSON.toMediaType()
//            )
//        )
        builder.addConverterFactory(GsonConverterFactory.create())
        return builder
    }

    override fun createGson(): Gson? {
        val gson: Gson
        gson = if (Build.VERSION.SDK_INT >= 23) {
            val gsonBuilder = GsonBuilder()
                .excludeFieldsWithModifiers(
                    Modifier.FINAL,
                    Modifier.TRANSIENT, Modifier.STATIC
                )
            gsonBuilder.create()
        } else {
            Gson()
        }
        return gson
    }

    override fun createOkHttpClient(): OkHttpClient.Builder? {
        val builder = OkHttpClient.Builder()
            .connectTimeout(getConnectTimeout().toLong(), TimeUnit.SECONDS)
            .readTimeout(getReadTimeout().toLong(), TimeUnit.SECONDS)
            .writeTimeout(getWriteTimeout().toLong(), TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
        val httpLoggingInterceptor = createHttpLoggingInterceptor()
        val downloadInterceptor = createInterceptor()
        val headerInterceptor = createHeaderInterceptor()
        val otherInterceptorArray = createInterceptors()
        if (httpLoggingInterceptor != null)
            builder.addInterceptor(httpLoggingInterceptor)
        if (downloadInterceptor != null)
            builder.addInterceptor(downloadInterceptor)
        if (headerInterceptor != null)
            builder.addInterceptor(headerInterceptor)
        if (otherInterceptorArray != null && otherInterceptorArray.isNotEmpty()) {
            for (interceptor in otherInterceptorArray) {
                builder.addInterceptor(interceptor)
            }
        }
        return builder
    }

    override fun createHttpLoggingInterceptor(): Interceptor? {
        val httpLoggingInterceptor =
            HttpLoggingInterceptor(object :
                HttpLoggingInterceptor.Logger {
                override fun log(message: String) {
                    Log.d(TAG, message)
                }
            })
        //Debug环境下才打印日志
        httpLoggingInterceptor.level =
            if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
        return httpLoggingInterceptor
    }

    override fun createInterceptor(): Interceptor? {
        return null
    }

    override fun createHeaderInterceptor(): Interceptor? {
        return null
    }

    override fun createInterceptors(): Array<Interceptor>? {
        return null
    }

    override fun getProgressListener(): ProgressListener? {
        return null
    }

    override fun getConnectTimeout(): Int {
        return DEFAULT_CONNECTTIMEOUT_TIME
    }

    override fun getReadTimeout(): Int {
        return DEFAULT_READTIMEOUT_TIME
    }

    override fun getWriteTimeout(): Int {
        return DEFAULT_WRITETIMEOUT_TIME
    }
}