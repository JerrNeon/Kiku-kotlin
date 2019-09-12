package com.jn.examplekotlin.entiy

/**
 * Author：Stevie.Chen Time：2019/9/12
 * Class Comment：
 */
data class XaResult<out T>(
    val code: Int,
    val message: String?,
    val result: T
)