package com.jn.kikukt.net.rxjava.manager

import com.google.gson.Gson
import com.jn.kikukt.net.retrofit.callback.ProgressListener
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit

/**
 * Author：Stevie.Chen Time：2019/7/15
 * Class Comment：Retrofit管理
 */
interface IRetrofitManage {

    val DEFAULT_CONNECTTIMEOUT_TIME: Int //连接超时时间(单位：秒)
        get() = 10
    val DEFAULT_READTIMEOUT_TIME: Int //读取超时时间(单位：秒)
        get() = 10
    val DEFAULT_WRITETIMEOUT_TIME: Int //写入超时时间(单位：秒)
        get() = 10

    /**
     * Retrofit配置
     *
     * @return
     */
    fun createRetrofit(): Retrofit

    /**
     * Retrofit配置
     *
     * @return
     */
    fun createRetrofitBuilder(): Retrofit.Builder

    /**
     * Gson配置
     *
     * @return
     */
    fun createGson(): Gson?

    /**
     * OkHttpClient配置
     *
     * @return
     */
    fun createOkHttpClient(): OkHttpClient.Builder?

    /**
     * 创建请求响应日志拦截器
     *
     * @return
     */
    fun createHttpLoggingInterceptor(): Interceptor?

    /**
     * 创建拦截器
     *
     * @return
     */
    fun createInterceptor(): Interceptor?

    /**
     * 创建Header拦截器
     *
     * @return Interceptor
     */
    fun createHeaderInterceptor(): Interceptor?

    /**
     * 创建拦截器
     *
     */
    fun createInterceptors(): Array<Interceptor>?

    /**
     * 进度监听
     *
     * @return
     */
    fun getProgressListener(): ProgressListener?

    /**
     * 获取连接超时时间(单位：秒)
     *
     * @return
     */
    fun getConnectTimeout(): Int

    /**
     * 获取读取超时时间(单位：秒)
     *
     * @return
     */
    fun getReadTimeout(): Int

    /**
     * 获取写入超时时间(单位：秒)
     *
     * @return
     */
    fun getWriteTimeout(): Int

}