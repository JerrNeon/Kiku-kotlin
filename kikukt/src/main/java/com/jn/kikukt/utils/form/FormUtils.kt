package com.jn.kikukt.utils.form

import android.content.Context
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import java.util.regex.Pattern

/**
 * Author：Stevie.Chen Time：2019/7/15
 * Class Comment：表单验证
 */

/**
 * 验证所有表单是否为空
 *
 * @param errors    错误信息
 * @param editTexts 要验证的EditText
 * @return 有一个为空返回true，全都不为空返回false
 */
fun Context.validate(errors: Array<String>, vararg editTexts: EditText): Boolean {
    for (i in editTexts.indices) {
        val et = editTexts[i]
        if (et.text.toString().trim().isEmpty()) {
            Toast.makeText(this, errors[i] + "不能为空", Toast.LENGTH_SHORT).show()
            return true
        }
    }
    return false
}

/**
 * 验证所有表单是否为空
 *
 * @param errors    错误信息
 * @param textViews 要验证的TextView
 * @return 有一个为空返回true，全都不为空返回false
 */
fun Context.validate(errors: Array<String>, vararg textViews: TextView): Boolean {
    for (i in textViews.indices) {
        val tv = textViews[i]
        if (tv.text.toString().trim().isEmpty()) {
            Toast.makeText(this, errors[i] + "不能为空", Toast.LENGTH_SHORT).show()
            return true
        }
    }
    return false
}

/**
 * 正则表达式验证所有表单(提示信息为 "错误信息"+ "格式不正确")
 *
 * @param regexs    正则表达式
 * @param errors    错误信息
 * @param editTexts 要验证的EditText
 * @return 有一个验证失败返回true，全都都验证成功返回false
 */
fun Context.validate(regexs: Array<String>, errors: Array<String>, vararg editTexts: EditText): Boolean {
    for (i in editTexts.indices) {
        val et = editTexts[i]
        if (!Pattern.matches(regexs[i], et.text.toString().trim())) {
            Toast.makeText(this, errors[i] + "格式不正确", Toast.LENGTH_SHORT).show()
            return true
        }
    }
    return false
}

/**
 * 正则表达式验证所有表单(提示信息为 "错误信息")
 *
 * @param regexs    正则表达式
 * @param errors    错误信息
 * @param editTexts 要验证的EditText
 * @return 有一个验证失败返回true，全都都验证成功返回false
 */
fun Context.validateAll(regexs: Array<String>, errors: Array<String>, vararg editTexts: EditText): Boolean {
    for (i in editTexts.indices) {
        val et = editTexts[i]
        if (!Pattern.matches(regexs[i], et.text.toString().trim())) {
            Toast.makeText(this, errors[i], Toast.LENGTH_SHORT).show()
            return true
        }
    }
    return false
}