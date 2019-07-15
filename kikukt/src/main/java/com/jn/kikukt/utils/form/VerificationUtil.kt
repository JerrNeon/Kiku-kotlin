package com.jn.kikukt.utils.form

import java.util.regex.Pattern

/**
 * Author：Stevie.Chen Time：2019/7/15
 * Class Comment：
 */
object VerificationUtil {

    /**
     * 验证Email
     *
     * @param email email地址，格式：zhangsan@sina.com，zhangsan@xxx.com.cn，xxx代表邮件服务商
     * @return 验证成功返回true，验证失败返回false
     */
    fun checkEmail(email: String): Boolean {
        val regex = "\\w+@\\w+\\.[a-z]+(\\.[a-z]+)?"
        return Pattern.matches(regex, email)
    }

    /**
     * 验证身份证号码
     *
     * @param idCard 18位，最后一位可能是数字或字母
     * @return 验证成功返回true，验证失败返回false
     */
    fun checkIdCard(idCard: String): Boolean {
        val regex = "^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{4}$"
        return Pattern.matches(regex, idCard)
    }

    /**
     * 验证手机号码（支持国际格式，+86135xxxx...（中国内地），+00852137xxxx...（中国香港））
     *
     * @param mobile 移动、联通、电信运营商的号码段
     *
     *
     * 移动的号段：134(0-8)、135、136、137、138、139、147（预计用于TD上网卡）
     * 、150、151、152、157（TD专用）、158、159、187（未启用）、188（TD专用）
     *
     *
     *
     * 联通的号段：130、131、132、155、156（世界风专用）、185（未启用）、186（3g）
     *
     *
     *
     * 电信的号段：133、153、180（未启用）、189
     *
     *
     *
     * 4G号段：17*
     *
     * @return 验证成功返回true，验证失败返回false
     */
    fun checkMobile(mobile: String): Boolean {
        val regex = "(\\+\\d+)?1[34578]\\d{9}$"
        return Pattern.matches(regex, mobile)
    }

    /**
     * 验证固定电话号码
     *
     * @param phone 电话号码，格式：国家（地区）电话代码 + 区号（城市代码） + 电话号码，如：+8602085588447
     *
     *
     * **国家（地区） 代码 ：**标识电话号码的国家（地区）的标准国家（地区）代码。它包含从 0 到 9
     * 的一位或多位数字， 数字之后是空格分隔的国家（地区）代码。
     *
     *
     *
     * **区号（城市代码）：**这可能包含一个或多个从 0 到 9 的数字，地区或城市代码放在圆括号——
     * 对不使用地区或城市代码的国家（地区），则省略该组件。
     *
     *
     *
     * **电话号码：**这包含从 0 到 9 的一个或多个数字
     *
     * @return 验证成功返回true，验证失败返回false
     */
    fun checkPhone(phone: String): Boolean {
        val regex = "(\\+\\d+)?(\\d{3,4}\\-?)?\\d{7,8}$"
        return Pattern.matches(regex, phone)
    }

    /**
     * 验证整数（正整数和负整数）
     *
     * @param digit 一位或多位0-9之间的整数
     * @return 验证成功返回true，验证失败返回false
     */
    fun checkDigit(digit: String): Boolean {
        val regex = "\\-?[1-9]\\d+"
        return Pattern.matches(regex, digit)
    }

    /**
     * 验证整数和浮点数（正负整数和正负浮点数）
     *
     * @param decimals 一位或多位0-9之间的浮点数，如：1.23，233.30
     * @return 验证成功返回true，验证失败返回false
     */
    fun checkDecimals(decimals: String): Boolean {
        val regex = "\\-?[1-9]\\d+(\\.\\d+)?"
        return Pattern.matches(regex, decimals)
    }

    /**
     * 验证整数 0-99的正整数
     *
     * @param decimals nums
     * @return 验证成功返回true，验证失败返回false
     */
    fun checkNums(decimals: String): Boolean {
        val regex = "^[1-9][0-9]?$"
        return Pattern.matches(regex, decimals)
    }

    /**
     * 验证空白字符
     *
     * @param blankSpace 空白字符，包括：空格、\t、\n、\r、\f、\x0B
     * @return 验证成功返回true，验证失败返回false
     */
    fun checkBlankSpace(blankSpace: String): Boolean {
        val regex = "\\s+"
        return Pattern.matches(regex, blankSpace)
    }

