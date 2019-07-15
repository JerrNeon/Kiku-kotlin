package com.jn.kikukt.retrofit.manager

import android.support.annotation.IntDef
import com.jn.kikukt.retrofit.callback.ProgressListener

/**
 * Author：Stevie.Chen Time：2019/7/15
 * Class Comment：Retrofit请求管理工厂
 */
class RetrofitManagerFactory {

    companion object {
        const val REQUEST = 1//普通请求
        const val DOWNLOAD = 2//下载
        const val UPLOAD = 3//上传

        fun create(@RequestType requestType: Int, BASE_URL: String, listener: ProgressListener?): IRetrofitManage? {
            var retrofitManage: IRetrofitManage? = null
            when (requestType) {
                REQUEST -> retrofitManage = RetrofitRequestManager(BASE_URL)
                DOWNLOAD -> retrofitManage = RetrofitDownloadManager(BASE_URL, listener)
                UPLOAD -> retrofitManage = RetrofitUploadManager(BASE_URL, listener)
                else -> {
                }
            }
            return retrofitManage
        }
    }

    @IntDef(REQUEST, DOWNLOAD, UPLOAD)
    @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
    internal annotation class RequestType
}