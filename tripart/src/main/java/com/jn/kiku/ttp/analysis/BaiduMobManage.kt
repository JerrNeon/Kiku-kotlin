package com.jn.kiku.ttp.analysis;

import android.content.Context;

import com.baidu.mobstat.StatService;

/**
 * Author：Stevie.Chen Time：2020/09/08 10:48
 * Class Comment：百度统计
 */
object BaiduMobManage {

    /**
     * 由于多进程等可能造成Application多次执行，建议此代码不要埋点在Application中，否则可能造成启动次数偏高
     * 建议此代码埋点在统计路径触发的第一个页面中，若可能存在多个则建议都埋点
     */
    fun start(context: Context) {
        StatService.start(context)
    }

    /**
     * 开发时调用，建议上线前关闭，以免影响性能
     *
     * @param isDebugEnable
     */
    fun setDebugOn(isDebugEnable: Boolean) {
        StatService.setDebugOn(isDebugEnable)
    }

    /**
     * 获取测试设备ID
     */
    fun getTestDeviceId(context: Context): String {
        return StatService.getTestDeviceId(context)
    }
}
