package com.jn.kikukt.common

import android.app.Activity
import android.app.Application
import android.os.Bundle

/**
 * Author：Stevie.Chen Time：2019/7/15
 * Class Comment：
 */
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