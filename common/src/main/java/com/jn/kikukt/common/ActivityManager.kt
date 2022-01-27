package com.jn.kikukt.common


import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.jn.kikukt.common.utils.log
import java.util.*

/**
 * Author：Stevie.Chen Time：2019/7/15
 * Class Comment：Activity统一管理
 */
class ActivityManager {

    private val mActivityStack = Stack<Activity>()

    companion object {
        val instance: ActivityManager by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            ActivityManager()
        }
    }

    fun register(
        application: Application,
        callback: Application.ActivityLifecycleCallbacks = ActivityManagerLifecycleCallbackImpl()
    ) {
        application.registerActivityLifecycleCallbacks(callback)
    }

    fun addActivity(activity: Activity) {
        mActivityStack.push(activity)
    }

    fun removeActivity(activity: Activity) {
        mActivityStack.remove(activity)
    }

    /**
     * finish对应class的所有activity
     *
     * @param cls 要关闭的Activity Class
     */
    fun finishActivity(cls: Class<out Activity>) {
        for (i in mActivityStack.indices.reversed()) {
            val activity = mActivityStack[i]
            if (cls.name == activity.javaClass.name) {
                finishActivity(activity)
                break
            }
        }
    }

    /**
     * finish对应class的所有activity
     *
     * @param cls 要关闭的Activity Class
     */
    fun finishActivity(vararg cls: Class<out Activity>) {
        for (i in mActivityStack.indices.reversed()) {
            val activity = mActivityStack[i]
            for (c in cls) {
                if (c.name == activity.javaClass.name) {
                    finishActivity(activity)
                }
            }
        }
    }


    /**
     * 关闭栈顶的Activity
     */
    fun finishTopActivity() {
        try {
            val pop = mActivityStack.pop()
            if (!pop.isFinishing) {
                pop.finish()
            }
        } catch (e: Exception) {
            e.log()
        }
    }


    /**
     * finish除白名单以外的所有activity
     *
     * @param activityWhitelist 要保留的activity
     */
    fun finishAllActivityByWhitelist(vararg activityWhitelist: Class<out Activity>) {
        for (i in mActivityStack.indices.reversed()) {
            val activity = mActivityStack[i]
            for (c in activityWhitelist) {
                if (c.name == activity.javaClass.name) {
                    break
                } else {
                    finishActivity(activity)
                }
            }
        }
    }


    /**
     * finish所有activity
     */
    fun finishAllActivity() {
        for (i in mActivityStack.indices.reversed()) {
            val activity = mActivityStack[i]
            finishActivity(activity)
        }
    }


    private fun finishActivity(activity: Activity?) {
        if (activity != null) {
            if (!activity.isFinishing) {
                activity.finish()
                mActivityStack.remove(activity)
            }
        }
    }

    fun getActivityStack(): Stack<Activity> {
        return mActivityStack
    }

    /**
     * 获取栈顶的Activity
     */
    fun getTopActivity(): Activity? {
        return try {
            mActivityStack.lastElement()
        } catch (e: Exception) {
            e.log()
            null
        }
    }

    /**
     * @param activity
     * @return 是否存在此activity
     */
    fun isContainsActivity(activity: Activity): Boolean {
        return mActivityStack.contains(activity)
    }

    /**
     * @return 已经打开activity的数量
     */
    fun getActivityCount(): Int {
        return mActivityStack.size
    }
}

class ActivityManagerLifecycleCallbackImpl : Application.ActivityLifecycleCallbacks {
    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        ActivityManager.instance.addActivity(activity)
    }

    override fun onActivityStarted(activity: Activity) {
    }

    override fun onActivityResumed(activity: Activity) {
    }

    override fun onActivityPaused(activity: Activity) {
    }

    override fun onActivityStopped(activity: Activity) {
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
    }

    override fun onActivityDestroyed(activity: Activity) {
        ActivityManager.instance.removeActivity(activity)
    }
}