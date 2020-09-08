package com.jn.kiku.ttp.analysis

import android.content.Context
import com.zhuge.analysis.stat.ZhugeSDK
import com.zhuge.analysis.stat.ZhugeSDK.ZhugeJS
import org.json.JSONObject
import java.util.*

/**
 * Author：Stevie.Chen Time：2020/09/08 10:48
 * Class Comment：诸葛IO管理
 */
object ZhugeManage {
    fun init(context: Context) {
        ZhugeSDK.getInstance().init(context.applicationContext)
    }

    fun flush(context: Context) {
        ZhugeSDK.getInstance().flush(context.applicationContext)
    }

    /**
     * 设置数据上传服务器地址
     */
    fun setUploadURL(url: String?) {
        ZhugeSDK.getInstance().setUploadURL(url!!, null)
    }

    fun openDebug() {
        ZhugeSDK.getInstance().openDebug()
    }

    fun openLog() {
        ZhugeSDK.getInstance().openLog()
    }

    /**
     * 设置日志级别
     *
     * @param logLevel Log.VERBOSE/Log.DEBUG/Log.ERROR/Log.ASSERT/Log.WARN
     */
    fun setLogLevel(logLevel: Int) {
        ZhugeSDK.getInstance().setLogLevel(logLevel)
    }

    /**
     * 标识用户
     *
     * @param context Context
     * @param userId     用户ID
     * @param jsonObject 用户属性
     */
    fun identify(context: Context, userId: String?, jsonObject: JSONObject?) {
        ZhugeSDK.getInstance().identify(context.applicationContext, userId, jsonObject)
    }

    /**
     * 标识用户
     *
     * @param context Context
     * @param userId  用户ID
     * @param hashMap 用户属性
     */
    fun identify(context: Context, userId: String?, hashMap: HashMap<String?, Any?>?) {
        ZhugeSDK.getInstance().identify(context.applicationContext, userId, hashMap)
    }

    /**
     * 记录事件
     * 只统计事件的次数
     *
     * @param context Context
     * @param trackName 事件名称
     */
    fun track(context: Context, trackName: String?) {
        ZhugeSDK.getInstance().track(context.applicationContext, trackName)
    }

    /**
     * 记录事件
     *
     * @param context Context
     * @param trackName  事件名称
     * @param jsonObject 事件属性
     */
    fun track(context: Context, trackName: String?, jsonObject: JSONObject?) {
        ZhugeSDK.getInstance().track(context.applicationContext, trackName, jsonObject)
    }

    /**
     * 记录事件
     *
     * @param context Context
     * @param trackName 事件名称
     * @param hashMap   事件属性
     */
    fun track(context: Context, trackName: String?, hashMap: HashMap<String?, Any?>?) {
        ZhugeSDK.getInstance().track(context.applicationContext, trackName, hashMap)
    }

    /**
     * 时长事件的统计(来开始一个事件的统计)
     *
     *
     * 若您希望统计⼀一个事件发生的时长，比如视频的播放，页面的停留，那么可以调用如下接口来进行
     *
     *
     * 注意： startTrack()与endTrack()必须成对出现（eventName⼀一致），单独调⽤用⼀一个接⼝口是⽆无效的
     *
     * @param eventName 事件的名称
     */
    fun startTrack(eventName: String?) {
        ZhugeSDK.getInstance().startTrack(eventName)
    }

    /**
     * 调⽤用 endTrack() 来记录事件的持续时长。调用 endTrack() 之前，相同eventName的事件必须
     * 已经调用过 startTrack() ，否则这个接口并不不会产生任何事件。
     *
     *
     * 注意： startTrack()与endTrack()必须成对出现（eventName⼀一致），单独调⽤用⼀一个接⼝口是⽆无效的
     *
     * @param eventName 事件名
     * @param jsonObject 信息体
     */
    fun endTrack(eventName: String?, jsonObject: JSONObject?) {
        ZhugeSDK.getInstance().endTrack(eventName, jsonObject)
    }

    /**
     * @return 获取当前设备在诸葛体系下的设备标识
     */
    val did: String
        get() = ZhugeSDK.getInstance().did

    /**
     * @return 获得当前应用所属的会话ID
     */
    val sid: Long
        get() = ZhugeSDK.getInstance().sid

    /**
     * 事件自定义属性
     *
     *
     * 若有⼀一些属性对于您来说，每⼀一个事件都要拥有，那么您可以调用 setSuperProperty() 将它传⼊入。之
     * 后，每⼀一个经过track(),endTrack()传入的事件，都将自动获得这些属性。
     *
     * @param jsonObject 信息体
     */
    fun setSuperProperty(jsonObject: JSONObject?) {
        ZhugeSDK.getInstance().setSuperProperty(jsonObject)
    }

    /**
     * 设备⾃自定义属性
     *
     *
     * 诸葛默认展示的设备信息包含一些硬件信息，如系统版本，设备分辨率，设备制造商等。若您希望在展示设
     * 备信息时展示一些额外的 Context信息，那么可以调⽤用 setPlatform() 传入，我们会将这些信息添加在设备信息中。
     *
     * @param jsonObject 信息体
     */
    fun setPlatform(jsonObject: JSONObject?) {
        ZhugeSDK.getInstance().setPlatform(jsonObject)
    }

    /**
     * 获得WebView中的ZhuGeJS
     *
     * @return
     */
    val zhugeJS: ZhugeJS
        get() = ZhugeJS()
}