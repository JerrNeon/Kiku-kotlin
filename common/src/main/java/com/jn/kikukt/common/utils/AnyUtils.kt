package com.jn.kikukt.common.utils

/**
 * Author：Stevie.Chen Time：2020/1/16
 * Class Comment：
 */
inline fun <reified T> Any.cast(): T? {
    return this as? T
}