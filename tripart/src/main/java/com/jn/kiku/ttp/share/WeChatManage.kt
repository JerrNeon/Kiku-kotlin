package com.jn.kiku.ttp.share

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.annotation.IntDef
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.jn.kiku.ttp.TtpConstants
import com.jn.kiku.ttp.pay.wxpay.WxPayInfoVO
import com.jn.kiku.ttp.share.wechat.WeChatAccessTokenVO
import com.jn.kiku.ttp.share.wechat.WeChatApiManager
import com.jn.kiku.ttp.share.wechat.WeChatApiService
import com.jn.kiku.ttp.share.wechat.WeChatUserInfoVO
import com.jn.kiku.ttp.wxapi.WXEntryCallbackActivity
import com.jn.kiku.ttp.wxapi.WXPayEntryCallbackActivity
import com.jn.kikukt.common.utils.ContextUtils
import com.tencent.mm.opensdk.constants.Build
import com.tencent.mm.opensdk.constants.ConstantsAPI
import com.tencent.mm.opensdk.modelbase.BaseResp
import com.tencent.mm.opensdk.modelbiz.WXLaunchMiniProgram
import com.tencent.mm.opensdk.modelmsg.SendAuth
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage
import com.tencent.mm.opensdk.modelpay.PayReq
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.lang.reflect.InvocationTargetException
import java.util.*

/**
 * Author：Stevie.Chen Time：2019/8/2
 * Class Comment：微信登录 、 分享 、 支付管理
 */
class WeChatManage : DefaultLifecycleObserver {
    private val mContext: Context
        get() = ContextUtils.context

    private var mIWXAPI: IWXAPI? = null
    private var mWeChatResultListener: WeChatResultListener? = null
    private val mWeChatLoginResultListener: WeChatResultListener = WeChatResultListenerIml()
    private var mWeChatTokenResultListener: WeChatTokenResultListener? = null
    private var mWeChatUserInfoResultListener: WeChatUserInfoResultListener? = null
    private var mWeChatLaunchReceiver: BroadcastReceiver? = null//微信启动广播
    private var lifecycleOwner: LifecycleOwner? = null

    @IntDef(WECHAT_FRIEND, WECHAT_CIRCLE)
    @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
    private annotation class ShareType

    private val mHandler = Handler(Looper.getMainLooper()) { msg ->
        if (msg.what == requestCode1) {
            if (mWeChatResultListener != null) mWeChatResultListener!!.onFailure(null)
            if (mWeChatTokenResultListener != null) mWeChatTokenResultListener!!.onFailure()
            if (mWeChatUserInfoResultListener != null) mWeChatUserInfoResultListener!!.onFailure()
            Toast.makeText(mContext, "您还未安装微信,请安装微信客户端", Toast.LENGTH_SHORT).show()
        } else if (msg.what == requestCode2) {
            if (mWeChatResultListener != null) mWeChatResultListener!!.onFailure(null)
            if (mWeChatTokenResultListener != null) mWeChatTokenResultListener!!.onFailure()
            if (mWeChatUserInfoResultListener != null) mWeChatUserInfoResultListener!!.onFailure()
            Toast.makeText(mContext, "微信版本太低,不能分享到朋友圈", Toast.LENGTH_SHORT).show()
        }
        false
    }

    companion object {
        const val WECHAT_FRIEND = 1 //微信好友
        const val WECHAT_CIRCLE = 2 //微信朋友圈
        private const val requestCode1 = 0x01
        private const val requestCode2 = 0x02

        val instance: WeChatManage by lazy(mode = LazyThreadSafetyMode.NONE) { WeChatManage() }
    }

    private fun init(activity: Activity) {
        if (mIWXAPI == null) mIWXAPI =
            WXAPIFactory.createWXAPI(activity, TtpConstants.WECHAT_APP_ID, false)
    }

