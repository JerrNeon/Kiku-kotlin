package com.jn.kikukt.receiver

import android.content.Context
import android.content.Intent

/**
 * Author：Stevie.Chen Time：2019/7/12
 * Class Comment：广播回调
 */
interface IReceiverListener {

    fun onReceive(context: Context, intent: Intent)
}