package com.jn.kikukt.utils.date

/**
 * Author：Stevie.Chen Time：2019/7/15
 * Class Comment：
 */
object TimeUtils {
    private const val yearLevelValue = 365 * 24 * 60 * 60 * 1000L
    private const val monthLevelValue = 30 * 24 * 60 * 60 * 1000L
    private const val dayLevelValue = 24 * 60 * 60 * 1000L
    private const val hourLevelValue = 60 * 60 * 1000L
    private const val minuteLevelValue = 60 * 1000L
    private const val secondLevelValue = 1000L

    fun getDifference(nowTime: Long, targetTime: Long): String {//目标时间与当前时间差
        val period = targetTime - nowTime
        return getDifference(period)
    }

    fun getDifferenceHourAndMinute(nowTime: Long, targetTime: Long): IntArray {//目标时间与当前时间差
        val period = targetTime - nowTime
        /*******计算出时间差中的年、月、日、天、时、分、秒 */
        val year = getYear(period)
        val month = getMonth(period - year * yearLevelValue)
        val day = getDay(period - year * yearLevelValue - month * monthLevelValue)
        val hour = getHour(period - year * yearLevelValue - month * monthLevelValue - day * dayLevelValue)
        val minute =
            getMinute(period - year * yearLevelValue - month * monthLevelValue - day * dayLevelValue - hour * hourLevelValue)
        return intArrayOf(hour, minute)
    }

    private fun getDifference(period: Long): String {//根据毫秒差计算时间差
        val result = StringBuilder()

        /*******计算出时间差中的年、月、日、天、时、分、秒 */
        val year = getYear(period)
        val month = getMonth(period - year * yearLevelValue)
        val day = getDay(period - year * yearLevelValue - month * monthLevelValue)
        val hour = getHour(period - year * yearLevelValue - month * monthLevelValue - day * dayLevelValue)
        val minute =
            getMinute(period - year * yearLevelValue - month * monthLevelValue - day * dayLevelValue - hour * hourLevelValue)
        val second =
            getSecond(period - year * yearLevelValue - month * monthLevelValue - day * dayLevelValue - hour * hourLevelValue - minute * minuteLevelValue)

        if (year != 0)
            result.append("<font color=\"#ff0000\">$year</font>年")
        if (month != 0)
            result.append("<font color=\"#ff0000\">$month</font>月")
        if (day != 0)
            result.append("<font color=\"#ff0000\">$day</font>天")
        if (hour != 0)
            result.append("<font color=\"#ff0000\">$hour</font>时")
        if (minute != 0)
            result.append("<font color=\"#ff0000\">$minute</font>分")
        //        if (second != 0)
        //            result.append("<font color=\"#ff0000\">" + second + "</font>秒");
        return result.toString()
    }

    fun getYear(period: Long): Int {
        return (period / yearLevelValue).toInt()
    }

    fun getMonth(period: Long): Int {
        return (period / monthLevelValue).toInt()
    }

    fun getDay(period: Long): Int {
        return (period / dayLevelValue).toInt()
    }

    fun getHour(period: Long): Int {
        return (period / hourLevelValue).toInt()
    }

    fun getMinute(period: Long): Int {
        return (period / minuteLevelValue).toInt()
    }

    fun getSecond(period: Long): Int {
        return (period / secondLevelValue).toInt()
    }
}