    /**
     * 登录
     */
    fun <T : WXEntryCallbackActivity?> login(
        activity: Activity,
        tClass: Class<T>,
        resultListener: WeChatResultListener? = null,
        tokenResultListener: WeChatTokenResultListener? = null,
        userInfoResultListener: WeChatUserInfoResultListener? = null
    ) {
        init(activity)
        mWeChatResultListener = resultListener
        mWeChatTokenResultListener = tokenResultListener
        mWeChatUserInfoResultListener = userInfoResultListener
        if (mIWXAPI == null) return
        if (isWXAppInstalled) return
        try {
            val method =
                tClass.getMethod("setWeChatResultListener", WeChatResultListener::class.java)
            if (mWeChatResultListener != null || tokenResultListener != null || userInfoResultListener != null)
                method.invoke(null, mWeChatLoginResultListener)
        } catch (e: NoSuchMethodException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
        }
        val req = SendAuth.Req()
        req.scope = "snsapi_userinfo" //应用授权作用域
        val random = Random()
        req.state =
            "wx${random.nextInt()}${System.currentTimeMillis()}" //用于保持请求和回调的状态，授权请求后原样带回给第三方。可设置为简单的随机数加session进行校验
        mIWXAPI?.sendReq(req)
    }

    /**
     * 微信分享
     *
     * @param type     WeiXinFriend: 好友分享,WeiXinCircle：朋友圈分享
     * @param listener 结果监听
     * 1.
     * WXWebpageObject object = new WXWebpageObject();//网页地址分享
     * object.webpageUrl = "网页url";//限制长度不超过10KB
     * <P>
     * //初始化一个WXMusicObject，填写url
     * WXMusicObject object = new WXMusicObject();
     * object.musicUrl="音乐url";//限制长度不超过10KB
     * </P>
     * WXVideoObject object = new WXVideoObject();
     * object.videoUrl ="视频url";//限制长度不超过10KB
     * WXMiniProgramObject object = new WXMiniProgramObject();
     * object.webpageUrl = "http://www.qq.com"; // 兼容低版本的网页链接
     * object.miniprogramType = WXMiniProgramObject.MINIPTOGRAM_TYPE_RELEASE;// 正式版:0，测试版:1，体验版:2
     * object.userName = "gh_d43f693ca31f";     // 小程序原始id
     * object.path = "/pages/media";            //小程序页面路径
     * 2.
     * WXMediaMessage wxMediaMessage = new WXMediaMessage();
     * wxMediaMessage.mediaObject = object;
     * wxMediaMessage.title = "标题";//限制长度不超过512Bytes
     * wxMediaMessage.description = "描述";//限制长度不超过1KB
     * Bitmap thumbBmp = BitmapFactory.decodeResource(getResources(), R.drawable.send_music_thumb);
     * wxMediaMessage.thumbData = Util.bmpToByteArray(thumbBmp, true);//限制内容大小不超过32KB
     */
    fun <T : WXEntryCallbackActivity?> share(
        activity: Activity,
        @ShareType type: Int,
        wxMediaMessage: WXMediaMessage?,
        tClass: Class<T>,
        listener: WeChatResultListener?
    ) {
        init(activity)
        mWeChatResultListener = listener
        if (mIWXAPI == null) return
        if (isWXAppInstalled) return
        try {
            val method =
                tClass.getMethod("setWeChatResultListener", WeChatResultListener::class.java)
            if (listener != null) method.invoke(null, listener)
        } catch (e: NoSuchMethodException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
        }
        if (wxMediaMessage != null) {
            val req = SendMessageToWX.Req()
            if (type == WECHAT_FRIEND) req.scene = SendMessageToWX.Req.WXSceneSession //发送到微信好友(默认)
            else if (type == WECHAT_CIRCLE) {
                if (mIWXAPI!!.wxAppSupportAPI >= Build.TIMELINE_SUPPORTED_SDK_INT) req.scene =
                    SendMessageToWX.Req.WXSceneTimeline //发送到朋友圈
                else mHandler.sendEmptyMessage(requestCode2)
            }
            req.transaction = System.currentTimeMillis().toString() //用于唯一标识一个请求
            req.message = wxMediaMessage
            mIWXAPI?.sendReq(req)
        }
    }

