package com.jn.kikukt.net.retrofit.body

import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
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
        val requestBody = file.asRequestBody(MultipartBody.FORM)
        return MultipartBody.Part.createFormData(fileKey, file.name, requestBody)
    }

    /**
     * 获取上传多张图片并带有额外参数的RequestBody
     *
     * @param fileParams 文件参数信息
     * @param params     额外参数信息
     * @return RequestBody
     */
    fun getFileUploadRequestBody(
        fileParams: Map<String, File>,
        params: Map<String, String>?
    ): RequestBody {
        val build = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
        if (fileParams.isNotEmpty()) {
            for ((key, value) in fileParams) {
                val requestBody = value.asRequestBody(MultipartBody.FORM)
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
     * @param map 请求参数对象
     * @return RequestBody
     */
    fun getJsonRequestBody(map: MutableMap<String, String?>): RequestBody? {
        return Json.encodeToString(serializer(), map)
            .toRequestBody(MediaTypeConstants.JSON.toMediaTypeOrNull())
    }
}