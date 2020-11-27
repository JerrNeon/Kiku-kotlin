package com.jn.kikukt.net.retrofit

import com.jn.kikukt.net.retrofit.exception.OkHttpException

/**
 * Author：Stevie.Chen Time：2020/1/16
 * Class Comment：
 */
//密封类结合when让可能情况都是已知的,代码维护性更高。
sealed class HttpResponse

data class Start(val start: Any? = null) : HttpResponse()
data class Success<out T>(val data: T) : HttpResponse()
data class Success2<out K, out V>(val data1: K, val data2: V) : HttpResponse()
data class Success3<out K, out V, out E>(val data1: K, val data2: V, val data3: E) : HttpResponse()
data class Failure(val error: OkHttpException) : HttpResponse()
data class Failure2<out T>(val error: OkHttpException, val data: T) : HttpResponse()
data class Complete(val complete: Any? = null) : HttpResponse()


interface BaseHttpResult<out T> {
    val rCode: Int
    val rMessage: String?
    val rData: T
}

object HttpStatus {

    /**
     * Success
     */
    const val CODE_200 = "200"

    /**
     * Created
     */
    const val CODE_201 = "201"

    /**
     * Failure
     */
    const val CODE_400 = "400"

    /**
     * Unauthorized
     */
    const val CODE_401 = "401"

    /**
     * Forbidden
     */
    const val CODE_403 = "403"

    /**
     * Not Found
     */
    const val CODE_404 = "404"
}