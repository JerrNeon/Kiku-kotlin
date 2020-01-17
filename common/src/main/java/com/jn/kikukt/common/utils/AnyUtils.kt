package com.jn.kikukt.common.utils

/**
 * Author：Stevie.Chen Time：2020/1/16
 * Class Comment：
 */
@Suppress("UNCHECKED_CAST")
fun <T> Any.cast(): T? {
    return this as? T
}