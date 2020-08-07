package com.jn.kikukt.common.leak

import android.app.Activity
import android.os.Handler
import android.os.Message
import androidx.activity.ComponentActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner

/**
 * Author：Stevie.Chen Time：2020/8/7
 * Class Comment：避免内存泄漏的[Handler]
 */
class WeakHandler : Handler {

    private var activity: Activity? by Weak()
    private var fragment: Fragment? by Weak()
    private var activityHandleMessage: ((activity: Activity, msg: Message) -> Unit)? = null
    private var fragmentHandleMessage: ((fragment: Fragment, msg: Message) -> Unit)? = null

    constructor(
        activity: ComponentActivity,
        handleMessage: ((activity: Activity, msg: Message) -> Unit)? = null
    ) {
        this.activity = activity
        activityHandleMessage = handleMessage
        onDestroy(activity.lifecycle)
    }

    constructor(
        fragment: Fragment,
        handleMessage: ((fragment: Fragment, msg: Message) -> Unit)? = null
    ) {
        this.fragment = fragment
        fragmentHandleMessage = handleMessage
        onDestroy(fragment.lifecycle)
    }

    override fun handleMessage(msg: Message) {
        super.handleMessage(msg)
        activity?.let {
            activityHandleMessage?.invoke(it, msg)
        }
        fragment?.let {
            fragmentHandleMessage?.invoke(it, msg)
        }
    }

    private fun onDestroy(lifecycle: Lifecycle) {
        lifecycle.addObserver(object : LifecycleEventObserver {
            override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                if (Lifecycle.Event.ON_DESTROY == event) {
                    removeCallbacksAndMessages(null)
                }
            }
        })
    }
}