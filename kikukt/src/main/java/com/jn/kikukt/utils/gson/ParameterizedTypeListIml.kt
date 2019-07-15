package com.jn.kikukt.utils.gson

import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 * Author：Stevie.Chen Time：2019/7/15
 * Class Comment：Json字符串转List关键类
 */
class ParameterizedTypeListIml(private val clazz: Class<*>) : ParameterizedType {

    override fun getRawType(): Type {
        return List::class.java//返回原生类型
    }

    override fun getOwnerType(): Type? {
        return null//返回 Type 对象，表示此类型是其成员之一的类型;如果此类型为 O<T>.I<S>，则返回 O<T> 的表示形式。 如果此类型为顶层类型，则返回 null。这里就直接返回null就行了。
    }

    override fun getActualTypeArguments(): Array<Type> {
        return arrayOf(clazz)//返回实际类型组成的数据
    }
}