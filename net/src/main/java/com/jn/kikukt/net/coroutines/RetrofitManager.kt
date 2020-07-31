package com.jn.kikukt.net.coroutines

import com.jn.kikukt.common.utils.getDomain
import com.jn.kikukt.net.retrofit.body.RetrofitBodyHelp
import com.jn.kikukt.net.retrofit.callback.ProgressListener
import com.jn.kikukt.net.rxjava.manager.RetrofitDownloadManager
import com.jn.kikukt.net.rxjava.manager.RetrofitUploadManager
import okhttp3.ResponseBody
import java.io.File

/**
 * Author：Stevie.Chen Time：2020/7/31
 * Class Comment：
 */
object RetrofitManager {

    /**
     * 下载
     *
     * @param url      下载地址
     * @param onProgress 进度监听器
     * @return
     */
    suspend fun download(
        url: String,
        onProgress: ((progressBytes: Long, totalBytes: Long, progressPercent: Float, done: Boolean) -> Unit)? = null
    ): ResponseBody {
        val retrofit = RetrofitDownloadManager(url.getDomain(), object : ProgressListener {
            override fun onProgress(
                progressBytes: Long,
                totalBytes: Long,
                progressPercent: Float,
                done: Boolean
            ) {
                onProgress?.invoke(progressBytes, totalBytes, progressPercent, done)
            }
        }).createRetrofit()
        val apiService = retrofit.create(CoroutinesApiService::class.java)
        return apiService.download(url)
    }

    /**
     * 上传
     *
     * @param url 上传地址
     * @param fileParams 文件参数
     * @param params 额外参数
     * @param onProgress 进度监听器
     * @return
     */
    suspend fun upload(
        url: String,
        fileParams: Map<String, File>,
        params: Map<String, String>? = null,
        onProgress: ((progressBytes: Long, totalBytes: Long, progressPercent: Float, done: Boolean) -> Unit)? = null
    ): ResponseBody {
        val retrofit = RetrofitUploadManager(url.getDomain(), object : ProgressListener {
            override fun onProgress(
                progressBytes: Long,
                totalBytes: Long,
                progressPercent: Float,
                done: Boolean
            ) {
                onProgress?.invoke(progressBytes, totalBytes, progressPercent, done)
            }
        }).createRetrofit()
        val requestBody = RetrofitBodyHelp.getFileUploadRequestBody(fileParams, params)
        val apiService = retrofit.create(CoroutinesApiService::class.java)
        return apiService.upload(url, requestBody)
    }
}