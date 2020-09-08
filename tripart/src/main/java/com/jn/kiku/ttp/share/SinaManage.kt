package com.jn.kiku.ttp.share

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.jn.kiku.ttp.TtpConstants
import com.jn.kiku.ttp.share.sina.SinaApiManager
import com.jn.kiku.ttp.share.sina.SinaApiService
import com.jn.kiku.ttp.share.sina.SinaUserInfoVO
import com.jn.kikukt.common.utils.ContextUtils
import com.jn.kikukt.common.utils.showToast
import com.sina.weibo.sdk.WbSdk
import com.sina.weibo.sdk.api.ImageObject
import com.sina.weibo.sdk.api.TextObject
import com.sina.weibo.sdk.api.WeiboMultiMessage
import com.sina.weibo.sdk.auth.*
import com.sina.weibo.sdk.auth.sso.SsoHandler
import com.sina.weibo.sdk.share.WbShareCallback
import com.sina.weibo.sdk.share.WbShareHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

/**
 * Author：Stevie.Chen Time：2019/8/2
 * Class Comment：新浪登录 、 分享管理
 */
class SinaManage private constructor() : WbShareCallback, DefaultLifecycleObserver {
    private val mContext: Context
        get() = ContextUtils.context
    private var mSsoHandler: SsoHandler? = null //授权关键类(登录仅用到此类)

    //封装了 "access_token"，"expires_in"，"refresh_token"，并提供了他们的管理功能
    private var mAccessToken: Oauth2AccessToken? = null
    private var mWbShareHandler: WbShareHandler? = null //分享关键类
    private var mSinaResultListener: SinaResultListener? = null //登录、分享结果监听
    private var mSinaUserInfoResultListener: SinaUserInfoResultListener? = null //用户信息监听
    private var lifecycleOwner: LifecycleOwner? = null

    companion object {
        val instance: SinaManage by lazy(mode = LazyThreadSafetyMode.NONE) { SinaManage() }
    }

    /**
     * 初始化分享
     */
    private fun initShare(activity: Activity) {
        if (null == mWbShareHandler) mWbShareHandler = WbShareHandler(activity)
        mWbShareHandler?.registerApp()
    }

    /**
     * 初始化登录
     */
    private fun initLogin(activity: Activity) {
        mSsoHandler = SsoHandler(activity)
    }

    /**
     * 新浪微博登录
     *
     * @param listener 结果监听
     */
    fun login(activity: Activity, listener: SinaResultListener) {
        mSinaResultListener = listener
        initLogin(activity)
        mSsoHandler?.authorize(SelfWbAuthListener())
    }

    /**
     * 新浪微博登录
     *
     * @param listener 结果监听
     */
    fun login(activity: Activity, listener: SinaUserInfoResultListener) {
        initLogin(activity)
        mSinaUserInfoResultListener = listener
        mSsoHandler?.authorize(SelfWbAuthListener())
    }

    /**
     * 新浪微博退出
     */
    fun logout() {
        AccessTokenKeeper.clear(mContext)
        mAccessToken = Oauth2AccessToken()
    }

    /**
     * 新浪微博分享
     *
     * @param listener 结果监听
     *
     *
     * TextObject textObject = new TextObject();
     * textObject.title = "";
     * textObject.text = "分享内容";
     * textObject.actionUrl = "";
     *
     */
    fun share(
        activity: Activity,
        textObject: TextObject?,
        imageObject: ImageObject?,
        listener: SinaResultListener?
    ) {
        mSinaResultListener = listener
        val message = WeiboMultiMessage()
        if (textObject != null) message.textObject = textObject
        if (imageObject != null) {
            message.imageObject = imageObject
        }
        initShare(activity)
        if (mWbShareHandler != null) mWbShareHandler!!.shareMessage(message, false)
    }

    /**
     * 设置APP_KEY、REDIRECT_URL、SCOPE等信息
     */
    private fun install() {
        WbSdk.install(
            mContext,
            AuthInfo(
                mContext,
                TtpConstants.SINA_APP_KEY,
                TtpConstants.SINA_REDIRECT_URL,
                TtpConstants.SINA_SCOPE
            )
        )
    }

    /**
     * 授权时在Activity中onActivityResult()方法中调用
     */
    fun authorizeCallBack(requestCode: Int, resultCode: Int, data: Intent?) {
        // SSO 授权回调
        // 重要：发起 SSO 登陆的 Activity 必须重写 onActivityResults
        mSsoHandler?.authorizeCallBack(requestCode, resultCode, data)
    }

    /**
     * 分享时在Activity中onNewIntent()方法中调用
     */
    fun onActivityResult(intent: Intent?) {
        mWbShareHandler?.doResultIntent(intent, this)
    }

    /**
     * 登录回调
     */
    private inner class SelfWbAuthListener : WbAuthListener {
        override fun onSuccess(token: Oauth2AccessToken) {
            mAccessToken = token
            if (mAccessToken!!.isSessionValid) {
                // 保存 Token 到 SharedPreferences
                AccessTokenKeeper.writeAccessToken(mContext, mAccessToken)
                //ToastUtil.showToast(mContext, "登录成功");
                if (mSinaResultListener != null) mSinaResultListener!!.onSuccess(token)
                if (mSinaUserInfoResultListener != null) getUserInfo(token)
            }
        }

        override fun cancel() {
            showToast("取消登录")
        }

        override fun onFailure(errorMessage: WbConnectErrorMessage) {
            showToast("登录失败")
            if (mSinaResultListener != null) mSinaResultListener!!.onFailure(errorMessage)
        }
    }

    override fun onWbShareSuccess() {
        if (mSinaResultListener != null) mSinaResultListener!!.onSuccess(null)
        showToast("分享成功")
    }

    override fun onWbShareCancel() {
        showToast("取消分享")
    }

    override fun onWbShareFail() {
        if (mSinaResultListener != null) mSinaResultListener!!.onFailure(null)
        showToast("分享失败")
    }

    /**
     * 获取用户信息
     */
    fun getUserInfo(token: Oauth2AccessToken) {
        lifecycleOwner?.lifecycleScope?.launch {
            val result = async(Dispatchers.IO) {
                SinaApiManager.service.getUserInfo(
                    SinaApiService.GET_USERINFO,
                    token.token,
                    token.uid
                )
            }
            val sinaUserInfoVO = result.await()?.apply {
                uid = token.uid
                access_token = token.token
            }
            mSinaUserInfoResultListener?.onSuccess(sinaUserInfoVO)
        }
    }

    private fun showToast(message: String) {
        mContext.showToast(message)
    }

    private fun onDestroy() {
        if (mSsoHandler != null) mSsoHandler = null
        if (mAccessToken != null) mAccessToken = null
        if (mWbShareHandler != null) mWbShareHandler = null
        if (mSinaResultListener != null) mSinaResultListener = null
        if (mSinaUserInfoResultListener != null) mSinaUserInfoResultListener = null
        if (lifecycleOwner != null) lifecycleOwner = null
    }

    /**
     * 分享、登录结果回调(分享返回的token和errorMessage都为空)
     */
    interface SinaResultListener {
        fun onSuccess(token: Oauth2AccessToken?)
        fun onFailure(errorMessage: WbConnectErrorMessage?)
    }

    interface SinaUserInfoResultListener {
        fun onSuccess(response: SinaUserInfoVO?)
    }

    override fun onCreate(owner: LifecycleOwner) {
        lifecycleOwner = owner
        install()
    }

    override fun onDestroy(owner: LifecycleOwner) {
        onDestroy()
    }

}