package com.jn.kikukt.common.utils.date

/**
 * Author：Stevie.Chen Time：2019/7/12
 * Class Comment：
 */
class CalendarVO {
    var year: Int = 0
    var month: Int = 0
    var day: Int = 0
    var week: Int = 0
    private var lunar: String = ""//农历

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
        return "year年${if (month > 9) month.toString() else "0$month"}月"
    }

    override fun toString(): String {
        return "$year-$month-$day"
    }
}
