package com.jn.kikukt.net.coroutines.manager

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
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Modifier
import java.util.concurrent.TimeUnit

/**
 * Author：Stevie.Chen Time：2020/1/15
 * Class Comment：
 */
interface IRetrofitManager {

    val TAG: String
        get() = Retrofit::class.java.simpleName

    /**
     * create service
     */
    fun <T> create(service: Class<T>, baseUrl: String): T {
        return retrofitBuilder
            .baseUrl(baseUrl)
            .build()
            .create(service)
    }

    /**
     * Retrofit配置
     */
    val retrofitBuilder: Retrofit.Builder
        get() = Retrofit.Builder()
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())

    /**
     * Gson配置
     */
    val gson: Gson
        get() = if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP)
            GsonBuilder().excludeFieldsWithModifiers(
                Modifier.FINAL,
                Modifier.TRANSIENT, Modifier.STATIC
            ).create() else Gson()

    /**
     * OkHttpClient
     */
    val okHttpClient: OkHttpClient
        get() = okHttpClientBuilder.build()

    /**
     * OkHttpClient配置
     */
    val okHttpClientBuilder: OkHttpClient.Builder
        get() {
            val client = OkHttpClient.Builder()
                .connectTimeout(connectTimeout, TimeUnit.SECONDS)
                .readTimeout(readTimeout, TimeUnit.SECONDS)
                .writeTimeout(readTimeout, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .addInterceptor(httpLoggingInterceptor)
            interceptor?.let {
                client.addInterceptor(it)
            }
            headerInterceptor?.let {
                client.addInterceptor(it)
            }
            interceptors?.let {
                it.forEach { interceptor ->
                    client.addInterceptor(interceptor)
                }
            }
            return client
        }

    /**
     * OkHttp请求响应日志拦截器
     * <P>Debug环境下才打印日志</p>
     */
    val httpLoggingInterceptor: Interceptor
        get() = HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
            override fun log(message: String) {
                Log.d(TAG, message)
            }
        }).setLevel(if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE)

    /**
     * OkHttp拦截器
     */
    val interceptor: Interceptor?
        get() = null

    /**
     * OkHttp请求头拦截器
     */
    val headerInterceptor: Interceptor?
        get() = null

    /**
     * 其它OkHttp拦截器
     */
    val interceptors: Array<Interceptor>?
        get() = null

    /**
     * 进度监听(一般用于上传或下载)
     */
    val progressListener: ProgressListener?
        get() = null

    /**
     * 连接超时时间(单位：秒)，默认10
     */
    val connectTimeout: Long
        get() = 10

    /**
     * 读取超时时间(单位：秒)，默认10
     */
    val readTimeout: Long
        get() = 10

    /**
     * 写入超时时间(单位：秒)，默认10
     */
    val writeTimeout: Long
        get() = 10
}