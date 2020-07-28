package com.jn.kikukt.common.utils

import java.lang.reflect.ParameterizedType

/**
 * Author：Stevie.Chen Time：2020/1/17
 * Class Comment：
 */
object Clazz {

    @Suppress("UNCHECKED_CAST")
    fun <T> getClass(t: Any, argumentsPosition: Int = 0): Class<T> {
        // 通过反射 获取父类泛型 (T) 对应 Class类
        return (t.javaClass.genericSuperclass as ParameterizedType)
            .actualTypeArguments[argumentsPosition]
                as Class<T>
    }
}