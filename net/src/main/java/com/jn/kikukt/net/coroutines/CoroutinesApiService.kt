package com.jn.kikukt.net.coroutines

import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.http.*

/**
 * Author：Stevie.Chen Time：2020/7/31
 * Class Comment：
 */
interface CoroutinesApiService {

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