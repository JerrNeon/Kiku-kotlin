package com.jn.kikukt.net

import com.jn.kikukt.common.utils.getDomain
import com.jn.kikukt.net.retrofit.body.RetrofitBodyHelp
import com.jn.kikukt.net.retrofit.callback.ProgressListener
import com.jn.kikukt.net.retrofit.manager.RetrofitDownloadManager
import com.jn.kikukt.net.retrofit.manager.RetrofitUploadManager
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
    fun getDownloadObservable(url: String, listener: ProgressListener?): Observable<ResponseBody> {
        val retrofit = RetrofitDownloadManager(url.getDomain(), listener).createRetrofit()
        val apiService = retrofit.create(RetrofitApiService::class.java)
        return apiService.downloadFile(url)
    }

    /**
     * 获取上传多张图片的Observable
     *
     * @param url
     * @param fileParams
     * @param params
     * @param listener 进度监听器
     * @return
     */
    fun getUploadObservable(
        url: String,
        fileParams: Map<String, File>,
        params: Map<String, String>? = null,
        listener: ProgressListener? = null
    ): Observable<ResponseBody> {
        val retrofit = RetrofitUploadManager(url.getDomain(), listener).createRetrofit()
        val requestBody = RetrofitBodyHelp.getFileUploadRequestBody(fileParams, params)
        val apiService = retrofit.create(RetrofitApiService::class.java)
        return apiService.uploadFile(url, requestBody)
    }
}