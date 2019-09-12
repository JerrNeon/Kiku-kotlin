package com.jn.kikukt.common.utils

import com.google.gson.internal.`$Gson$Types`
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 * Author：Stevie.Chen Time：2019/7/15
 * Class Comment：反射工具类
 */
object ReflectUtils {

    /**
     * 获取泛型类型
     *
     * @param subclass
     * @return
     */
    fun getSuperclassTypeParameter(subclass: Class<*>): Type? {
        val superclass = subclass.genericSuperclass
        if (superclass is Class<*>) {
            throw RuntimeException("Missing type parameter.")
        }
        val parameterized = superclass as ParameterizedType
        return `$Gson$Types`.canonicalize(
            parameterized
                .actualTypeArguments[0]
        )
    }

}