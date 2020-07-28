package com.jn.kikukt.common.utils

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonSyntaxException
import com.jn.kikukt.common.utils.GsonUtils.INSTANCE
import org.json.JSONObject
import org.json.JSONTokener
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 * Author：Stevie.Chen Time：2020/7/16
 * Class Comment：
 */
object GsonUtils {
    val INSTANCE: Gson = GsonBuilder().create()
}

/**
 * 将对象转换成json字符串
 * 上传头像时Base64字符串转成json时=会转成\u003d，设置[disableHtmlEscaping]就不会转
 */
fun <T> T.toJson(disableHtmlEscaping: Boolean = false): String {
    return if (!disableHtmlEscaping) {
        INSTANCE.toJson(this)
    } else {
        GsonBuilder().disableHtmlEscaping().create().toJson(this)
    }
}

/**
 * 将json字符串转换成对象
 */
inline fun <reified T> String.fromJson(): T? {
    return try {
        INSTANCE.fromJson(this, T::class.java)
    } catch (e: JsonSyntaxException) {
        e.log()
        null
    }
}

/**
 * 将json字符串转换成List
 */
inline fun <reified T> String.fromJsonList(): List<T>? {
    return try {
        INSTANCE.fromJson(
            this,
            ParameterizedTypeListIml(T::class.java)
        )
    } catch (e: JsonSyntaxException) {
        e.log()
        null
    }
}

/**
 * 将json字符串转换成集合
 */
fun <T> String.fromJson(type: Type): T? {
    return try {
        INSTANCE.fromJson(this, type)
    } catch (e: JsonSyntaxException) {
        e.log()
        null
    }
}

/**
 * 根据json字符串和key获取相应的value
 */
inline fun <reified T> String.getJsonValue(key: String): T? {
    return try {
        val jsonToken = JSONTokener(this)
        val jsonObject = jsonToken.nextValue() as? JSONObject
        val obj = jsonObject?.get(key)
        obj as? T
    } catch (e: JsonSyntaxException) {
        e.log()
        null
    } catch (e: Throwable) {
        e.log()
        null
    }
}

/**
 * 读取本地asset文件夹中的json文件
 */
fun Context.getAssetJson(jsonFileName: String): String? {
    return try {
        val builder = StringBuilder()
        val isr = InputStreamReader(assets.open("$jsonFileName.json"), "UTF-8")
        val br = BufferedReader(isr)
        var line: String? = br.readLine()
        while (line != null) {
            builder.append(line)
            line = br.readLine()
        }
        br.close()
        isr.close()
        builder.toString()
    } catch (e: JsonSyntaxException) {
        e.log()
        null
    } catch (e: Throwable) {
        e.log()
        null
    }
}

/**
 * Json字符串转List关键类
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