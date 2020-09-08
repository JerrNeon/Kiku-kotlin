package com.jn.kiku.ttp.bug

import android.app.Activity
import android.app.Application
import android.view.MotionEvent
import androidx.fragment.app.Fragment
import com.bugtags.library.Bugtags
import com.jn.kiku.ttp.TtpConstants

/**
 * Author：Stevie.Chen Time：2020/09/08 10:48
 * Class Comment：Bugtags
 */
object BugtagsManage {
    /**
     * 在Application初始化
     */
    fun init(application: Application?) {
        start(TtpConstants.BUGTAGS_APPKEY, application, Bugtags.BTGInvocationEventNone)
    }

    /**
     * 在Application初始化
     *
     * @param event       BTGInvocationEventNone | BTGInvocationEventShake | BTGInvocationEventBubble
     *
     *
     * BTGInvocationEventNone    // 静默模式，只收集 Crash 信息（如果允许，默认为允许）
     * BTGInvocationEventShake   // 通过摇一摇呼出 Bugtags
     * BTGInvocationEventBubble  // 通过悬浮小球呼出 Bugtags
     *
     */
    fun start(appKey: String?, application: Application?, event: Int) {
        Bugtags.start(appKey, application, event)
    }

    /**
     * 回调 1
     */
    fun onResume(activity: Activity?) {
        Bugtags.onResume(activity)
    }

    /**
     * 回调 2
     */
    fun onPause(activity: Activity?) {
        Bugtags.onPause(activity)
    }

    /**
     * 回调 3
     */
    fun dispatchTouchEvent(activity: Activity?, event: MotionEvent?) {
        Bugtags.onDispatchTouchEvent(activity, event)
    }

    /**
     * 回调 1
     *
     */
    fun onResume(fragment: Fragment?) {
        Bugtags.onResume(fragment)
    }

    /**
     * 回调 2
     *
     */
    fun onPause(fragment: Fragment?) {
        Bugtags.onPause(fragment)
    }
}