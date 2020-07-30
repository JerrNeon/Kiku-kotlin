package com.jn.kikukt.common.utils.date

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Author：Stevie.Chen Time：2019/7/15
 * Class Comment：
 */
object CalendarUtils {

    private val weekName = arrayOf("周日", "周一", "周二", "周三", "周四", "周五", "周六")

    fun getMonthDays(y: Int, m: Int): Int {
        var year = y
        var month = m
        if (month > 12) {
            month = 1
            year += 1
        } else if (month < 1) {
            month = 12
            year -= 1
        }
        val arr = intArrayOf(31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31)
        var days = 0

        if (year % 4 == 0 && year % 100 != 0 || year % 400 == 0) {
            arr[1] = 29 // 闰年2月29天
        }

        try {
            days = arr[month - 1]
        } catch (e: Exception) {
            e.stackTrace
        }

        return days
    }

    fun getYear(): Int {
        return Calendar.getInstance().get(Calendar.YEAR)
    }

    fun getMonth(): Int {
        return Calendar.getInstance().get(Calendar.MONTH) + 1
    }

    fun getDay(): Int {
        return Calendar.getInstance().get(Calendar.DATE)
    }

    //日
    fun getCurrentMonthDay(): Int {
        return Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
    }

    //星期
    fun getWeekDay(): Int {
        return Calendar.getInstance().get(Calendar.DAY_OF_WEEK)
    }

    //当年的第几周
    fun getWeekOfYear(): Int {
        return Calendar.getInstance().get(Calendar.WEEK_OF_YEAR)
    }

    //当年的第几周
    fun getWeekOfMonth(): Int {
        return Calendar.getInstance().get(Calendar.WEEK_OF_MONTH)
    }

    // 获取当前时间所在年的最大周数
    fun getMaxWeekNumOfYear(year: Int): Int {
        val c = GregorianCalendar()
        c.set(year, Calendar.DECEMBER, 31, 23, 59, 59)
        return getWeekOfYear(c.time)
    }

    // 获取当前时间所在年的周数
    fun getWeekOfYear(date: Date): Int {
        val c = GregorianCalendar()
        c.firstDayOfWeek = Calendar.MONDAY
        c.minimalDaysInFirstWeek = 7
        c.time = date
        return c.get(Calendar.WEEK_OF_YEAR)
    }

    fun getHour(): Int {
        return Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
    }

    fun getMinute(): Int {
        return Calendar.getInstance().get(Calendar.MINUTE)
    }

    fun getSecond(): Int {
        return Calendar.getInstance().get(Calendar.SECOND)
    }

    fun getNextSunday(): CalendarVO {
        val c = Calendar.getInstance()
        c.add(Calendar.DATE, 7 - getWeekDay() + 1)
        return CalendarVO(
            c.get(Calendar.YEAR),
            c.get(Calendar.MONTH) + 1, c.get(Calendar.DAY_OF_MONTH)
        )
    }

    fun getWeekSunday(year: Int, month: Int, day: Int, pervious: Int): IntArray {
        val time = IntArray(3)
        val c = Calendar.getInstance()
        c.set(Calendar.YEAR, year)
        c.set(Calendar.MONTH, month)
        c.set(Calendar.DAY_OF_MONTH, day)
        c.add(Calendar.DAY_OF_MONTH, pervious)
        time[0] = c.get(Calendar.YEAR)
        time[1] = c.get(Calendar.MONTH) + 1
        time[2] = c.get(Calendar.DAY_OF_MONTH)
        return time
    }

    /**
     * 获得时间的星期
     * @param year
     * @param month
     * @param day
     * @return
     */
    fun getWeek(year: Int, month: Int, day: Int): String {
        val c = Calendar.getInstance()
        c.set(Calendar.YEAR, year)
        c.set(Calendar.MONTH, month - 1)
        c.set(Calendar.DAY_OF_MONTH, day)
        var dayOfWeek = c.get(Calendar.DAY_OF_WEEK) - 1
        if (dayOfWeek < 0)
            dayOfWeek = 0
        return weekName[dayOfWeek]
    }

    fun getWeekDayFromDate(year: Int, month: Int): Int {
        val cal = Calendar.getInstance()
        cal.time = getDateFromString(year, month)!!
        var weekIndex = cal.get(Calendar.DAY_OF_WEEK) - 1
        if (weekIndex < 0) {
            weekIndex = 0
        }
        return weekIndex
    }

    fun getDateFromString(year: Int, month: Int): Date? {
        val dateString = (year.toString() + "-" + (if (month > 9) month else "0$month")
                + "-01")
        var date: Date? = null
        try {
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            date = sdf.parse(dateString)
        } catch (e: ParseException) {
            println(e.message)
        }
        return date
    }

    /**
     * 获取两位格式时间 1-> 01; 13-> 13
     * @param time
     * @return
     */
    fun getTwoTime(time: Int): String {
        return if (time > 9) time.toString() else "0$time"
    }

