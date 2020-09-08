package com.jn.kiku.ttp.analysis

import android.content.Context
import com.umeng.analytics.MobclickAgent
import com.umeng.analytics.MobclickAgent.EScenarioType
import com.umeng.commonsdk.UMConfigure

/**
 * Author：Stevie.Chen Time：2020/09/08 10:48
 * Class Comment：友盟统计管理
 */
object UMManage {
    /**
     * 初始化，在Application中使用
     *
     * @param context     上下文，不能为空
     * @param isLogEnable boolean 默认为false，如需查看LOG设置为true
     */
    fun init(context: Context?, isLogEnable: Boolean) {
        init(context, UMConfigure.DEVICE_TYPE_PHONE, null)
        setEncryptEnabled(true)
        setLogEnabled(isLogEnable)
    }

    /**
     * 设置场景类型为普通统计场景类型
     *
     * @param context
     */
    fun setScenarioType(context: Context?) {
        setScenarioType(context, EScenarioType.E_UM_NORMAL)
    }

    /**
     * 初始化，在Application中使用
     *
     *
     * 新版本中即使已经在AndroidManifest.xml中配置appkey和channel值，也需要在App代码中调用初始化接口
     * （如需要使用AndroidManifest.xml中配置好的appkey和channel值，UMConfigure.init调用中appkey和channel参数请置为null）：
     *
     *
     * @param context    上下文，不能为空
     * @param deviceType 设备类型，UMConfigure.DEVICE_TYPE_PHONE为手机、UMConfigure.DEVICE_TYPE_BOX为盒子，默认为手机
     * @param pushSecret Push推送业务的secret，需要集成Push功能时必须传入Push的secret，否则传空
     */
    fun init(context: Context?, deviceType: Int, pushSecret: String?) {
        UMConfigure.init(context, deviceType, pushSecret)
    }

    /**
     * 初始化，在Application中使用
     *
     *
     * 新版本中即使已经在AndroidManifest.xml中配置appkey和channel值，也需要在App代码中调用初始化接口
     * （如需要使用AndroidManifest.xml中配置好的appkey和channel值，UMConfigure.init调用中appkey和channel参数请置为null）：
     *
     *
     * @param context    上下文，不能为空
     * @param appKey     友盟 AppKey
     * @param channel    :友盟 Channel
     * @param deviceType 设备类型，UMConfigure.DEVICE_TYPE_PHONE为手机、UMConfigure.DEVICE_TYPE_BOX为盒子，默认为手机
     * @param pushSecret Push推送业务的secret，需要集成Push功能时必须传入Push的secret，否则传空
     */
    fun init(
        context: Context?,
        appKey: String?,
        channel: String?,
        deviceType: Int,
        pushSecret: String?
    ) {
        UMConfigure.init(context, appKey, channel, deviceType, pushSecret)
    }

    /**
     * 场景类型设置接口
     *
     * @param context
     * @param etype   EScenarioType.E_UM_NORMAL 普通统计场景类型  EScenarioType.E_UM_GAME 游戏场景类型
     */
    fun setScenarioType(context: Context?, etype: EScenarioType?) {
        MobclickAgent.setScenarioType(context, etype)
    }

    /**
     * 设置组件化的Log开关
     * 参数: boolean 默认为false，如需查看LOG设置为true
     */
    fun setLogEnabled(isLogEnable: Boolean) {
        UMConfigure.setLogEnabled(isLogEnable)
    }

    /**
     * 设置日志加密
     * 参数：boolean 默认为false（不加密）
     */
    fun setEncryptEnabled(isEncryptEnable: Boolean) {
        UMConfigure.setEncryptEnabled(isEncryptEnable)
    }

    /**
     * 设置Secret Key
     * 新增secret Key接口,防止appkey被盗用,secretkey网站申请
     *
     * @param context
     * @param secretkey
     */
    fun setSecret(context: Context?, secretkey: String?) {
        MobclickAgent.setSecret(context, secretkey)
    }

    /**
     * 统计应用时长的(也就是Session时长,当然还包括一些其他功能)
     *
     *
     * 必须调用 MobclickAgent.onResume() 和MobclickAgent.onPause()方法，
     * 才能够保证获取正确的新增用户、活跃用户、启动次数、使用时长等基本数据。
     *
     * @param context
     */
    fun onResume(context: Context?) {
        MobclickAgent.onResume(context)
    }

    /**
     * 统计应用时长的(也就是Session时长,当然还包括一些其他功能)
     *
     * 必须调用 MobclickAgent.onResume() 和MobclickAgent.onPause()方法，
     * 才能够保证获取正确的新增用户、活跃用户、启动次数、使用时长等基本数据。
     */
    fun onPause(context: Context?) {
        MobclickAgent.onPause(context)
    }

    /**
     * 统计页面信息
     *
     *
     * 在onResume中调用
     *
     *
     * @param className
     */
    fun onPageStart(className: String?) {
        MobclickAgent.onPageStart(className)
    }

    /**
     * 统计页面信息
     *
     *
     * 在onPause中调用
     *
     *
     * @param className
     */
    fun onPageEnd(className: String?) {
        MobclickAgent.onPageEnd(className)
    }

    fun onResumeActivity(context: Context?, className: String?) {
        onPageStart(className) // [统计页面(仅有Activity的应用中SDK自动调用,不需要单独写。参数为页面名称,可自定义)]
        onResume(context) //友盟统计，所有Activity中添加，父类添加后子类不用重复添加
    }

    fun onPauseActivity(context: Context?, className: String?) {
        onPageEnd(className) // [统计页面(仅有Activity的应用中SDK自动调用,不需要单独写。参数为页面名称,可自定义)]
        onPause(context) //友盟统计，所有Activity中添加，父类添加后子类不用重复添加
    }

    fun onResumeFragment(className: String?) {
        onPageStart(className) // [统计页面(仅有Activity的应用中SDK自动调用,不需要单独写。参数为页面名称,可自定义)]
    }

    fun onPauseFragment(className: String?) {
        onPageEnd(className) // [统计页面(仅有Activity的应用中SDK自动调用,不需要单独写。参数为页面名称,可自定义)]
    }

    /**
     * 如果开发者调用kill或者exit之类的方法杀死进程，请务必在此之前调用onKillProcess(Context context)方法，用来保存统计数据。
     */
    fun onKillProcess(context: Context?) {
        MobclickAgent.onKillProcess(context)
    }

    /**
     * 获取测试设备信息
     */
    fun getTestDeviceInfo(context: Context?): Array<String> {
        return UMConfigure.getTestDeviceInfo(context)
    }
}