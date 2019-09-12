package com.jn.kikukt.common.utils

import android.os.Build
import android.text.Html
import android.text.TextUtils
import android.util.Base64
import java.io.*
import java.net.URLDecoder
import java.net.URLEncoder
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import kotlin.experimental.and

/**
 * Author：Stevie.Chen Time：2019/7/15
 * Class Comment：编码解码工具类
 */
object EncodeUtils {

    private val HEX_DIGITS = charArrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F')

    /**
     * URL编码
     *
     * 若想自己指定字符集,可以使用[.urlEncode]方法
     *
     * @param input 要编码的字符
     * @return 编码为UTF-8的字符串
     */
    fun urlEncode(input: String): String {
        return urlEncode(input, "UTF-8")
    }

    /**
     * URL编码
     *
     * 若系统不支持指定的编码字符集,则直接将input原样返回
     *
     * @param input   要编码的字符
     * @param charset 字符集
     * @return 编码为字符集的字符串
     */
    fun urlEncode(input: String, charset: String): String {
        return try {
            URLEncoder.encode(input, charset)
        } catch (e: UnsupportedEncodingException) {
            input
        }
    }

    /**
     * URL解码
     *
     * 若想自己指定字符集,可以使用 [.urlDecode]方法
     *
     * @param input 要解码的字符串
     * @return URL解码后的字符串
     */
    fun urlDecode(input: String): String {
        return urlDecode(input, "UTF-8")
    }

    /**
     * URL解码
     *
     * 若系统不支持指定的解码字符集,则直接将input原样返回
     *
     * @param input   要解码的字符串
     * @param charset 字符集
     * @return URL解码为指定字符集的字符串
     */
    fun urlDecode(input: String, charset: String): String {
        return try {
            URLDecoder.decode(input, charset)
        } catch (e: UnsupportedEncodingException) {
            input
        }

    }

    /**
     * Base64编码
     *
     * @param input 要编码的字符串
     * @return Base64编码后的字符串
     */
    fun base64Encode(input: String): ByteArray {
        return base64Encode(input.toByteArray())
    }

    /**
     * Base64编码
     *
     * @param input 要编码的字节数组
     * @return Base64编码后的字符串
     */
    fun base64Encode(input: ByteArray): ByteArray {
        return Base64.encode(input, Base64.NO_WRAP)
    }

    /**
     * Base64编码
     *
     * @param input 要编码的字节数组
     * @return Base64编码后的字符串
     */
    fun base64Encode2String(input: ByteArray): String {
        return Base64.encodeToString(input, Base64.NO_WRAP)
    }

    /**
     * Base64解码
     *
     * @param input 要解码的字符串
     * @return Base64解码后的字符串
     */
    fun base64Decode(input: String): ByteArray {
        return Base64.decode(input, Base64.NO_WRAP)
    }

    /**
     * Base64解码
     *
     * @param input 要解码的字符串
     * @return Base64解码后的字符串
     */
    fun base64Decode(input: ByteArray): ByteArray {
        return Base64.decode(input, Base64.NO_WRAP)
    }

    /**
     * Base64URL安全编码
     *
     * 将Base64中的URL非法字符�?,/=转为其他字符, 见RFC3548
     *
     * @param input 要Base64URL安全编码的字符串
     * @return Base64URL安全编码后的字符串
     */
    fun base64UrlSafeEncode(input: String): ByteArray {
        return Base64.encode(input.toByteArray(), Base64.URL_SAFE)
    }

