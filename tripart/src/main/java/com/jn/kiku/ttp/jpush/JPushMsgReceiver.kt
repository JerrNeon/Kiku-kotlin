package com.jn.kiku.ttp.jpush

import android.app.Notification
import android.content.Context
import android.util.Log
import cn.jpush.android.api.CustomMessage
import cn.jpush.android.api.NotificationMessage
import cn.jpush.android.service.JPushMessageReceiver

/**
 * Author：Stevie.Chen Time：2020/09/08 10:55
 * Class Comment：极光推送消息处理
 */
class JPushMsgReceiver : JPushMessageReceiver() {
    companion object {
        private const val TAG = "JPushMsgReceiver"
    }

    override fun onRegister(context: Context, s: String) {
        super.onRegister(context, s)
        logI("[MyReceiver] 接收Registration Id : $s")
    }

    override fun getNotification(
        context: Context,
        notificationMessage: NotificationMessage
    ): Notification {
        logI("收到了通知")
        return super.getNotification(context, notificationMessage)
    }

    override fun onMessage(context: Context, customMessage: CustomMessage) {
        super.onMessage(context, customMessage)
        logI("收到了自定义消息。消息内容是：$customMessage")
    }

    override fun onNotifyMessageOpened(context: Context, notificationMessage: NotificationMessage) {
        super.onNotifyMessageOpened(context, notificationMessage)
        logI("用户点击打开了通知。消息内容是：$notificationMessage")
        // 在这里可以自己写代码去定义用户点击后的行为
    }

    override fun onNotifyMessageArrived(
        context: Context,
        notificationMessage: NotificationMessage
    ) {
        super.onNotifyMessageArrived(context, notificationMessage)
        logI("通知已到达。消息内容是：$notificationMessage")
    }

    override fun onNotifyMessageDismiss(
        context: Context,
        notificationMessage: NotificationMessage
    ) {
        super.onNotifyMessageDismiss(context, notificationMessage)
        logI("通知已消失。消息内容是：$notificationMessage")
    }

    private fun logI(msg: String) {
        Log.i(TAG, msg)
    }
}