    fun isToday(date: CalendarVO): Boolean {
        return (date.year == getYear() &&
                date.month == getMonth()
                && date.day == getCurrentMonthDay())
    }

    fun isCurrentMonth(date: CalendarVO): Boolean {
        return date.year == getYear() && date.month == getMonth()
    }

    /******************2015年10月26日***************************/

    /**
     * 根据年 月 获取对应的月份 天数
     */
    fun getDaysByYearMonth(year: Int, month: Int): Int {
        val a = Calendar.getInstance()
        a.set(Calendar.YEAR, year)
        a.set(Calendar.MONTH, month - 1)
        a.set(Calendar.DATE, 1)
        a.roll(Calendar.DATE, -1)
        return a.get(Calendar.DATE)
    }

    /**
     * @param year
     * @param month
     * @param day
     * @return 2015-09-12 14:57:19
     */
    fun getStringFormat(year: Int, month: Int, day: Int, isStart: Boolean): String {
        return if (isStart)
            year.toString() + "-" + (if (month > 9) month else "0$month") + "-" +
                    (if (day > 9) day else "0$day") + " " + "00:00:00"
        else
            year.toString() + "-" + (if (month > 9) month else "0$month") + "-" +
                    (if (day > 9) day else "0$day") + " " + "23:59:59"
    }

    /**
     *
     * @param year
     * @param month
     * @param day
     * @return  20151023
     */
    fun getStrFormatYMD(year: Int, month: Int, day: Int): String {
        return year.toString() + "" + (if (month > 9) month else "0$month") +
                if (day > 9) day else "0$day"
    }

    /**
     * 获取月份第一天时间戳
     */
    fun getMonthFirst(mDate: CalendarVO): String? {
        //NLogUtil.logI("starttime", mDate.getYear()+"-"+mDate.getMonth()+"-"+"1");
        return getTime(
            getStringFormat(
                mDate.year, mDate.month, 1, true
            ), "yyyy-MM-dd HH:mm:ss"
        )
    }

    /**
     * 获取月份最后一天时间戳
     */
    fun getMonthFinally(mDate: CalendarVO): String? {
        //选择月份的最大日期
        val selectMonthMaxDay = getMonthDays(mDate.year, mDate.month)
        //NLogUtil.logI("end", mDate.getYear()+"-"+mDate.getMonth()+"-"+selectMonthMaxDay);
        return getTime(
            getStringFormat(
                mDate.year,
                mDate.month, selectMonthMaxDay, false
            ), "yyyy-MM-dd HH:mm:ss"
        )
    }

    /**
     * 比较当前月份
     *
     * @param mDate
     * @return 相等返回 0 ;小于返回 -1；大于返回1
     */
    fun compareCurrentMonth(mDate: CalendarVO): Int {
        return when {
            mDate.year < getYear() -> //判断年
                -1
            mDate.year > getYear() -> 1
            mDate.month < getMonth() -> //判断月
                -1
            mDate.month > getMonth() -> 1
            else -> 0
        }

    }

    /**
     * 比较当前日期
     *
     * @param mDate
     * @return 相等返回 0 ;小于返回 -1；大于返回1
     */
    fun compareCurrentDay(mDate: CalendarVO): Int {
        return when {
            mDate.year < getYear() -> //判断年
                -1
            mDate.year > getYear() -> 1
            mDate.month < getMonth() -> //年份相等判断月
                -1
            mDate.month > getMonth() -> 1
            mDate.day > getCurrentMonthDay() -> //月份相等判断日
                1
            mDate.day < getCurrentMonthDay() -> -1
            else -> 0
        }
    }

    /**
     * mDate1 与 mDate2 比较
     * @param mDate1
     * @param mDate2
     * @return   相等返回 0 ;小于返回 -1；大于返回1
     */
    fun compareTwoDay(mDate1: CalendarVO, mDate2: CalendarVO): Int {
        return when {
            mDate1.year < mDate2.year -> -1
            mDate1.year > mDate2.year -> 1
            else -> when {
                mDate1.month < mDate2.month -> -1
                mDate1.month > mDate2.month -> 1
                else -> 0
            }
        }
    }

    /**
     * 将字符串转为时间戳
     *
     * @param timeStr   时间字符串
     * @param dateStyle 转化格式
     * @return
     */
    fun getTime(timeStr: String, dateStyle: String): String? {
        //NLogUtil.logI("日期", timeStr);
        var reTime: String? = null
        //SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日HH时mm分ss秒");
        val sdf = SimpleDateFormat(dateStyle, Locale.getDefault())
        val d: Date?
        try {
            d = sdf.parse(timeStr)
            val l = d.time
            reTime = l.toString()
            //NLogUtil.logI("时间戳", re_time);
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return reTime
    }

}