package com.jn.kikukt.net.coroutines

import com.jn.kikukt.net.retrofit.exception.OkHttpException

/**
 * Author：Stevie.Chen Time：2020/1/16
 * Class Comment：
 */
//密封类结合when让可能情况都是已知的,代码维护性更高。
sealed class HttpResponse

data class Start(val start: Any? = null) : HttpResponse()
data class Success<out T>(val data: T) : HttpResponse()
data class Failure(val error: OkHttpException) : HttpResponse()
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