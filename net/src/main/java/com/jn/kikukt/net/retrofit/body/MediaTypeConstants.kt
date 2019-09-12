package com.jn.kikukt.net.retrofit.body

/**
 * Author：Stevie.Chen Time：2019/7/15
 * Class Comment：MediaType常量
 */
class MediaTypeConstants {

    companion object {
        /**
         * 数据是个普通表单
         */
        const val FORM_URLENCODED = "application/x-www-form-urlencoded"

        /**
         * 数据里有文件
         */
        const val FORM_DATA = "multipart/form-data"

        /**
         * 数据是个json
         */
        const val JSON = "application/json; charset=utf-8"

        /**
         * 上传的是图片类型
         */
        const val IMAGE = "image/*"
    }
}