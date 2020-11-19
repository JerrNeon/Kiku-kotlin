package com.jn.kiku.ttp.wxapi

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.jn.kiku.ttp.share.WeChatManage
import com.jn.kiku.ttp.share.WeChatManage.WeChatResultListener
import com.jn.kikukt.common.utils.logE
import com.jn.kikukt.common.utils.logI
import com.jn.kikukt.common.utils.showToast
import com.tencent.mm.opensdk.constants.ConstantsAPI
import com.tencent.mm.opensdk.modelbase.BaseReq
import com.tencent.mm.opensdk.modelbase.BaseResp
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler

/**
 * Author：Stevie.Chen Time：2019/8/2
 * Class Comment：微信支付回调
 */
open class WXPayEntryCallbackActivity : AppCompatActivity(), IWXAPIEventHandler {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WeChatManage.instance.handleIntent(intent, this)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        WeChatManage.instance.handleIntent(getIntent(), this)
    }

    // 微信发送请求到第三方应用时，会回调到该方法
    override fun onReq(baseReq: BaseReq) {}

    // 第三方应用发送到微信的请求处理后的响应结果，会回调到该方法
    override fun onResp(baseResp: BaseResp) {
        when (baseResp.errCode) {
            BaseResp.ErrCode.ERR_OK -> {
                when (baseResp.type) {
                    ConstantsAPI.COMMAND_SENDAUTH -> {
                        //授权成功
                        //ToastUtil.showToast(this, "登录成功");
                        logI("onResp: 授权成功")
                    }
                    ConstantsAPI.COMMAND_SENDMESSAGE_TO_WX -> {
                        //分享成功
                        logI("onResp: 分享成功")
                        showToast("分享成功")
                    }
                    ConstantsAPI.COMMAND_PAY_BY_WX -> {
                        //支付成功
                        logI("onResp: 支付成功")
                        showToast("支付成功")
                    }
                    ConstantsAPI.COMMAND_LAUNCH_WX_MINIPROGRAM -> {
                        //WXLaunchMiniProgram.Resp baseResp
                        logI("onResp: 启动小程序成功")
                    }
                }
                mWeChatResultListener?.onSuccess(baseResp)
            }
            BaseResp.ErrCode.ERR_USER_CANCEL -> when (baseResp.type) {
                ConstantsAPI.COMMAND_SENDAUTH -> {
                    //授权取消
                    logI("onResp: 取消授权")
                    showToast("取消登录")
                }
                ConstantsAPI.COMMAND_SENDMESSAGE_TO_WX -> {
                    //分享成功
                    logI("onResp: 取消分享")
                    showToast("取消分享")
                }
                ConstantsAPI.COMMAND_PAY_BY_WX -> {
                    //支付取消
                    logI("onResp: 取消支付")
                    showToast("取消支付")
                }
                ConstantsAPI.COMMAND_LAUNCH_WX_MINIPROGRAM -> {
                    logI("onResp: 取消启动小程序")
                }
            }
            else -> {
                when (baseResp.type) {
                    ConstantsAPI.COMMAND_SENDAUTH -> {
                        //授权失败
                        logE("onResp: 授权失败")
                        showToast("登录失败")
                    }
                    ConstantsAPI.COMMAND_SENDMESSAGE_TO_WX -> {
                        //分享失败
                        logE("onResp:分享失败")
                        showToast("分享失败")
                    }
                    ConstantsAPI.COMMAND_PAY_BY_WX -> {
                        //支付失败
                        logE("onResp:支付失败")
                        showToast("支付失败")
                    }
                    ConstantsAPI.COMMAND_LAUNCH_WX_MINIPROGRAM -> {
                        logI("onResp: 启动小程序失败")
                    }
                }
                mWeChatResultListener?.onFailure(baseResp)
            }
        }
        finish()
    }

    private fun logI(message: String) {
        message.logI()
    }

    private fun logE(message: String) {
        message.logE()
    }

    private fun showToast(message: String) {
        applicationContext.showToast(message)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mWeChatResultListener != null) mWeChatResultListener = null
    }

    companion object {
        private var mWeChatResultListener: WeChatResultListener? = null

        @JvmStatic
        fun setWeChatResultListener(listener: WeChatResultListener?) {
            mWeChatResultListener = listener
        }
    }
}