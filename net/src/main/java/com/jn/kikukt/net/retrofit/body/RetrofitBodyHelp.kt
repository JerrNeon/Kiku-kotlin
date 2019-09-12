package com.jn.kikukt.net.retrofit.body

import com.jn.kikukt.common.utils.gson.JsonUtils
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

/**
 * Author：Stevie.Chen Time：2019/7/15
 * Class Comment：Retrofit请求响应体帮助类
 */
object RetrofitBodyHelp {

    /**
     * 获取上传单张图片并带有额外参数的RequestBody
     *
     * @param fileKey 文件Key参数信息
     * @param file    文件参数信息
     * @return RequestBody
     */
    fun getFileUploadRequestBody(fileKey: String, file: File): MultipartBody.Part {
        val requestBody = RequestBody.create(MultipartBody.FORM, file)
        return MultipartBody.Part.createFormData(fileKey, file.name, requestBody)
    }

    /**
     * 获取上传多张图片并带有额外参数的RequestBody
     *
     * @param fileParams 文件参数信息
     * @param params     额外参数信息
     * @return RequestBody
     */
    fun getFileUploadRequestBody(fileParams: Map<String, File>, params: Map<String, String>?): RequestBody {
        val build = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
        if (fileParams.isNotEmpty()) {
            for ((key, value) in fileParams) {
                val requestBody = RequestBody.create(MultipartBody.FORM, value)
                build.addFormDataPart(key, value.name, requestBody)
            }
        }
        if (params != null && params.isNotEmpty()) {
            for ((key, value) in params) {
                build.addFormDataPart(key, value)
            }
        }
        return build.build()
    }

    /**
     * 获取Json类型的RequestBody
     *
     * @param object 请求参数对象
     * @return RequestBody
     */
    fun getJsonRequestBody(`object`: Any?): RequestBody? {
        return if (`object` != null) RequestBody.create(
            MediaType.parse(MediaTypeConstants.JSON),
            JsonUtils.instance.toJson(`object`)!!
        ) else null
    }
}