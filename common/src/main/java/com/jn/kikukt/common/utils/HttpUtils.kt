package com.jn.kikukt.common.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import java.io.*
import java.net.HttpURLConnection
import java.net.URL

/**
 * Author：Stevie.Chen Time：2019/7/15
 * Class Comment：
 */
class HttpUtils {

    companion object {

        private const val TIMEOUT_IN_MILLIONS = 5000

        /**
         * 异步的Get请求
         *
         * @param urlStr
         * @param callBack
         */
        fun doGetAsyn(urlStr: String, callBack: CallBack?) {
            object : Thread() {
                override fun run() {
                    try {
                        val result = doGet(urlStr)
                        callBack?.onRequestComplete(result)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                }
            }.start()
        }

        /**
         * 异步的Post请求
         *
         * @param urlStr
         * @param params
         * @param callBack
         * @throws Exception
         */
        @Throws(Exception::class)
        fun doPostAsyn(
            urlStr: String, params: String,
            callBack: CallBack?
        ) {
            object : Thread() {
                override fun run() {
                    try {
                        val result = doPost(urlStr, params)
                        callBack?.onRequestComplete(result)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                }
            }.start()

        }

        /**
         * Get请求，获得返回数据
         *
         * @param urlStr
         * @return
         * @throws Exception
         */
        fun doGet(urlStr: String): String? {
            var url: URL? = null
            var conn: HttpURLConnection? = null
            var `is`: InputStream? = null
            var baos: ByteArrayOutputStream? = null
            try {
                url = URL(urlStr)
                conn = url.openConnection() as HttpURLConnection
                conn.readTimeout = TIMEOUT_IN_MILLIONS
                conn.connectTimeout = TIMEOUT_IN_MILLIONS
                conn.requestMethod = "GET"
                conn.setRequestProperty("accept", "*/*")
                conn.setRequestProperty("connection", "Keep-Alive")
                if (conn.responseCode == 200) {
                    `is` = conn.inputStream
                    baos = ByteArrayOutputStream()
                    var len: Int
                    val buf = ByteArray(128)

                    len = `is`!!.read(buf)
                    while (len != -1) {
                        baos.write(buf, 0, len)
                        len = `is`!!.read(buf)
                    }
                    baos.flush()
                    return baos.toString()
                } else {
                    throw RuntimeException(" responseCode is not 200 ... ")
                }

            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                try {
                    `is`?.close()
                } catch (e: IOException) {
                }

                try {
                    baos?.close()
                } catch (e: IOException) {
                }

                conn!!.disconnect()
            }

            return null

        }

        /**
         * 向指定 URL 发送POST方法的请求
         *
         * @param url   发送请求的 URL
         * @param param 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
         * @return 所代表远程资源的响应结果
         * @throws Exception
         */
        fun doPost(url: String, param: String?): String {
            var out: PrintWriter? = null
            var `in`: BufferedReader? = null
            var result = ""
            try {
                val realUrl = URL(url)
                // 打开和URL之间的连接
                val conn = realUrl
                    .openConnection() as HttpURLConnection
                // 设置通用的请求属性
                conn.setRequestProperty("accept", "*/*")
                conn.setRequestProperty("connection", "Keep-Alive")
                conn.requestMethod = "POST"
                conn.setRequestProperty(
                    "Content-Type",
                    "application/x-www-form-urlencoded"
                )
                conn.setRequestProperty("charset", "utf-8")
                conn.useCaches = false
                // 发送POST请求必须设置如下两行
                conn.doOutput = true
                conn.doInput = true
                conn.readTimeout = TIMEOUT_IN_MILLIONS
                conn.connectTimeout = TIMEOUT_IN_MILLIONS

                if (param != null && param.trim { it <= ' ' } != "") {
                    // 获取URLConnection对象对应的输出流
                    out = PrintWriter(conn.outputStream)
                    // 发送请求参数
                    out.print(param)
                    // flush输出流的缓冲
                    out.flush()
                }
                // 定义BufferedReader输入流来读取URL的响应
                `in` = BufferedReader(
                    InputStreamReader(conn.inputStream)
                )
                var line: String?
                line = `in`.readLine()
                while (line != null) {
                    result += line
                    line = `in`.readLine()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                try {
                    out?.close()
                    `in`?.close()
                } catch (ex: IOException) {
                    ex.printStackTrace()
                }

            }// 使用finally块来关闭输出流、输入流
            return result
        }

        fun getBitmapFromUrlAndCrop(url: String): Bitmap? {
            // 显示网络上的图片
            var bitmap: Bitmap? = null
            try {
                val myFileUrl = URL(url)
                val conn = myFileUrl.openConnection() as HttpURLConnection
                conn.doInput = true
                conn.connect()
                val `is` = conn.inputStream
                val myBitmap = BitmapFactory.decodeStream(`is`)
                //设置固定大小
                //需要的大小
                val newWidth = 200f
                val newHeigth = 200f

                //图片大小
                val width = myBitmap.width
                val height = myBitmap.height

                //缩放比例
                val scaleWidth = newWidth / width
                val scaleHeigth = newHeigth / height
                val matrix = Matrix()
                matrix.postScale(scaleWidth, scaleHeigth)

                bitmap = Bitmap.createBitmap(myBitmap, 0, 0, width, height, matrix, true)
                `is`.close()
            } catch (e: OutOfMemoryError) {
                e.printStackTrace()
                bitmap = null
            } catch (e: IOException) {
                e.printStackTrace()
                bitmap = null
            }

            return bitmap
        }

        fun getBitmapFromUrl(url: String): Bitmap? {
            // 显示网络上的图片
            var bitmap: Bitmap? = null
            try {
                val myFileUrl = URL(url)
                val conn = myFileUrl.openConnection() as HttpURLConnection
                conn.doInput = true
                conn.connect()
                val `is` = conn.inputStream
                bitmap = BitmapFactory.decodeStream(`is`)
                `is`.close()
            } catch (e: OutOfMemoryError) {
                e.printStackTrace()
                bitmap = null
            } catch (e: IOException) {
                e.printStackTrace()
                bitmap = null
            }

            return bitmap
        }
    }

    interface CallBack {
        fun onRequestComplete(result: String?)
    }

}