package com.jn.kikukt.net.rxjava

import io.reactivex.rxjava3.core.Observable
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.http.*

/**
 * Author：Stevie.Chen Time：2020/7/31
 * Class Comment：
 */
interface RxJavaApiService {

    /**
     * 上传文件
     *
     * @param url         地址
     * @param requestBody 请求体
     * @return
     */
    @POST
    fun upload(@Url url: String, @Body requestBody: RequestBody): Observable<ResponseBody>

    /**
     * 下载文件(带下载进度监听)
     *
     * @param url
     * @return
     */
    @Streaming
    @GET
    fun download(@Url url: String): Observable<ResponseBody>
}