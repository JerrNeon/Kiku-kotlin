package com.jn.kikukt.common


import android.app.Activity
import android.app.Application
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

    fun register(application: Application, callback: ActivityManagerLifecycleCallbackImpl) {
        application.registerActivityLifecycleCallbacks(callback)
    }

    fun register(application: Application) {
        register(application, ActivityManagerLifecycleCallbackImpl())
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
        val pop = mActivityStack.pop()
        if (!pop.isFinishing) {
            pop.finish()
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