package com.jn.kikukt.common

import android.content.Context
import com.google.gson.JsonParseException
import com.jn.kikukt.common.utils.SPUtils
import com.jn.kikukt.common.utils.gson.JsonUtils

/**
 * Author：Stevie.Chen Time：2019/7/15
 * Class Comment：
 */
class SPManage {

    private lateinit var mContext: Context

    companion object {
        private const val USER_INFO = "user_info"//用户信息
        private const val FIRST_GUIDE = "first_guide"//是否首次进入引导页
        private const val PUSH_ENABLE = "push_enable"//推送是否可用
        private const val SOUND_ENABLE = "sound_enable"//声音是否可用
        private const val VIRBATE_ENABLE = "virbate_enable"//震动是否可用
        private const val MESSAGE_ENABLE = "message_enable"//消息是否可用
        private const val ALIAS_VALUE = "alias_value"//别名值
        private const val EXCEPTION_MESSAGE = "exception_message"//异常信息

        val instance: SPManage by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            SPManage()
        }
    }

    fun init(context: Context) {
        mContext = context.applicationContext
    }

    fun <T> getUserInfo(tClass: Class<T>): T? {
        var userInfo: T? = null
        try {
            userInfo = JsonUtils.instance.toObject(SPUtils[mContext, USER_INFO, ""] as String, tClass)
        } catch (e: NullPointerException) {
            e.printStackTrace()
        } catch (e: JsonParseException) {
            e.printStackTrace()
        }

        return userInfo
    }

    fun setUserInfo(userInfo: Any) {
        SPUtils.put(mContext, USER_INFO, JsonUtils.instance.toJson(userInfo))
    }

    fun clearUserInfo() {
        SPUtils.remove(mContext, USER_INFO)
    }

    fun getFirstGuide(): Boolean {
        return SPUtils[mContext, FIRST_GUIDE, true] as Boolean
    }

    fun setFirstGuide(isFirstGuide: Boolean) {
        SPUtils.put(mContext, FIRST_GUIDE, isFirstGuide)
    }

    fun getPushEnable(): Boolean {
        return SPUtils[mContext, PUSH_ENABLE, true] as Boolean
    }

    fun setPushEnable(pushEnable: Boolean) {
        SPUtils.put(mContext, PUSH_ENABLE, pushEnable)
    }

    fun isSoundEnable(): Boolean {
        return SPUtils[mContext, SOUND_ENABLE, true] as Boolean
    }

    fun setSoundEnable(soundEnable: Boolean) {
        SPUtils.put(mContext, SOUND_ENABLE, soundEnable)
    }

    fun isVirbateEnable(): Boolean {
        return SPUtils[mContext, VIRBATE_ENABLE, true] as Boolean
    }

    fun setVirbateEnable(virbateEnable: Boolean) {
        SPUtils.put(mContext, VIRBATE_ENABLE, virbateEnable)
    }

    fun isMessageEnable(): Boolean {
        return SPUtils[mContext, MESSAGE_ENABLE, true] as Boolean
    }

    fun setMessageEnable(messageEnable: Boolean) {
        SPUtils.put(mContext, MESSAGE_ENABLE, messageEnable)
    }

    fun getAliasValue(): String {
        return SPUtils[mContext, ALIAS_VALUE, ""] as String
    }

    fun setAliasValue(aliasValue: String) {
        SPUtils.put(mContext, ALIAS_VALUE, aliasValue)
    }

    fun getExceptionMessage(): String {
        return SPUtils.get(mContext, EXCEPTION_MESSAGE, "") as String
    }

    fun setExceptionMessage(exception_message: String) {
        SPUtils.put(mContext,
            EXCEPTION_MESSAGE, exception_message)
    }

    fun clearExceptionMessage() {
        SPUtils.remove(mContext, EXCEPTION_MESSAGE)
    }
}