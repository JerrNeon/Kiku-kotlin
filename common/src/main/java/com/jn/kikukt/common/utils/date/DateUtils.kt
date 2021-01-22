package com.jn.kikukt.common.utils.date

import com.jn.kikukt.common.utils.date.DateUtils.YYYY_MM_DD_HH_MM_SS
import com.jn.kikukt.common.utils.date.DateUtils.YYYY_MM_DD_SHORTHAND
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Author：Stevie.Chen Time：2019/7/15
 * Class Comment：
 */
object DateUtils {
    const val YYYY_MM_DD = "yyyy-MM-dd"//短日期格式
    const val YYYY_MM = "yyyy-MM"//短日期格式
    const val YYYY_MM_DD_SHORTHAND = "yyyyMMdd"//短日期格式
    const val YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss"//长日期时间格式
    const val HH_MM_SS = "HH:mm:ss"//长时间格式
    const val HH_MM = "HH:mm"//短时间格式

    /**
     * 获取当前时间.(yyyy-MM-dd HH:mm:ss)
     */
    fun getCurrTime(pattern: String = YYYY_MM_DD_HH_MM_SS): String {
        return SimpleDateFormat(pattern, Locale.getDefault()).format(Date())
    }
}

/**
 * 格式化时间.
 */
fun String.formatDateStr(): String {
    return if (isNotEmpty() && (length == 8 || length == 14)) {
        if (length == 8) "${substring(0, 4)}-${substring(4, 6)}-${substring(6, 8)}"
        else "${substring(0, 4)}-${substring(4, 6)}-${substring(6, 8)} ${substring(
            8,
            10
        )}:${substring(10, 12)}:${substring(12, 14)}"
    } else ""
}

/**
 * 将时间字符串格式化成一个日期("2014-06-15 12:30:12")
 * @param pattern 格式化模板，如"yyyy-MM-dd HH:mm:ss"
 */
fun String.toDate(pattern: String = YYYY_MM_DD_HH_MM_SS): Date? {
    val dateFormat = SimpleDateFormat(pattern, Locale.getDefault())
    var date: Date? = null
    try {
        date = dateFormat.parse(this)
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return date
}

/**
 * 格式化时间字符串("2014-06-15 12:30:12")
 * @param pattern 格式化模板，如"yyyy-MM-dd HH:mm:ss"
 */
fun String.formatDate(pattern: String = YYYY_MM_DD_HH_MM_SS): String? {
    val dateFormat = SimpleDateFormat(pattern, Locale.getDefault())
    var dateStr = ""
    try {
        val date = dateFormat.parse(this)
        if (date != null) dateStr = dateFormat.format(date)
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return dateStr
}

/**
 * 格式化[Date]("2014-06-15 12:30:12")
 * @param pattern 格式化模板，如"yyyy-MM-dd HH:mm:ss"
 */
fun String.formatDate(date: Date, pattern: String = YYYY_MM_DD_HH_MM_SS): String? {
    val dateFormat = SimpleDateFormat(pattern, Locale.getDefault())
    var dateStr = ""
    try {
        dateStr = dateFormat.format(date)
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return dateStr
}

/**
 * 将一个毫秒数时间转换为相应的时间格式（yyyy-MM-dd hh:mm:ss）
 */
fun Long.formatDate(pattern: String = YYYY_MM_DD_HH_MM_SS): String {
    val gc = GregorianCalendar()
    gc.timeInMillis = this
    val format = SimpleDateFormat(YYYY_MM_DD_HH_MM_SS, Locale.getDefault())
    return format.format(gc.time)
}

/**
 * 将日期格式的字符串转换为长整型
 */
fun String.toDateLong(pattern: String = YYYY_MM_DD_HH_MM_SS): Long? {
    try {
        val sf = SimpleDateFormat(pattern, Locale.getDefault())
        return sf.parse(this)?.time
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    return 0L
}

/**
 * 时间取出年月日显示
 */
fun String.formatDate(
    isYear: Boolean = false,
    isMonth: Boolean = true,
    isDay: Boolean = true
): String {
    val format = SimpleDateFormat(YYYY_MM_DD_HH_MM_SS, Locale.getDefault())
    try {
        // 用parse方法，可能会异常，所以要try-catch
        val date = format.parse(this)
        // 获取日期实例
        val calendar = Calendar.getInstance()
        // 将日历设置为指定的时间
        if (date != null)
            calendar.time = date
        // 获取年
        val year = calendar.get(Calendar.YEAR)
        // 这里要注意，月份是从0开始。
        val month = calendar.get(Calendar.MONTH)
        // 获取天
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val monthStr = if (month < 9)
            "0${month + 1}"
        else
            "${month + 1}"
        val dayStr = if (day < 10)
            "0$day"
        else
            day.toString()
        return if (isYear && isMonth && isDay) "$year-$monthStr-$dayStr"
        else if (isYear && isMonth) "$year-$monthStr"
        else if (isMonth && isDay) "$monthStr-$dayStr"
        else if (isYear) "$year"
        else if (isMonth) monthStr
        else if (isDay) dayStr
        else ""
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    return ""
}

/**
 * 获取下一天.
 *
 * @return the next date str
 */
fun String.getNextDay(): String {
    val sdf = SimpleDateFormat(YYYY_MM_DD_SHORTHAND, Locale.getDefault())
    val date = sdf.parse(this)
    val calendar = Calendar.getInstance()
    if (date != null)
        calendar.time = date
    calendar.add(Calendar.DATE, 1)
    return sdf.format(calendar.time)
}

/**
 * 获取上一天.
 *
 * @return the next date str
 */
fun String.getYesterday(): String {
    val sdf = SimpleDateFormat(YYYY_MM_DD_SHORTHAND, Locale.getDefault())
    val date = sdf.parse(this)
    val calendar = Calendar.getInstance()
    if (date != null)
        calendar.time = date
    calendar.add(Calendar.DATE, -1)
    return sdf.format(calendar.time)
}

/**
 * 根据日期获取星期
 */
fun String.getWeekDayByDate(
    pattern: String = YYYY_MM_DD_HH_MM_SS,
    weekArrays: Array<String> = arrayOf("星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六")
): String {
    val sdfInput = SimpleDateFormat(pattern, Locale.getDefault())
    val calendar = Calendar.getInstance()
    var date: Date? = null
    try {
        date = sdfInput.parse(this)
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    if (date != null)
        calendar.time = date
    var dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1
    if (dayOfWeek < 0)
        dayOfWeek = 0
    return weekArrays[dayOfWeek]
}