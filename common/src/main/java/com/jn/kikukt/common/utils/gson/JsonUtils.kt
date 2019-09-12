package com.jn.kikukt.common.utils.gson

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonSyntaxException
import org.json.JSONObject
import org.json.JSONTokener
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.reflect.Type

/**
 * Author：Stevie.Chen Time：2019/7/15
 * Class Comment：
 */
class JsonUtils {

    private var mGson: Gson? = null

    init {
        if (mGson == null) {
            mGson = GsonBuilder().create()
        }
    }

    companion object {

        val instance: JsonUtils by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            JsonUtils()
        }
    }

    /**
     * 将Java对象转换成json字符串
     *
     * @param object
     * @return
     */
    fun <T> toJson(`object`: T): String? {
        return mGson?.toJson(`object`)
    }

    /**
     * 将Java对象转换成json字符串
     * 上传头像时Base64字符串转成json时=会转成\u003d，设置disableHtmlEscaping就不会转
     *
     * @param object
     * @return
     */
    fun <T> toJsonDisableHtml(`object`: T): String {
        val gson = GsonBuilder().disableHtmlEscaping().create()
        return gson.toJson(`object`)
    }

    /**
     * 将json字符串转成JavaBean对象
     *
     * @param jsonStr
     * @return
     */
    fun <T> toObject(jsonStr: String, tClass: Class<T>): T? {
        try {
            return mGson?.fromJson(jsonStr, tClass)
        } catch (e: JsonSyntaxException) {
            e.printStackTrace()
        }
        return null
    }

    /**
     * 将json字符串转成List集合
     *
     * @param jsonStr
     * @param type  new TypeToken<List></List><T>>() {}.getType());
     * @return
    </T> */
    fun <T> toObject(jsonStr: String, type: Type): List<T>? {
        try {
            return mGson?.fromJson(jsonStr, type)
        } catch (e: JsonSyntaxException) {
            e.printStackTrace()
        }
        return null
    }

    /**
     * 将json字符串转成Map集合(无序的)
     *
     * @param jsonStr
     * @param type
     * @return
     */
    fun <T, V> toMap(jsonStr: String, type: Type): Map<T, V>? {
        try {
            return mGson?.fromJson(jsonStr, type)
        } catch (e: JsonSyntaxException) {
            e.printStackTrace()
        }
        return null
    }

    /**
     * json转List
     *
     * @param jsonStr json字符串
     * @param clazz   类型类对象
     * @param <T>     类型
     * @return
    </T> */
    fun <T> toList(jsonStr: String, clazz: Class<*>): List<T>? {
        try {
            val type = ParameterizedTypeListIml(clazz)
            return mGson?.fromJson(jsonStr, type)
        } catch (e: JsonSyntaxException) {
            e.printStackTrace()
        }
        return null
    }

    /**
     * 根据json字符串和key获取相应的数据
     *
     * @param json json字符串
     * @param name key
     * @return
     */
    fun getObjectForName(json: Any?, name: String): String? {
        var result: String? = null
        if (json != null) {
            try {
                var jsonParser: JSONTokener? = JSONTokener(json.toString())
                var status: JSONObject? = jsonParser!!.nextValue() as JSONObject
                val obj = status!!.get(name)
                if (obj != null) {
                    result = obj.toString()
                }
                jsonParser = null
                status = null
            } catch (e: Throwable) {
                result = null
            }
        }
        return result
    }

    fun readLocalJson(mContext: Context, jsonFileName: String): String {
        val builder = StringBuilder()
        try {
            val isr = InputStreamReader(mContext.assets.open("$jsonFileName.json"), "UTF-8")
            val br = BufferedReader(isr)
            var line: String? = br.readLine()
            while (line != null) {
                builder.append(line)
                line = br.readLine()
            }
            br.close()
            isr.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return builder.toString()
    }
}