    /**
     * 将文件转成base64 字符串
     *
     * @param path
     * @return
     * @throws Exception
     */
    fun encodeBase64File(path: String): String? {
        val file = File(path)
        var base64: String? = null
        var `in`: InputStream? = null
        try {
            `in` = FileInputStream(file)
            val bytes = ByteArray(`in`.available())
            val length = `in`.read(bytes)
            base64 = Base64.encodeToString(bytes, 0, length, Base64.DEFAULT)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            try {
                `in`!!.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return base64
    }

    /**
     * Html编码
     *
     * @param input 要Html编码的字符串
     * @return Html编码后的字符串
     */
    fun htmlEncode(input: CharSequence): String {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            return Html.escapeHtml(input)
        } else {
            // 参照Html.escapeHtml()中代码
            val out = StringBuilder()
            var i = 0
            val len = input.length
            while (i < len) {
                val c = input[i]
                if (c == '<') {
                    out.append("&lt;")
                } else if (c == '>') {
                    out.append("&gt;")
                } else if (c == '&') {
                    out.append("&amp;")
                } else if (c.toInt() in 0xD800..0xDFFF) {
                    if (c.toInt() < 0xDC00 && i + 1 < len) {
                        val d = input[i + 1]
                        if (d.toInt() in 0xDC00..0xDFFF) {
                            i++
                            val codepoint = 0x010000 or (c.toInt() - 0xD800 shl 10) or d.toInt() - 0xDC00
                            out.append("&#").append(codepoint).append(";")
                        }
                    }
                } else if (c.toInt() > 0x7E || c < ' ') {
                    out.append("&#").append(c.toInt()).append(";")
                } else if (c == ' ') {
                    while (i + 1 < len && input[i + 1] == ' ') {
                        out.append("&nbsp;")
                        i++
                    }
                    out.append(' ')
                } else {
                    out.append(c)
                }
                i++
            }
            return out.toString()
        }
    }

    /**
     * Html解码
     *
     * @param input 待解码的字符串
     * @return Html解码后的字符串
     */
    fun htmlDecode(input: String): CharSequence {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(input, Html.FROM_HTML_MODE_LEGACY)
        } else {
            Html.fromHtml(input)
        }
    }

    /**
     * 文件路径MD5加密16进制
     *
     * @param filePath 文件路径
     * @return 加密后的字符串
     */
    fun file2md5Hex(filePath: String): String {
        val fis: InputStream
        val buffer = ByteArray(1024)
        var numRead = 0
        val md5: MessageDigest
        try {
            fis = FileInputStream(filePath)
            md5 = MessageDigest.getInstance("MD5")
            numRead = fis.read(buffer)
            while (numRead > 0) {
                md5.update(buffer, 0, numRead)
                numRead = fis.read(buffer)
            }
            fis.close()
            val md5Str = toHexString(md5.digest())
            return if (TextUtils.isEmpty(md5Str)) "" else md5Str
        } catch (e: Exception) {
            println("error")
            return ""
        }

    }

    /**
     * @param b
     * @return
     */
    fun toHexString(b: ByteArray): String {
        val sb = StringBuilder(b.size * 2)
        for (i in b.indices) {
            sb.append(HEX_DIGITS[(b[i] and 0xf0.toByte()).toInt().ushr(4)])
            sb.append(HEX_DIGITS[(b[i] and 0x0f).toInt()])
        }
        return sb.toString()
    }

    /***
     * MD5加密 生成16位md5码(小写)
     * 16位小写加密只需32位.substring(8, 24);即可
     */
    fun str2Md5HexLower(str: String): String {
        return str2Md5Hex2Lower(str).substring(8, 24)
    }

    /***
     * MD5加密 生成16位md5码(大写)
     * 16位小写加密只需32位.substring(8, 24);即可
     */
    fun str2Md5HexUpper(str: String): String {
        return str2Md5HexLower(str).toUpperCase()
    }

    /***
     * MD5加密 生成32位md5码(小写)
     */
    fun str2Md5Hex2Lower(str: String): String {
        var result = ""
        var md5: MessageDigest? = null
        try {
            md5 = MessageDigest.getInstance("MD5")
            // 这句是关键
            md5!!.update(str.toByteArray(charset("UTF-8")))
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }

        val b = md5!!.digest()
        var i: Int
        val buf = StringBuffer("")
        for (offset in b.indices) {
            i = b[offset].toInt()
            if (i < 0)
                i += 256
            if (i < 16)
                buf.append("0")
            buf.append(Integer.toHexString(i))
        }
        result = buf.toString()

        return result
    }

    /***
     * MD5加密 生成32位md5码(大写)
     */
    fun str2Md5Hex2Upper(str: String): String {
        return str2Md5Hex2Lower(str).toUpperCase()
    }

    /**
     * 加密解密算法 执行一次方法加密，两次方法解密(位数为str的长度)
     */
    fun convertMD5(str: String): String {
        val a = str.toCharArray()
        for (i in a.indices) {
            a[i] = (a[i].toInt() xor 't'.toInt()).toChar()
        }
        return String(a)
    }
}