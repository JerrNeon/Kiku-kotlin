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