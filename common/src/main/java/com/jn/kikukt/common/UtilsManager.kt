package com.jn.kikukt.common

import android.app.Application
import com.jn.kikukt.common.utils.ContextUtils
import com.jn.kikukt.common.utils.LogUtils

/**
 * Author：Stevie.Chen Time：2019/7/15
 * Class Comment：工具管理
 */
object UtilsManager {

    /**
     * 初始化Log
     *
     * @param logEnable 是否开启日志，建议debug开启日志信息，release关闭日志信息
     * @param tagName   Tag名称
     */
    fun initLogUtils(logEnable: Boolean, tagName: String) {
        LogUtils.LOG_ENABLE = logEnable
        LogUtils.TAG = tagName
    }

    /**
     * 初始化Context
     */
    fun initContextUtils(application: Application) {
        ContextUtils.application = application
    }

}