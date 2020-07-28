package com.jn.kikukt.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.jn.kikukt.activity.ProcessAliveActivity

/**
 * Author：Stevie.Chen Time：2019/7/12
 * Class Comment：进程保活需要用到的系统广播
 */
open class ProcessAliveReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        //利用系统广播Intent.ACTION_TIME_TICK 每隔一分钟检测一次Service的运行状态
        when (intent.action) {
            Intent.ACTION_TIME_TICK -> checkProcessAliveService(context)
            Intent.ACTION_SCREEN_ON -> {//屏幕亮
                context.sendBroadcast(Intent(ProcessAliveActivity.KIKUKT_FINISH_ACTION))
                val it = Intent(Intent.ACTION_MAIN)
                it.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                it.addCategory(Intent.CATEGORY_HOME)
                context.startActivity(it)
            }
            Intent.ACTION_SCREEN_OFF -> {//屏幕熄
                val it = Intent(context, ProcessAliveActivity::class.java)
                it.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                context.startActivity(it)
            }
        }
    }

    /**
     * 检查进程保活服务
     *
     * @param context Context
     */
    open fun checkProcessAliveService(context: Context) {

    }
}