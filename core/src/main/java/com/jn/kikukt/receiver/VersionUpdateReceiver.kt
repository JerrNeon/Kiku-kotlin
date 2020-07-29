package com.jn.kikukt.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

/**
 * Author：Stevie.Chen Time：2019/7/12
 * Class Comment：版本更新广播
 */
class VersionUpdateReceiver(private val onReceive: ((context: Context, intent: Intent) -> Unit)? = null) :
    BroadcastReceiver() {

    companion object {
        const val VERSION_UPDATE_ACTION = "com.jn.kikukt.VersionUpdate"//版本更新广播Action
    }

    override fun onReceive(context: Context, intent: Intent) {
        onReceive?.invoke(context, intent)
    }
}