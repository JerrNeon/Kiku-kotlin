package com.jn.kikukt.common.utils.date

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Author：Stevie.Chen Time：2019/7/15
 * Class Comment：
 */
object DateUtils {

    private const val LONG_DATE_FORMAT = "yyyy-MM-dd"//长日期格式
    private const val SMALL_DATE_FORMAT = "yyyy-MM"//短日期格式
    private const val LONG_DATE_FORMAT2 = "yyyyMMdd"//长日期格式
    private const val DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss"//长日期时间格式
    private const val LONG_TIME_FORMAT = "HH:mm:ss"//长时间格式
    private const val SMALL_TIME_FORMAT = "HH:mm"//短时间格式

    /**
     * 获取当前时间.(yyyy-MM-dd HH:mm:ss)
     */
    fun getToDayStr(): String {
        val sdf = SimpleDateFormat(DATE_TIME_FORMAT, Locale.getDefault())
        return sdf.format(Date())
    }

    /**
     * 获取当前时间.(yyyy-MM-dd)
     */
    fun getToDayStrSmall(): String {
        val sdf = SimpleDateFormat(LONG_DATE_FORMAT, Locale.getDefault())
        return sdf.format(Date())
    }

    /**
     * 格式化时间.
     */
    fun fomatDate(date: String): String {
        return if (date.isNotEmpty()) {
            (date.substring(0, 4) + "-" + date.substring(4, 6) + "-"
                    + date.substring(6, 8))
        } else ""
    }

    /**
     * 格式化时间.
     *
     * @param date the date
     * @return the string
     */
    fun fomatLongDate(date: String): String {
        return if (date.isNotEmpty()) {
            (date.substring(0, 4) + "-" + date.substring(4, 6) + "-"
                    + date.substring(6, 8) + " " + date.substring(8, 10) + ":"
                    + date.substring(10, 12) + ":" + date.substring(12, 14))
        } else ""
    }

    /**
     * 格式化时间.
     */
    fun fomatDateTime2String(date: String): String {
        return if (date.isNotEmpty()) {
            date.replace("-", "").replace("T", "").replace(":", "")
                .replace(" ", "")
        } else ""
    }

