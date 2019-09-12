package com.jn.kikukt.utils

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import com.jn.kikukt.receiver.ProcessAliveReceiver
import com.jn.kikukt.service.ProcessAliveService

/**
 * Author：Stevie.Chen Time：2019/7/15
 * Class Comment：Android组件管理
 */
object ComponentManager {

    /**
     * 注册进程保活需要用到的系统广播
     *
     *
     * 此广播只能动态注册
     *
     *
     * @param context Context
     */
    fun registerProcessAliveReceiver(context: Context, receiver: ProcessAliveReceiver?) {
        val intentFilter = IntentFilter()
        intentFilter.addAction(Intent.ACTION_TIME_TICK)//时间变化
        intentFilter.addAction(Intent.ACTION_SCREEN_ON)//亮屏
        intentFilter.addAction(Intent.ACTION_SCREEN_OFF)//息屏
        intentFilter.addAction(Intent.ACTION_USER_PRESENT)//
        if (receiver != null)
            context.registerReceiver(receiver, intentFilter)
    }

    /**
     * 开启进程保活服务
     *
     * @param context Context
     * @param service ProcessAliveService
     */
    fun startProcessAliveService(context: Context, service: Class<out ProcessAliveService>) {
        val intent = Intent(context, service)
        context.startService(intent)
    }

    /**
     * 停止进程保活服务
     *
     * @param context Context
     * @param service ProcessAliveService
     */
    fun stopProcessAliveService(context: Context, service: Class<out ProcessAliveService>) {
        val intent = Intent(context, service)
        context.stopService(intent)
    }
}