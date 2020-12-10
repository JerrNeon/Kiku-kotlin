package com.jn.kiku.ttp

import android.content.Context
import cn.jpush.android.api.JPushInterface
import com.baidu.mapapi.SDKInitializer
import com.umeng.analytics.MobclickAgent
import com.umeng.commonsdk.UMConfigure

/**
 * Author：Stevie.Chen Time：2020/09/08 11:50
 * Class Comment：第三方平台管理
 */
object TtpManager {

    /**
     * 初始化百度地图SDK
     *
     * @param context Context
     */
    fun initBaiduMap(context: Context?) {
        SDKInitializer.initialize(context);
    }

    /**
     * 初始化极光推送SDK
     *
     * @param context   Context
     * @param LOG_DEBUG 是否开启日志，建议debug下才开启
     */
    fun initJPush(context: Context, LOG_DEBUG: Boolean) {
        JPushInterface.setDebugMode(LOG_DEBUG)
        JPushInterface.init(context)
    }

    /**
     * 初始化新浪回调地址
     *
     * @param redirectUrl 回调地址
     */
    fun initSina(redirectUrl: String) {
        TtpConstants.SINA_REDIRECT_URL = redirectUrl
    }

    /**
     * 初始化友盟
     */
    fun initUM(context: Context?, appKey: String?) {
        UMConfigure.init(context, appKey, "Umeng", UMConfigure.DEVICE_TYPE_PHONE, null)
        MobclickAgent.setPageCollectionMode(MobclickAgent.PageMode.AUTO)
    }
}