    /**
     * 将时间字符串格式化成一个日期(java.util.Date)
     *
     * @param dateStr   要格式化的日期字符串，如"2014-06-15 12:30:12"
     * @param formatStr 格式化模板，如"yyyy-MM-dd HH:mm:ss"
     * @return the string
     */
    fun formatDateString2Date(dateStr: String, formatStr: String): Date? {
        val dateFormat = SimpleDateFormat(formatStr, Locale.getDefault())
        var date: Date? = null
        try {
            date = dateFormat.parse(dateStr)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return date
    }

    /**
     * 将时间字符串格式化成一个日期(java.util.Date)
     *
     * @param date      要格式化的日期字符串，如"2014-06-15 12:30:12"
     * @param formatStr 格式化模板，如"yyyy-MM-dd HH:mm:ss"
     * @return the string
     */
    fun formatDate2String(date: Date, formatStr: String): String? {
        val dateFormat = SimpleDateFormat(formatStr, Locale.getDefault())
        var result: String? = null
        try {
            result = dateFormat.format(date)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return result
    }

    /**
     * 将时间字符串规范化
     *
     * @param dateStr   要格式化的日期字符串，如"2014-06-15 12:30:12.0"
     * @param formatStr 格式化模板，如"yyyy-MM-dd HH:mm:ss"
     * @return the string
     */
    fun formatDateString2String(dateStr: String, formatStr: String): String? {
        val dateFormat = SimpleDateFormat(formatStr, Locale.getDefault())
        var result: String? = null
        try {
            result = dateFormat.format(dateFormat.parse(dateStr))
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return result
    }

    /**
     * 将一个毫秒数时间转换为相应的时间格式（yyyy-MM-dd hh:mm:ss）
     */
    fun formateDateLongToString(longSecond: Long): String {
        val gc = GregorianCalendar()
        gc.timeInMillis = longSecond
        val format = SimpleDateFormat(DATE_TIME_FORMAT, Locale.getDefault())
        return format.format(gc.time)
    }

    /**
     * 将一个毫秒数时间转换为相应的时间格式（yyyy-MM-dd hh:mm:ss）
     */
    fun formateDateLongToString2(longSecond: Long): String {
        val format = SimpleDateFormat(DATE_TIME_FORMAT, Locale.getDefault())
        return format.format(longSecond * 1000)
    }


    /**
     * 将一个毫秒数时间转换为相应的时间格式（yyyy-MM-dd）
     *
     * @param longSecond
     * @return
     */
    fun formateDateLongToStringSmall(longSecond: Long): String {
        val gc = GregorianCalendar()
        gc.timeInMillis = longSecond
        val format = SimpleDateFormat(LONG_DATE_FORMAT, Locale.getDefault())
        return format.format(gc.time)
    }

    /**
     * 将一个毫秒数时间转换为相应的时间格式（yyyy-MM-dd）
     *
     * @param longSecond
     * @return
     */
    fun formateDateLongToStringSmall2(longSecond: Long): String {
        val format = SimpleDateFormat(LONG_DATE_FORMAT, Locale.getDefault())
        return format.format(longSecond * 1000)
    }

    /**
     * 时间取出月日显示
     */
    fun fomatDateString2MonthDayString(dateStr: String): String {
        val format = SimpleDateFormat(DATE_TIME_FORMAT, Locale.getDefault())
        try {
            // 用parse方法，可能会异常，所以要try-catch
            val date = format.parse(dateStr)
            // 获取日期实例
            val calendar = Calendar.getInstance()
            // 将日历设置为指定的时间
            calendar.time = date
            // 获取年
            val year = calendar.get(Calendar.YEAR)
            // 这里要注意，月份是从0开始。
            val month = calendar.get(Calendar.MONTH)
            // 获取天
            val day = calendar.get(Calendar.DAY_OF_MONTH)
            var monthStr = ""
            var dayStr = ""
            if (month < 9)
                monthStr = "0" + (month + 1)
            else
                monthStr = (month + 1).toString() + ""
            if (day < 10)
                dayStr = "0$day"
            else
                dayStr = day.toString() + ""
            return "$monthStr-$dayStr"
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return ""
    }

    /**
     * 将日期格式的字符串转换为长整型
     */
    fun convert2long(date: String, format: String): Long {
        var dateFormat = format
        try {
            if (date.isNotEmpty()) {
                if (dateFormat.isEmpty())
                    dateFormat = LONG_TIME_FORMAT
                val sf = SimpleDateFormat(dateFormat, Locale.getDefault())
                return sf.parse(date).time
            }
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return 0L
    }

    /**
     * 获取下一天.
     *
     * @param currentDate the current date
     * @return the next date str
     * @throws ParseException the parse exception
     */
    @Throws(ParseException::class)
    fun getNextDateStr(currentDate: String): String {
        val sdf = SimpleDateFormat(LONG_DATE_FORMAT2, Locale.getDefault())
        val date = sdf.parse(currentDate)
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.add(Calendar.DATE, 1)
        return sdf.format(calendar.time)
    }

    /**
     * 获取上一天.
     *
     * @param currentDate the current date
     * @return the next date str
     * @throws ParseException the parse exception
     */
    @Throws(ParseException::class)
    fun getYesterdayStr(currentDate: String): String {
        val sdf = SimpleDateFormat(LONG_DATE_FORMAT2, Locale.getDefault())
        val date = sdf.parse(currentDate)
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.add(Calendar.DATE, -1)
        return sdf.format(calendar.time)
    }

    /**
     * 根据日期获取星期
     *
     * @param strdate
     * @param forMat  20150101 ....
     * @return
     */
    fun getWeekDayByDate(strdate: String, forMat: String): String {
        val dayNames = arrayOf("星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六")
        val sdfInput = SimpleDateFormat(forMat, Locale.getDefault())
        val calendar = Calendar.getInstance()
        var date = Date()
        try {
            date = sdfInput.parse(strdate)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        calendar.time = date
        var dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1
        if (dayOfWeek < 0)
            dayOfWeek = 0
        return dayNames[dayOfWeek]
    }

    fun getDateByDayNumber(day: Int?): Date {
        val cal = Calendar.getInstance()
        cal.time = Date()
        cal.add(Calendar.DATE, day!!)
        return cal.time
    }
}