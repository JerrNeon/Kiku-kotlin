package com.jn.kikukt.common.utils.date

/**
 * Author：Stevie.Chen Time：2019/7/12
 * Class Comment：
 */
class CalendarVO {
    internal var year: Int = 0
    internal var month: Int = 0
    internal var day: Int = 0
    internal var week: Int = 0
    internal var lunar = "初三"

    constructor() {
        this.year = CalendarUtils.getYear()
        this.month = CalendarUtils.getMonth()
        this.day = CalendarUtils.getCurrentMonthDay()
    }

    constructor(y: Int, m: Int, day: Int) {
        var year = y
        var month = m
        if (month > 12) {
            month = 1
            year++
        } else if (month < 1) {
            month = 12
            year--
        }
        this.year = year
        this.month = month
        this.day = day
    }

    /**
     * 月份增加
     */
    fun addMonth() {
        if (month == 12) {
            month = 1
            year++
        } else {
            month++
        }

    }

    /**
     * 月份减少
     */
    fun subMonth() {
        if (month == 1) {
            month = 12
            year--
        } else {
            month--
        }
    }

    /**
     * 获取字符串格式时间 2015年08月
     */
    fun getDateString(): String {
        return (year.toString() + "年" + (if (month > 9) month else "0$month")
                + "月")
    }

    /**
     * @return the lunar
     */
    fun getLunar(): String {
        //农历转换
        //Calendar calCalendar = Calendar.getInstance();
        // calCalendar.setTime(DateUtil.string2Date(year + "-" + month + "-" + day));
        // CalendarUtil calendarUtil = new CalendarUtil(calCalendar);
        //lunar = calendarUtil.toString();
        return lunar
    }

    /**
     * @param lunar the lunar to set
     */
    fun setLunar(lunar: String) {
        this.lunar = lunar
    }

    override fun toString(): String {
        return "$year-$month-$day"
    }

    fun getYear(): Int {
        return year
    }

    fun setYear(year: Int) {
        this.year = year
    }

    fun getMonth(): Int {
        return month
    }

    fun setMonth(month: Int) {
        this.month = month
    }

    fun getDay(): Int {
        return day
    }

    fun setDay(day: Int) {
        this.day = day
    }

    fun getWeek(): Int {
        return week
    }

    fun setWeek(week: Int) {
        this.week = week
    }
}
