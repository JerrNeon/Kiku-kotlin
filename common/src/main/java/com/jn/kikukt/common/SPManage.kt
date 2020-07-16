package com.jn.kikukt.common

import com.jn.kikukt.common.utils.SPUtils

/**
 * Author：Stevie.Chen Time：2019/7/15
 * Class Comment：
 */
class SPManage {

    companion object {
        private const val FIRST_GUIDE = "first_guide"//是否首次进入引导页
        private const val PUSH_ENABLE = "push_enable"//推送是否可用
        private const val SOUND_ENABLE = "sound_enable"//声音是否可用
        private const val SHAKE_ENABLE = "shake_enable"//震动是否可用
        private const val ALIAS_VALUE = "alias_value"//别名值
        private const val EXCEPTION_MESSAGE = "exception_message"//异常信息

        val instance: SPManage by lazy(mode = LazyThreadSafetyMode.NONE) {
            SPManage()
        }
    }

    var isFirstGuide: Boolean
        set(value) {
            SPUtils.put(FIRST_GUIDE, value)
        }
        get() {
            return SPUtils[FIRST_GUIDE, true]
        }

    var isPushEnable: Boolean
        set(value) {
            SPUtils.put(PUSH_ENABLE, value)
        }
        get() {
            return SPUtils[PUSH_ENABLE, true]
        }

    var isSoundEnable: Boolean
        set(value) {
            SPUtils.put(SOUND_ENABLE, value)
        }
        get() {
            return SPUtils[SOUND_ENABLE, true]
        }

    var isShakeEnable: Boolean
        set(value) {
            SPUtils.put(SHAKE_ENABLE, value)
        }
        get() {
            return SPUtils[SHAKE_ENABLE, true]
        }

    var alias: String
        set(value) {
            SPUtils.put(ALIAS_VALUE, value)
        }
        get() {
            return SPUtils[ALIAS_VALUE, ""]
        }

    var exceptionMessage: String
        set(value) {
            SPUtils.put(EXCEPTION_MESSAGE, value)
        }
        get() {
            return SPUtils[EXCEPTION_MESSAGE, ""]
        }
}