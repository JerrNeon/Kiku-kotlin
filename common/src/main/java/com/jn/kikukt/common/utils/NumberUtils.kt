package com.jn.kikukt.common.utils

import java.math.BigDecimal
import java.text.DecimalFormat

/**
 * Author：Stevie.Chen Time：2019/7/15
 * Class Comment：
 */
object NumberUtils {

    private const val DEF_DIV_SCALE = 10

    /**
     * * 两个Double数相加
     *
     * @param v1
     * @param v2
     * @return Double
     */
    fun add(v1: Double, v2: Double): Double {
        val b1 = BigDecimal(v1.toString())
        val b2 = BigDecimal(v2.toString())
        return b1.add(b2).toDouble()
    }

    /**
     * * 两个Double数相减
     *
     * @param v1
     * @param v2
     * @return Double
     */
    fun sub(v1: Double, v2: Double): Double {
        val b1 = BigDecimal(v1.toString())
        val b2 = BigDecimal(v2.toString())
        return b1.subtract(b2).toDouble()
    }

    /**
     * * 两个Double数相乘
     *
     * @param v1
     * @param v2
     * @return Double
     */
    fun mul(v1: Double, v2: Double): Double {
        val b1 = BigDecimal(v1.toString())
        val b2 = BigDecimal(v2.toString())
        return b1.multiply(b2).toDouble()
    }

    /**
     * * 两个Double数相除
     *
     * @param v1
     * @param v2
     * @return Double
     */
    fun div(v1: Double, v2: Double): Double {
        val b1 = BigDecimal(v1.toString())
        val b2 = BigDecimal(v2.toString())
        return b1.divide(b2, DEF_DIV_SCALE, BigDecimal.ROUND_HALF_UP)
            .toDouble()
    }