    /**
     * 支付
     *
     * @param info     支付信息
     * @param listener 结果监听
     */
    fun <T : WXPayEntryCallbackActivity?> pay(
        activity: Activity,
        info: WxPayInfoVO,
        tClass: Class<T>,
        listener: WeChatResultListener?
    ) {
        init(activity)
        mWeChatResultListener = listener
        if (mIWXAPI == null) return
        if (isWXAppInstalled) return
        try {
            val method =
                tClass.getMethod("setWeChatResultListener", WeChatResultListener::class.java)
            if (listener != null) method.invoke(null, listener)
        } catch (e: NoSuchMethodException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
        }
        val request = PayReq()
        request.appId = TtpConstants.WECHAT_APP_ID
        request.partnerId = info.partnerid //微信支付分配的商户号
        //微信返回的支付交易会话ID(服务器生成预付单，获取到prepay_id后将参数再次签名传输给APP发起支付)
        request.prepayId = info.prepayid
        request.packageValue = "Sign=WXPay"
        request.nonceStr = info.noncestr //随机字符串，不长于32位
        request.timeStamp = info.timestamp
        request.sign = info.sign
        mIWXAPI?.sendReq(request)
    }

    /**
     * 跳转到微信小程序
     *
     * @param req      请求参数
     * WXLaunchMiniProgram.Req req = new WXLaunchMiniProgram.Req();
     * req.userName = "gh_d43f693ca31f"; // 填小程序原始id
     * req.path = path;                  //拉起小程序页面的可带参路径，不填默认拉起小程序首页，对于小游戏，可以只传入 query 部分，来实现传参效果，如：传入 "?foo=bar"。
     * req.miniprogramType = WXLaunchMiniProgram.Req.MINIPTOGRAM_TYPE_RELEASE;// 可选打开 开发版，体验版和正式版
     * @param listener 结果监听
     */
    fun <T : WXEntryCallbackActivity?> launch(
        activity: Activity,
        req: WXLaunchMiniProgram.Req?,
        tClass: Class<T>,
        listener: WeChatResultListener?
    ) {
        init(activity)
        mWeChatResultListener = listener
        if (mIWXAPI == null) return
        if (isWXAppInstalled) return
        try {
            val method =
                tClass.getMethod("setWeChatResultListener", WeChatResultListener::class.java)
            if (listener != null) method.invoke(null, listener)
        } catch (e: NoSuchMethodException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
        }
        mIWXAPI?.sendReq(req)
    }

    fun handleIntent(intent: Intent?, iwxapiEventHandler: IWXAPIEventHandler?) {
        if (mIWXAPI == null) return
        mIWXAPI?.handleIntent(intent, iwxapiEventHandler)
    }

    /**
     * 检查微信是否安装
     *
     * @return true：未安装
     */
    private val isWXAppInstalled: Boolean
        get() {
            if (mIWXAPI?.isWXAppInstalled != true) {
                mHandler.sendEmptyMessage(requestCode1)
                return true
            }
            return false
        }

    /**
     * 微信是否安装
     */
    fun isWXAppInstalled(activity: Activity): Boolean {
        init(activity)
        return mIWXAPI?.isWXAppInstalled ?: false
    }

    /**
     * 获取access_token
     */
    fun getAccessToken(baseResp: BaseResp) {
        lifecycleOwner?.lifecycleScope?.launch {
            val result = async(Dispatchers.IO) {
                WeChatApiManager.service.getAccessToken(
                    WeChatApiService.GET_ACCESS_TOKEN,
                    TtpConstants.WECHAT_APP_ID,
                    TtpConstants.WECHAT_SECRET,
                    (baseResp as SendAuth.Resp).code,
                    "authorization_code"
                )
            }
            val weChatAccessTokenVO = result.await()
            mWeChatTokenResultListener?.onSuccess(weChatAccessTokenVO)
        }
    }

