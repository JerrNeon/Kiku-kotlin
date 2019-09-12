package com.jn.kikukt.common.utils.form

import java.util.regex.Pattern

/**
 * Author：Stevie.Chen Time：2019/7/15
 * Class Comment：正则表达式验证
 */

/**
 * 匹配正则
 *
 * @param regex 正则表达式
 * @param str   字符串
 * @return
 */
fun String.check(regex: String): Boolean {
    return Pattern.matches(regex, this)
}

/**
 * 验证手机号
 *
 * @param str 字符串
 * @return
 */
fun String.checkMobile(): Boolean {
    return Pattern.matches(RegexConstants.regexMobile, this)
}