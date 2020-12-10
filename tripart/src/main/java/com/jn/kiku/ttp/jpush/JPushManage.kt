package com.jn.kiku.ttp.jpush

import android.app.Activity
import android.app.Notification
import android.content.Context
import android.os.Handler
import android.os.Message
import cn.jpush.android.api.BasicPushNotificationBuilder
import cn.jpush.android.api.DefaultPushNotificationBuilder
import cn.jpush.android.api.JPushInterface
import cn.jpush.android.api.TagAliasCallback
import com.jn.kikukt.common.SPManage
import com.jn.kikukt.common.utils.ContextUtils
import com.jn.kikukt.common.utils.logE
import com.jn.kikukt.common.utils.logI

/**
 * Author：Stevie.Chen Time：2020/09/08 10:55
 * Class Comment：JPushManage管理类
 */
object JPushManage {
    private const val MSG_SET_ALIAS = 1001

    private val mContext: Context
        get() = ContextUtils.context

    enum class JPushType {
        SOUND, VIBRATE, All, NONE
    }

    /**
     * 设置别名
     *
     * @param alias 别名
     */
    fun setAlias(alias: String) {
        val aliasValue: String = SPManage.instance.alias
        if (alias == aliasValue) {
            "当前用户已设置过别名".logI()
            return
        }
        // 调用 Handler 来异步设置别名
        mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_ALIAS, alias))
    }

    /**
     * @param alias    null 此次调用不设置此值。（注：不是指的字符串"null"）"" （空字符串）表示取消之前的设置。
     * 每次调用设置有效的别名，覆盖之前的设置。
     * 有效的别名组成：字母（区分大小写）、数字、下划线、汉字、特殊字符(v2.1.6支持)@!#$&*+=.|。
     * 限制：alias 命名长度限制为 40 字节。（判断长度需采用UTF-8编码）
     * @param tags     null 此次调用不设置此值。（注：不是指的字符串"null")空数组或列表表示取消之前的设置。
     * 每次调用至少设置一个 tag，覆盖之前的设置，不是新增。
     * 有效的标签组成：字母（区分大小写）、数字、下划线、汉字、特殊字符(v2.1.6支持)@!#$&*+=.|。
     * 限制：每个 tag 命名长度限制为 40 字节，最多支持设置 1000 个 tag，但总长度不得超过7K字节。
     * （判断长度需采用UTF-8编码）
     * @param callback 在TagAliasCallback 的 gotResult 方法，返回对应的参数 alias, tags。
     * 并返回对应的状态码：0为成功，其他返回码请参考错误码定义。
     */
    fun setAliasAndTags(alias: String?, tags: Set<String?>?, callback: TagAliasCallback?) {
        JPushInterface.setAliasAndTags(mContext, alias, tags, callback)
    }

    /**
     * 过滤掉无效的 tags，得到有效的 tags
     *
     * @param tags
     * @return
     */
    fun filterValidTag(tags: Set<String?>?): Set<String> {
        return JPushInterface.filterValidTags(tags)
    }

    /**
     * 取得应用程序对应的 RegistrationID。 只有当应用程序成功注册到 JPush 的服务器时才返回对应的值，否则返回空字符串
     *
     * @return
     */
    val registrationID: String
        get() = JPushInterface.getRegistrationID(mContext)

    /**
     * 清除所有通知
     */
    fun clearAllNotifications() {
        JPushInterface.clearAllNotifications(mContext)
    }

    /**
     * 通过通知ID清除通知
     */
    fun clearNotificationById(notificationId: Int) {
        JPushInterface.clearNotificationById(mContext, notificationId)
    }

    /**
     * 设置允许推送时间
     *
     * @param weekDays  0表示星期天，1表示星期一，以此类推。 （7天制，Set集合里面的int范围为0到6）
     * 新功能:set的值为null,则任何时间都可以收到消息和通知，set的size为0，则表示任何时间都收不到消息和通知.
     * @param startHour 允许推送的开始时间 （24小时制：startHour的范围为0到23）
     * @param endHour   允许推送的结束时间 （24小时制：endHour的范围为0到23）
     */
    fun setPushTime(weekDays: Set<Int?>?, startHour: Int, endHour: Int) {
        JPushInterface.setPushTime(mContext, weekDays, startHour, endHour)
    }

    /**
     * 设置通知静默时间
     *
     * @param startHour   静音时段的开始时间 - 小时 （24小时制，范围：0~23 ）
     * @param startMinute 静音时段的开始时间 - 分钟（范围：0~59 ）
     * @param endHour     静音时段的结束时间 - 小时 （24小时制，范围：0~23 ）
     * @param endMinute   静音时段的结束时间 - 分钟（范围：0~59 ）
     */
    fun setSilenceTime(startHour: Int, startMinute: Int, endHour: Int, endMinute: Int) {
        JPushInterface.setSilenceTime(mContext, startHour, startMinute, endHour, endMinute)
    }

    /**
     * 设置保留最近通知条数
     * 默认为保留最近 5 条通知
     *
     * @param maxNum 最多显示的条数
     */
    fun setLatestNotificationNumber(maxNum: Int) {
        JPushInterface.setLatestNotificationNumber(mContext, maxNum)
    }
    /* ==================================静态方法================================== */
    /**
     * 在 Android 6.0 及以上的系统上，需要去请求一些用到的权限，
     * JPush SDK 用到的一些需要请求如下权限，因为需要这些权限使统计更加精准，
     * 功能更加丰富，建议开发者调用
     *
     *
     * "android.permission.READ_PHONE_STATE"
     * "android.permission.WRITE_EXTERNAL_STORAGE"
     * "android.permission.READ_EXTERNAL_STORAGE"
     * "android.permission.ACCESS_FINE_LOCATION"
     *
     * @param activity 当前应用的 Activity 的上下文
     */
    fun requestPermission(activity: Activity?) {
        JPushInterface.requestPermission(activity)
    }

    /**
     * 恢复推送服务
     */
    fun resumePush() {
        if (JPushInterface.isPushStopped(mContext)) JPushInterface.resumePush(mContext)
    }

    /**
     * 停止推送服务
     */
    fun stopPush() {
        if (!JPushInterface.isPushStopped(mContext)) JPushInterface.stopPush(mContext)
    }

    /**
     * 定制通知栏样式
     *
     * @param integer 样式编号
     */
    fun setPushNotificationBuilder(integer: Int?, builder: DefaultPushNotificationBuilder?) {
        JPushInterface.setPushNotificationBuilder(integer, builder)
    }

    /**
     * 推送声音、震动相关设置
     *
     * @param type SOUND：只有声音,VIBRATE:只有震动,All:声音震动都有,NONE:声音震动都没有
     */
    fun setPushHardware(activity: Activity?, type: JPushType?) {
        val builder = BasicPushNotificationBuilder(activity)
        when (type) {
            JPushType.SOUND -> builder.notificationDefaults =
                Notification.DEFAULT_SOUND or Notification.DEFAULT_LIGHTS
            JPushType.VIBRATE -> builder.notificationDefaults =
                Notification.DEFAULT_VIBRATE or Notification.DEFAULT_LIGHTS
            JPushType.All -> builder.notificationDefaults =
                Notification.DEFAULT_SOUND or Notification.DEFAULT_VIBRATE or Notification.DEFAULT_LIGHTS
            JPushType.NONE -> builder.notificationDefaults = Notification.DEFAULT_LIGHTS
            else -> {
            }
        }
        setPushNotificationBuilder(1, builder)
    }

    /**
     * 推送声音、震动相关设置
     *
     * @param isSound   是否有声音
     * @param isVirbate 是否有震动
     */
    fun setPushHardware(activity: Activity?, isSound: Boolean, isVirbate: Boolean) {
        if (isSound && !isVirbate) setPushHardware(
            activity,
            JPushType.SOUND
        ) else if (isVirbate && !isSound) setPushHardware(
            activity,
            JPushType.VIBRATE
        ) else if (isVirbate) setPushHardware(
            activity,
            JPushType.All
        ) else setPushHardware(activity, JPushType.NONE)
    }

    fun onResume(activity: Activity?) {
        JPushInterface.onResume(activity)
    }

    fun onPause(activity: Activity?) {
        JPushInterface.onPause(activity)
    }

    private val mAliasCallback: TagAliasCallback = TagAliasCallback { code, alias, _ ->
        when (code) {
            0 -> {
                "Set tag and alias success".logI()
                // 建议这里往 SharePreference 里写一个成功设置的状态。成功设置一次后，以后不必再次设置了。
                SPManage.instance.alias = alias
            }
            6002 -> {
                "Failed to set alias and tags due to timeout. Try again after 60s.".logI()
                // 延迟 60 秒来调用 Handler 设置别名
                this.mHandler.sendMessageDelayed(
                    mHandler.obtainMessage(MSG_SET_ALIAS, alias),
                    1000 * 60.toLong()
                )
            }
            else -> "Failed with errorCode = $code".logE()
        }
    }
    private val mHandler = Handler { msg: Message ->
        if (msg.what == MSG_SET_ALIAS) {
            "Set alias in handler.".logI()
            // 调用 JPush 接口来设置别名。
            setAliasAndTags(msg.obj as String, null, this.mAliasCallback)
        } else {
            "Unhandled msg - ${msg.what}".logI()
        }
        false
    }

}