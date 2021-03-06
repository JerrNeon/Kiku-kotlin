package com.jn.kikukt.common.utils

import java.util.*

/**
 * Author：Stevie.Chen Time：2019/7/12
 * Class Comment：
 */
object StringUtils {

    /**
     * 判断两个字符串是否相同(顺序一定是一样的)
     */
    fun isIncludeAllChar(str1: String, str2: String): Boolean {
        var flag = true
        if (str1.length != str2.length) {
            flag = false
        } else {
            if (str1 != str2)
                flag = false
        }
        return flag
    }

    /**
     * 判断两个字符串是否相同(不管顺序是否一样)
     */
    fun isIncludeSameChar(str1: String, str2: String): Boolean {
        var flag = true
        if (str1.length != str2.length) {
            flag = false
        } else {
            val str1Arr = str1.toCharArray()
            Arrays.sort(str1Arr)
            val str2Arr = str2.toCharArray()
            Arrays.sort(str2Arr)
            for (i in str2Arr.indices) {
                if (str2Arr[i] == str1Arr[i]) {
                    continue
                } else {
                    flag = false
                }
            }
        }
        return flag
    }
}

/**
 * 获取域名
 * return "http://www.xx.com/"
 */
fun String.getDomain(): String {
    return if (startsWith("http") || startsWith("Http")) {
        val index = indexOf("//")
        val httpStr = substring(0, index + 2)
        val urlStr = substring(index + 2, length)//不包含http://
        httpStr + urlStr.substring(0, urlStr.indexOf("/") + 1)
    } else ""
}