    /**
     * 刷新access_token
     */
    fun refreshAccessToken(weChatAccessTokenVO: WeChatAccessTokenVO) {
        lifecycleOwner?.lifecycleScope?.launch {
            val result = async(Dispatchers.IO) {
                WeChatApiManager.service.refreshAccessToken(
                    WeChatApiService.REFRESH_ACCESS_TOKEN,
                    TtpConstants.WECHAT_APP_ID,
                    weChatAccessTokenVO.refresh_token,
                    "refresh_token"
                )
            }
            //val weChatAccessTokenVO = result.await()
        }
    }

    /**
     * 检查access_token是否可用
     */
    fun checkAccessToken(weChatAccessTokenVO: WeChatAccessTokenVO) {
        lifecycleOwner?.lifecycleScope?.launch {
            val result = async(Dispatchers.IO) {
                WeChatApiManager.service.checkAccessToken(
                    WeChatApiService.CHECK_ACCESS_TOKEN,
                    weChatAccessTokenVO.access_token,
                    weChatAccessTokenVO.openid
                )
            }
            //val weChatAccessTokenVO = result.await()
        }
    }

    /**
     * 获取用户信息
     */
    fun getUserInfo(baseResp: BaseResp) {
        lifecycleOwner?.lifecycleScope?.launch {
            val result = async(Dispatchers.IO) {
                val accessTokenVO = WeChatApiManager.service.getAccessToken(
                    WeChatApiService.GET_ACCESS_TOKEN,
                    TtpConstants.WECHAT_APP_ID,
                    TtpConstants.WECHAT_SECRET,
                    (baseResp as SendAuth.Resp).code,
                    "authorization_code"
                )
                WeChatApiManager.service.getUserInfo(
                    WeChatApiService.GET_USER_INFO,
                    accessTokenVO?.access_token,
                    accessTokenVO?.openid
                )
            }
            val weChatUserInfoVo = result.await()
            mWeChatUserInfoResultListener?.onSuccess(weChatUserInfoVo)
        }
    }

    private fun onDestroy() {
        if (mIWXAPI != null) mIWXAPI = null
        if (mWeChatResultListener != null) mWeChatResultListener = null
        if (mWeChatTokenResultListener != null) mWeChatTokenResultListener = null
        if (mWeChatUserInfoResultListener != null) mWeChatUserInfoResultListener = null
        if (lifecycleOwner != null) lifecycleOwner = null
    }

    /**
     * 实现回调结果
     */
    internal inner class WeChatResultListenerIml : WeChatResultListener {
        override fun onSuccess(resp: BaseResp) {
            if (mWeChatTokenResultListener != null) getAccessToken(resp)
            if (mWeChatUserInfoResultListener != null) getUserInfo(resp) //获取用户信息
        }

        override fun onFailure(resp: BaseResp?) {}
    }

    interface WeChatResultListener {
        fun onSuccess(resp: BaseResp)
        fun onFailure(resp: BaseResp?)
    }

    interface WeChatTokenResultListener {
        fun onSuccess(response: WeChatAccessTokenVO?)
        fun onFailure()
    }

    interface WeChatUserInfoResultListener {
        fun onSuccess(response: WeChatUserInfoVO?)
        fun onFailure()
    }

    override fun onCreate(owner: LifecycleOwner) {
        lifecycleOwner = owner
        if (null == mWeChatLaunchReceiver) {
            mWeChatLaunchReceiver = object : BroadcastReceiver() {
                override fun onReceive(context: Context, intent: Intent) {
                    if (mIWXAPI != null) mIWXAPI!!.registerApp(TtpConstants.WECHAT_APP_ID)
                }
            }
        }
        mContext.registerReceiver(
            mWeChatLaunchReceiver,
            IntentFilter(ConstantsAPI.ACTION_REFRESH_WXAPP)
        )
    }

    override fun onDestroy(owner: LifecycleOwner) {
        if (mWeChatLaunchReceiver != null) {
            mContext.unregisterReceiver(mWeChatLaunchReceiver)
            mWeChatLaunchReceiver = null
        }
        onDestroy()
    }
}