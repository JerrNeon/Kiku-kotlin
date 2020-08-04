package com.jn.examplekotlin.entiy

import com.jn.kikukt.net.retrofit.BaseHttpResult
import kotlinx.serialization.Serializable

/**
 * Author：Stevie.Chen Time：2019/9/12
 * Class Comment：
 */
@Serializable
data class ApiResult<out T>(
    val code: Int,
    val message: String?,
    val result: T
) : BaseHttpResult<T> {
    override val rCode: Int
        get() = code
    override val rMessage: String?
        get() = message
    override val rData: T
        get() = result
}