    /**
     * * 两个Double数相除，并保留scale位小数
     *
     * @param v1
     * @param v2
     * @param scale
     * @return Double
     */
    fun div(v1: Double?, v2: Double?, scale: Int): Double {
        if (scale < 0) {
            throw IllegalArgumentException(
                "The scale must be a positive integer or zero"
            )
        }
        val b1 = BigDecimal(v1!!.toString())
        val b2 = BigDecimal(v2!!.toString())
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).toDouble()
    }

    /**
     * 数据格式化.
     *
     * @param pattern the pattern
     * @param value   the i
     * @return the string
     */
    fun codeFormat(pattern: String, value: Any): String {
        val df = DecimalFormat(pattern)
        return df.format(value)
    }

    /**
     * 格式化金额.
     *
     * @param value the value
     * @return the string
     */
    fun formatCurrency2String(value: Long?): String {
        if (value == null || "0" == value.toString()) {
            return "0.00"
        }
        val df = DecimalFormat("0.00")
        return df.format(value / 100.00)
    }

    /**
     * 格式化金额.
     *
     * @param priceFormat the price format
     * @return the long
     */
    fun formatCurrency2Long(priceFormat: String): Long? {
        val bg = BigDecimal(priceFormat)
        return bg.multiply(BigDecimal(100)).toLong()
    }

    /**
     * 格式化金额
     *
     * @param StrBd
     * @return
     */
    fun formatString2Bigdecimal(StrBd: String): BigDecimal {
        var bd = BigDecimal(StrBd)
        //设置小数位数，第一个变量是小数位数，第二个变量是取舍方法(四舍五入)
        bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP)
        return bd
    }

    /**
     * 格式化金额.(保留两位小数)
     *
     * @param value the value
     * @return the string
     */
    fun formatDouble2String(value: Double): String {
        val df = DecimalFormat("0.00")
        return df.format(value)
    }

    /**
     * 格式化折扣.(保留一位小数)
     *
     * @param value the value
     * @return the string
     */
    fun formatDiscount(value: Double): String {
        return try {
            val bigDec = BigDecimal(value)
            val total = bigDec.setScale(1, BigDecimal.ROUND_HALF_UP).toDouble()
            val df = DecimalFormat("0.0")
            df.format(total)
        } catch (e: Exception) {
            e.printStackTrace()
            "0.0"
        }

    }

    /**
     * 生成固定长度的随机字符和数字
     *
     * @param len
     * @return
     */
    fun generateRandomCharAndNumber(len: Int): String {
        val sb = StringBuffer()
        for (i in 0 until len) {
            val intRand = (Math.random() * 52).toInt()
            val numValue = (Math.random() * 10).toInt()
            val base = if (intRand < 26) 'A' else 'a'
            val c = (base.toInt() + intRand % 26).toChar()
            if (numValue % 2 == 0) {
                sb.append(c)
            } else {
                sb.append(numValue)
            }
        }
        return sb.toString()
    }

    /**
     * @param count 位数，如果是1就产生1位的数字，如果是2就产生2位数字，依次类推
     * @return
     * @Title: getRandomNumber
     * @Description: 获取随机数
     */
    fun getRandomNumber(count: Int): String {
        var result = ""
        for (i in 0 until count) {
            val rand = (Math.random() * 10).toInt()
            result += rand
        }
        return result
    }

    /**
     * 逗号分隔价格并带两位小数
     *
     * @param price
     * @return
     */
    fun formatPriceWith2Decimal(price: Double): String {
        return if (price == 0.0)
            "0.00"
        else if (price < 1) {
            DecimalFormat("0.00").format(price)
        } else {
            DecimalFormat(",###,###.00").format(price)
        }
    }

    /**
     * 逗号分隔价格
     *
     * @param price
     * @return
     */
    fun formatPriceWithComma(price: Double): String {
        if (price == 0.0)
            return removeZeroBehindPoint(price.toString())
        else if (price < 1) {
            val result = DecimalFormat("0.00").format(price)
            return removeZeroBehindPoint(result)
        } else {
            val result = DecimalFormat(",###,###.00").format(price)
            return removeZeroBehindPoint(result)
        }
    }

    /**
     * 逗号分隔价格
     *
     * @param price
     * @return
     */
    fun formatPriceWithComma(price: Long): String {
        return DecimalFormat(",###,###").format(price)
    }

    /**
     * 去掉后面无用的零,小数点后面全是零则去掉小数点
     *
     * @param object
     * @return
     */
    fun removeZeroBehindPoint(`object`: Any): String {
        var str = `object`.toString()
        if (str.indexOf(".") > 0) {
            //正则表达
            str = str.replace("0+?$".toRegex(), "")//去掉后面无用的零
            str = str.replace("[.]$".toRegex(), "")//如小数点后面全是零则去掉小数点
        }
        return str
    }

    /**
     * 距离转换 m转化为Km
     *
     * @param distance
     * @return
     */
    fun translateDistance(distance: Int): String {
        try {
            if (distance < 1000)
                return distance.toString() + "m"
            else {
                var result = distance / 1000.0
                val bigDecimal = BigDecimal(result)
                result = bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).toDouble()
                val df = DecimalFormat("0.00")
                return removeZeroBehindPoint(df.format(result)) + "km"
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return ""
        }

    }

    /**
     * 距离转换 m转化为Km
     *
     * @param distance
     * @return
     */
    fun translateDistance(distance: Double): String {
        try {
            if (distance < 1000) {
                //取整四舍五入
                val bigDecimal = BigDecimal(distance)
                val result = bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).toDouble()
                val df = DecimalFormat("0")
                return df.format(result) + "m"
            } else {
                //保留一位小数四舍五入
                var result = distance / 1000.0
                val bigDecimal = BigDecimal(result)
                result = bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).toDouble()
                val df = DecimalFormat("0.0")
                return removeZeroBehindPoint(df.format(result)) + "km"
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return ""
        }

    }

    /**
     * 手机号中间四位用*号代替
     *
     * @param phone
     * @return
     */
    fun formatPhone(phone: String): String {
        return try {
            if ("" == phone)
                phone
            else
                phone.substring(0, 3) + "****" + phone.substring(7, phone.length)
        } catch (e: Exception) {
            e.printStackTrace()
            phone
        }

    }

    /**
     * 名称只显示第一位，其余都用*号代替
     *
     * @param name
     * @return
     */
    fun formatName(name: String?): String {
        try {
            return when (name) {
                null -> ""
                "" -> name
                else -> {
                    val sb = StringBuilder()
                    for (i in 0 until name.length - 1) {
                        sb.append("*")
                    }
                    name.substring(0, 1) + sb.toString()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return name!!
        }
    }

    /**
     * 科学计数法转成数字
     *
     * @param str
     * @return
     */
    fun scientific2Str(str: String): String {
        return try {
            val bd = BigDecimal(str)
            bd.toPlainString()
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }

    }
}