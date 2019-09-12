package com.jn.kikukt.common.utils.form

/**
 * Author：Stevie.Chen Time：2019/7/15
 * Class Comment：
 */
object RegexConstants {

    /**
     * 验证Email
     *
     * @param email email地址，格式：zhangsan@sina.com，zhangsan@xxx.com.cn，xxx代表邮件服务商
     */
    const val regexEmail = "\\w+@\\w+\\.[a-z]+(\\.[a-z]+)?"

    /**
     * 验证身份证号码
     *
     * @param idCard 18位，最后一位可能是数字或字母
     */
    const val regexIdCard = "^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{4}$"

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
     */
    const val regexMobile = "(\\+\\d+)?1[123456789]\\d{9}$"

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
    const val regexFixedDialling = "(\\+\\d+)?(\\d{3,4}\\-?)?\\d{7,8}$"

    /**
     * 验证整数（正整数和负整数）
     *
     * @param digit 一位或多位0-9之间的整数
     * @return 验证成功返回true，验证失败返回false
     */
    val regexDigit = "\\-?[1-9]\\d+"

    /**
     * 验证整数和浮点数（正负整数和正负浮点数）
     *
     * @param decimals 一位或多位0-9之间的浮点数，如：1.23，233.30
     * @return 验证成功返回true，验证失败返回false
     */
    const val regexDecimal = "\\-?[1-9]\\d+(\\.\\d+)?"

    /**
     * 验证整数 0-99的正整数
     *
     * @param decimals nums
     * @return 验证成功返回true，验证失败返回false
     */
    const val regexNums = "^[1-9][0-9]?$"

    /**
     * 验证空白字符
     *
     * @param blankSpace 空白字符，包括：空格、\t、\n、\r、\f、\x0B
     * @return 验证成功返回true，验证失败返回false
     */
    const val regexBlankSpace = "\\s+"

    /**
     * 验证中文
     *
     * @param chinese 中文字符
     * @return 验证成功返回true，验证失败返回false
     */
    const val regexChinese = "^[\u4E00-\u9FA5]+$"

    /**
     * 验证日期（年月日）
     *
     * @param birthday 日期，格式：1992-09-03，或1992.09.03
     * @return 验证成功返回true，验证失败返回false
     */
    const val regexBirthday = "[1-9]{4}([-./])\\d{1,2}\\1\\d{1,2}"

    /**
     * 验证URL地址
     *
     * @param url 格式：http://blog.csdn.net:80/xyang81/article/details/7705960? 或
     * http://www.csdn.net:80
     * @return 验证成功返回true，验证失败返回false
     */
    const val regexUrl = "(https?://(w{3}\\.)?)?\\w+\\.\\w+(\\.[a-zA-Z]+)*(:\\d{1,5})?(/\\w*)*(\\??(.+=.*)?(&.+=.*)?)?"

    /**
     * 匹配中国邮政编码
     *
     * @param postcode 邮政编码
     * @return 验证成功返回true，验证失败返回false
     */
    const val regexPostCode = "[0-9]\\d{5}"

    /**
     * 匹配IP地址(简单匹配，格式，如：192.168.1.1，127.0.0.1，没有匹配IP段的大小)
     *
     * @param ipAddress IPv4标准地址
     * @return 验证成功返回true，验证失败返回false
     */
    const val regexIpAddress =
        "[1-9](\\d{1,2})?\\.(0|([1-9](\\d{1,2})?))\\.(0|([1-9](\\d{1,2})?))\\.(0|([1-9](\\d{1,2})?))"

    /**
     * 验证QQ(腾讯QQ号从10000开始)
     *
     * @param qq
     * @return 验证成功返回true，验证失败返回false
     */
    const val regexQQ = "[1-9][0-9]{4,}"

    /**
     * 验证金钱
     *
     * @param price
     * @return 验证成功返回true，验证失败返回false
     */
    const val regexPrice = "^(([1-9]+[0-9]*.{1}[0-9]+)|([0].{1}[1-9]+[0-9]*)|([1-9][0-9]*)|([0][.][0-9]+[1-9]*))$"


    /**
     * 验证车牌号
     * ^[\u4e00-\u9fa5]{1}代表以汉字开头并且只有一个，这个汉字是车辆所在省的简称
     * [A-Z]{1}代表A-Z的大写英文字母且只有一个，代表该车所在地的地市一级代码
     * [A-Z_0-9]{5}代表后面五个数字是字母和数字的组合。
     *
     * @param carNum
     * @return 验证成功返回true，验证失败返回false
     */
    const val regexCarNum = "^[\\u4e00-\\u9fa5]{1}[A-Z]{1}[A-Z_0-9]{5}$"

    /**
     * 验证密码
     * 只能为数字字母下划线组合 长度为6到16
     *
     * @param password
     * @return 验证成功返回true，验证失败返回false
     */
    const val regexPassWord = "^[a-zA-Z0-9_]{6,16}$"
}