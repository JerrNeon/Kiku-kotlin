package com.jn.kikukt.net

import com.jn.kikukt.net.retrofit.body.RetrofitBodyHelp
import com.jn.kikukt.net.retrofit.callback.ProgressListener
import com.jn.kikukt.net.retrofit.manager.RetrofitManagerFactory
import io.reactivex.rxjava3.core.Observable
import okhttp3.ResponseBody
import retrofit2.Retrofit
import java.io.File

/**
 * Author：Stevie.Chen Time：2019/7/15
 * Class Comment：
 */
class RetrofitManage {

    private var mRetrofit: Retrofit? = null
    private var mBaseUrl: String? = null

    companion object {
        val instance: RetrofitManage by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            RetrofitManage()
        }
    }

    /**
     * 初始化Retrofit
     *
     *
     * 请在Application中使用
     *
     * @param BASE_URL 服务器域名地址
     */
    fun initRetrofit(BASE_URL: String) {
        mBaseUrl = BASE_URL
    }

    /**
     * 创建请求服务
     *
     *
     * 用于普通请求
     *
     *
     * @param service
     * @param <T>
     * @return
    </T> */
    fun <T> create(service: Class<T>): T {
        if (mBaseUrl == null)
            throw NullPointerException("BaseUrl is empty,please initRetrofit in Application")
        if (mRetrofit == null) {
            val iRetrofitManage =
                RetrofitManagerFactory.create(RetrofitManagerFactory.REQUEST, mBaseUrl!!, null)
            mRetrofit = iRetrofitManage?.createRetrofit()
        }
        return mRetrofit!!.create(service)
    }

    /**
     * 创建请求服务
     *
     *
     * 用于下载
     *
     *
     * @param service
     * @param <T>
     * @param listener 进度监听器
     * @return
    </T> */
    fun <T> createDownload(service: Class<T>, listener: ProgressListener): T {
        if (mBaseUrl == null)
            throw NullPointerException("BaseUrl is empty,please initRetrofit in Application")
        val iRetrofitManage =
            RetrofitManagerFactory.create(RetrofitManagerFactory.DOWNLOAD, mBaseUrl!!, listener)
        val retrofit = iRetrofitManage?.createRetrofit()
        return retrofit!!.create(service)
    }

    /**
     * 创建请求服务
     *
     *
     * 用于上传
     *
     *
     * @param service
     * @param <T>
     * @param listener 进度监听器
     * @return
    </T> */
    fun <T> createUpload(service: Class<T>, listener: ProgressListener): T {
        if (mBaseUrl == null)
            throw NullPointerException("BaseUrl is empty,please initRetrofit in Application")
        val iRetrofitManage =
            RetrofitManagerFactory.create(RetrofitManagerFactory.UPLOAD, mBaseUrl!!, listener)
        val retrofit = iRetrofitManage?.createRetrofit()
        return retrofit!!.create(service)
    }

    /**
     * 获取下载文件的Observable
     *
     * @param url      下载地址
     * @param listener 进度监听器
     * @return
     */
    fun getDownloadObservable(url: String, listener: ProgressListener): Observable<ResponseBody> {
        val apiService = createDownload(RetrofitApiService::class.java, listener)
        return apiService.downloadFile(url)
    }

    /**
     * 获取上传多张图片的Observable
     *
     * @param url
     * @param fileParams
     * @param params
     * @return
     */
    fun getUploadObservable(
        url: String,
        fileParams: Map<String, File>,
        params: Map<String, String>? = null
    ): Observable<ResponseBody> {
        val requestBody = RetrofitBodyHelp.getFileUploadRequestBody(fileParams, params)
        val apiService = create(RetrofitApiService::class.java)
        return apiService.uploadFile(url, requestBody)
    }
}