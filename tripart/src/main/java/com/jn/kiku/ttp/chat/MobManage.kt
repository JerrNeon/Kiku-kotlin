package com.jn.kiku.ttp.chat

import android.app.Activity
import android.content.Context
import com.hyphenate.chat.ChatClient
import com.hyphenate.chat.ChatClient.ConnectionListener
import com.hyphenate.chat.ChatManager.MessageListener
import com.hyphenate.chat.Message
import com.hyphenate.helpdesk.callback.Callback
import com.hyphenate.helpdesk.callback.ValueCallBack
import com.hyphenate.helpdesk.easeui.util.IntentBuilder
import com.jn.kiku.ttp.BuildConfig
import com.jn.kiku.ttp.TtpConstants

/**
 * Author：Stevie.Chen Time：2020/09/08 10:48
 * Class Comment：环信管理
 */
object MobManage {

    fun init(context: Context?, logEnable: Boolean = BuildConfig.DEBUG) {
        ChatClient.getInstance().init(
            context, ChatClient.Options()
                .setAppkey(TtpConstants.MOB_APPKEY) //必填项，appkey获取地址：kefu.easemob.com，“管理员模式 > 渠道管理 > 手机APP”页面的关联的“Ap
                .setTenantId(TtpConstants.MOB_TENANTID) //必填项，tenantId获取地址：kefu.easemob.com，“管理员模式 > 设置 > 企业信息”页面的“租户ID”
                .setConsoleLog(logEnable)
        ) //是否开启日志
    }

    /**
     * 注册
     *
     * @param username 用户名
     * @param password 密码
     */
    fun register(username: String?, password: String?) {
        ChatClient.getInstance().register(username, password, object : Callback {
            override fun onSuccess() {}

            /*ErrorCode:
                    Error.NETWORK_ERROR 网络不可用
                    Error.USER_ALREADY_EXIST  用户已存在
                    Error.USER_AUTHENTICATION_FAILED 无开放注册权限（后台管理界面设置[开放|授权]）
                    Error.USER_ILLEGAL_ARGUMENT 用户名非法
           */
            override fun onError(i: Int, s: String) {}
            override fun onProgress(i: Int, s: String) {}
        })
    }

    /**
     * 登录
     *
     * @param username 用户名
     * @param password 密码
     */
    fun login(username: String?, password: String?) {
        ChatClient.getInstance().login(username, password, object : Callback {
            override fun onSuccess() {}

            /*ErrorCode:
                    Error.NETWORK_ERROR 网络不可用
                    Error.USER_ALREADY_EXIST  用户已存在
                    Error.USER_AUTHENTICATION_FAILED 无开放注册权限（后台管理界面设置[开放|授权]）
                    Error.USER_ILLEGAL_ARGUMENT 用户名非法
           */
            override fun onError(i: Int, s: String) {}
            override fun onProgress(i: Int, s: String) {}
        })
    }

    /**
     * 登录
     *
     * @param username 用户名
     * @param token    token
     */
    fun loginWithToken(username: String?, token: String?) {
        ChatClient.getInstance().loginWithToken(username, token, object : Callback {
            override fun onSuccess() {}

            /*ErrorCode:
                    Error.NETWORK_ERROR 网络不可用
                    Error.USER_ALREADY_EXIST  用户已存在
                    Error.USER_AUTHENTICATION_FAILED 无开放注册权限（后台管理界面设置[开放|授权]）
                    Error.USER_ILLEGAL_ARGUMENT 用户名非法
           */
            override fun onError(i: Int, s: String) {}
            override fun onProgress(i: Int, s: String) {}
        })
    }

    /**
     * 退出登录
     */
    fun logout() {
        // unbindToken:是否解绑推送的devicetoken
        ChatClient.getInstance().logout(true, object : Callback {
            override fun onSuccess() {}

            /*ErrorCode:
                    Error.NETWORK_ERROR 网络不可用
                    Error.USER_ALREADY_EXIST  用户已存在
                    Error.USER_AUTHENTICATION_FAILED 无开放注册权限（后台管理界面设置[开放|授权]）
                    Error.USER_ILLEGAL_ARGUMENT 用户名非法
           */
            override fun onError(i: Int, s: String) {}
            override fun onProgress(i: Int, s: String) {}
        })
    }

    /**
     * 是否已经登录
     *
     * @return true:登录
     */
    val isLogin: Boolean
        get() = ChatClient.getInstance().isLoggedInBefore

    /**
     * 打开客服界面
     *
     * @param activity       Activity
     * @param toChatUsername 客服
     */
    fun openServiceUI(activity: Activity, toChatUsername: String?) {
        val intent = IntentBuilder(activity)
            .setServiceIMNumber(toChatUsername) //客服关联的IM服务号 获取地址：kefu.easemob.com，“管理员模式 > 渠道管理 > 手机APP”页面的关联的“IM服务号”
            .build()
        activity.startActivity(intent)
    }

    /**
     * 创建一个新的留言
     *
     * @param postContent 留言内容
     * @param projectId   留言ProjectId  进入“管理员模式 → 留言”，可以看到这个Project ID
     * @param imUser      接入环信移动客服系统使用的关联的IM服务号
     */
    fun createLeaveMsg(postContent: String?, projectId: String?, imUser: String?) {
        ChatClient.getInstance().leaveMsgManager()
            .createLeaveMsg(postContent, projectId, imUser, object : ValueCallBack<String?> {
                override fun onSuccess(o: String?) {}
                override fun onError(i: Int, s: String) {}
            })
    }

    /**
     * 添加网络监听，可以显示当前是否连接服务器
     */
    fun addConnectionListener() {
        ChatClient.getInstance().addConnectionListener(object : ConnectionListener {
            override fun onConnected() {
                //成功连接到服务器
            }

            /*  errorcode的值
                    Error.USER_REMOVED 账号移除
                    Error.USER_LOGIN_ANOTHER_DEVICE 账号在其他地方登录
                    Error.USER_AUTHENTICATION_FAILED 账号密码错误
                    Error.USER_NOT_FOUND  账号找不到
            */
            override fun onDisconnected(errorcode: Int) {}
        })
    }

    /**
     * 添加消息监听
     */
    fun addMessageListener() {
        ChatClient.getInstance().chatManager().addMessageListener(object : MessageListener {
            override fun onMessage(list: List<Message>) {
                //收到普通消息
            }

            override fun onCmdMessage(list: List<Message>) {
                //收到命令消息，命令消息不存数据库，一般用来作为系统通知，例如留言评论更新，
                //会话被客服接入，被转接，被关闭提醒
            }

            override fun onMessageStatusUpdate() {
                //消息的状态修改，一般可以用来刷新列表，显示最新的状态
            }

            override fun onMessageSent() {
                //发送消息后，会调用，可以在此刷新列表，显示最新的消息
            }
        })
    }
}