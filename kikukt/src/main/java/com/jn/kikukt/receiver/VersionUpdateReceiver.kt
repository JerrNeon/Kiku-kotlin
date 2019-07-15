package com.jn.kikukt.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

/**
 * Author：Stevie.Chen Time：2019/7/12
 * Class Comment：版本更新广播
 */
class VersionUpdateReceiver(iReceiverListener: IReceiverListener?) : BroadcastReceiver() {

    companion object {
        const val VERSION_UPDATE_ACTION = "com.jn.kikukt.VersionUpdate"//版本更新广播Action
    }

    private val mIReceiverListener: IReceiverListener? = iReceiverListener

    override fun onReceive(context: Context, intent: Intent) {
        mIReceiverListener?.onReceive(context, intent)
    }
}