    /**
     * 验证中文
     *
     * @param chinese 中文字符
     * @return 验证成功返回true，验证失败返回false
     */
    fun checkChinese(chinese: String): Boolean {
        val regex = "^[\u4E00-\u9FA5]+$"
        return Pattern.matches(regex, chinese)
    }

    /**
     * 验证日期（年月日）
     *
     * @param birthday 日期，格式：1992-09-03，或1992.09.03
     * @return 验证成功返回true，验证失败返回false
     */
    fun checkBirthday(birthday: String): Boolean {
        val regex = "[1-9]{4}([-./])\\d{1,2}\\1\\d{1,2}"
        return Pattern.matches(regex, birthday)
    }

    /**
     * 验证URL地址
     *
     * @param url 格式：http://blog.csdn.net:80/xyang81/article/details/7705960? 或
     * http://www.csdn.net:80
     * @return 验证成功返回true，验证失败返回false
     */
    fun checkURL(url: String): Boolean {
        val regex = "(https?://(w{3}\\.)?)?\\w+\\.\\w+(\\.[a-zA-Z]+)*(:\\d{1,5})?(/\\w*)*(\\??(.+=.*)?(&.+=.*)?)?"
        return Pattern.matches(regex, url)
    }

    /**
     * 匹配中国邮政编码
     *
     * @param postcode 邮政编码
     * @return 验证成功返回true，验证失败返回false
     */
    fun checkPostcode(postcode: String): Boolean {
        val regex = "[0-9]\\d{5}"
        return Pattern.matches(regex, postcode)
    }

    /**
     * 匹配IP地址(简单匹配，格式，如：192.168.1.1，127.0.0.1，没有匹配IP段的大小)
     *
     * @param ipAddress IPv4标准地址
     * @return 验证成功返回true，验证失败返回false
     */
    fun checkIpAddress(ipAddress: String): Boolean {
        val regex = "[1-9](\\d{1,2})?\\.(0|([1-9](\\d{1,2})?))\\.(0|([1-9](\\d{1,2})?))\\.(0|([1-9](\\d{1,2})?))"
        return Pattern.matches(regex, ipAddress)
    }

    /**
     * 验证QQ(腾讯QQ号从10000开始)
     *
     * @param qq
     * @return 验证成功返回true，验证失败返回false
     */
    fun checkQQ(qq: String): Boolean {
        val regex = "[1-9][0-9]{4,}"
        return Pattern.matches(regex, qq)
    }

    /**
     * 验证金钱
     *
     * @param price
     * @return 验证成功返回true，验证失败返回false
     */
    fun checkPrice(price: String): Boolean {
        val regex = "^(([1-9]+[0-9]*.{1}[0-9]+)|([0].{1}[1-9]+[0-9]*)|([1-9][0-9]*)|([0][.][0-9]+[1-9]*))$"
        return Pattern.matches(regex, price)
    }

    /**
     * 验证传真
     *
     * @param fax
     * @return 验证成功返回true，验证失败返回false
     */
    fun checkFax(fax: String): Boolean {
        //        String regex = "(^(\\d{3,4}-)?\\d{7,8})$|^((1[0-9][0-9]\\d{8}$))";
        //        return Pattern.matches(regex, fax);
        return if (fax.length > 5 && fax.length < 12) true else false
    }

    /**
     * 验证车牌号
     * ^[\u4e00-\u9fa5]{1}代表以汉字开头并且只有一个，这个汉字是车辆所在省的简称
     * [A-Z]{1}代表A-Z的大写英文字母且只有一个，代表该车所在地的地市一级代码
     * [A-Z_0-9]{5}代表后面五个数字是字母和数字的组合。
     *
     * @param carNum
     * @return 验证成功返回true，验证失败返回false
     */
    fun checkCarnum(carNum: String): Boolean {
        val regex = "^[\\u4e00-\\u9fa5]{1}[A-Z]{1}[A-Z_0-9]{5}$"
        return Pattern.matches(regex, carNum)
    }

    /**
     * 验证密码
     * 只能为数字字母下划线组合 长度为8到16
     *
     * @param password
     * @return 验证成功返回true，验证失败返回false
     */
    fun checkPassword(password: String): Boolean {
        val regex = "^[a-zA-Z0-9_]{8,16}$"
        return Pattern.matches(regex, password)
    }
}