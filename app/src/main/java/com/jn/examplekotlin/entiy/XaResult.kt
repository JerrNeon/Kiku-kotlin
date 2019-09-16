package com.jn.examplekotlin.entiy

import kotlinx.serialization.Serializable

/**
 * Author：Stevie.Chen Time：2019/9/12
 * Class Comment：
 */
@Serializable
data class XaResult<out T>(
    val code: Int,
    val message: String?,
    val result: T
)