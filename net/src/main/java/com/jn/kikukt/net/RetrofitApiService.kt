package com.jn.kikukt.net

import io.reactivex.rxjava3.core.Observable
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.http.*

/**
 * Author：Stevie.Chen Time：2019/7/15
 * Class Comment：常用请求
 */
interface RetrofitApiService {

    /**
     * 上传文件
     *
     * @param url         地址
     * @param requestBody 请求体
     * @return
     */
    @POST
    fun uploadFile(@Url url: String, @Body requestBody: RequestBody): Observable<ResponseBody>

    /**
     * 下载文件(带下载进度监听)
     *
     * @param url
     * @return
     */
    @Streaming
    @GET
    fun downloadFile(@Url url: String): Observable<ResponseBody>

    /**
     * 上传文件
     * <P>
     *    协程
     * </p>
     *
     * @param url         地址
     * @param requestBody 请求体
     * @return
     */
    @POST
    suspend fun upload(@Url url: String, @Body requestBody: RequestBody): ResponseBody

    /**
     * 下载文件(带下载进度监听)
     * <P>
     *    协程
     * </p>
     * @param url
     * @return
     */
    @Streaming
    @GET
    suspend fun download(@Url url: String): ResponseBody
}