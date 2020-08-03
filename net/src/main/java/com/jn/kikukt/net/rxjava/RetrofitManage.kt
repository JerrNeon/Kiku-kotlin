package com.jn.kikukt.net.rxjava

import com.jn.kikukt.common.utils.getDomain
import com.jn.kikukt.net.retrofit.body.RetrofitBodyHelp
import com.jn.kikukt.net.retrofit.callback.ProgressListener
import com.jn.kikukt.net.rxjava.manager.RetrofitDownloadManager
import com.jn.kikukt.net.rxjava.manager.RetrofitUploadManager
import io.reactivex.rxjava3.core.Observable
import okhttp3.ResponseBody
import java.io.File

/**
 * Author：Stevie.Chen Time：2019/7/15
 * Class Comment：
 */
object RetrofitManage {

    /**
     * 获取下载文件的Observable
     *
     * @param url      下载地址
     * @param listener 进度监听器
     * @return
     */
    fun download(url: String, listener: ProgressListener?): Observable<ResponseBody> {
        val retrofit = RetrofitDownloadManager(listener)
        val apiService = retrofit.create(RxJavaApiService::class.java, url.getDomain())
        return apiService.download(url)
    }

    /**
     * 获取上传文件的Observable
     *
     * @param url 上传地址
     * @param fileParams 文件参数
     * @param params 额外参数
     * @param listener 进度监听器
     * @return
     */
    fun upload(
        url: String,
        fileParams: Map<String, File>,
        params: Map<String, String>? = null,
        listener: ProgressListener? = null
    ): Observable<ResponseBody> {
        val retrofit = RetrofitUploadManager(listener)
        val requestBody = RetrofitBodyHelp.getFileUploadRequestBody(fileParams, params)
        val apiService = retrofit.create(RxJavaApiService::class.java, url.getDomain())
        return apiService.upload(url, requestBody)
    }
}