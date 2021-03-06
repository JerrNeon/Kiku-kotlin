package com.jn.kikukt.net.retrofit.exception

import android.content.Context
import com.jn.kikukt.common.utils.isConnected
import com.jn.kikukt.common.utils.logE
import com.jn.kikukt.net.R
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException

/**
 * Author：Stevie.Chen Time：2019/7/15
 * Class Comment：错误隐藏帮助类
 */
object OkHttpErrorHelper {

    fun getMessage(error: Throwable, context: Context): String? {
        if (!context.isConnected()) { // 无网络连接
            return context.resources.getString(R.string.no_internet)
        } else if (error is OkHttpException) {
            return error.errorMsg ?: ""//服务器返回错误信息
        } else if (error is ConnectException) {
            return context.resources.getString(R.string.generic_server_down)   //可能是连接服务器失败
        } else if (error is SocketTimeoutException) {
            return context.resources.getString(R.string.connect_time_out)  //连接超时
        } else if (error is com.google.gson.JsonParseException) {
            "OkHttpErrorHelper--->" + context.resources.getString(R.string.json_paser_error).logE()
            return ""
        } else if ("java.net.SocketException: Socket closed" == error.toString() || "java.io.IOException: Canceled" == error.toString()) {
            //这里应该是用户取消请求抛出的异常,其他情况可能也会抛出此异常，上面大多数判断可以处理
            "OkHttpErrorHelper--->" + context.resources.getString(R.string.user_cancel).logE()
            return ""
        } else if (error is IOException) {
            return ""
        }
        return context.resources.getString(R.string.generic_error)
    }

    /**
     * 是否取消了请求
     *
     * @param error
     * @return
     */
    fun isCancelRequest(error: Throwable): Boolean {
        //这里应该是用户取消请求抛出的异常,其他情况可能也会抛出此异常，上面大多数判断可以处理
        return "java.net.SocketException: Socket closed" == error.toString() || "java.io.IOException: Canceled" == error.toString()
    }

}