package com.jn.kiku.ttp.pay.alipay

import android.app.Activity
import android.os.Handler
import android.os.Message
import android.text.TextUtils
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.alipay.sdk.app.AuthTask
import com.alipay.sdk.app.PayTask
import com.jn.kiku.ttp.TtpConstants
import com.jn.kiku.ttp.pay.alipay.OrderInfoUtil2_0.buildAuthInfoMap
import com.jn.kiku.ttp.pay.alipay.OrderInfoUtil2_0.buildOrderParam
import com.jn.kiku.ttp.pay.alipay.OrderInfoUtil2_0.buildOrderParamMap
import com.jn.kiku.ttp.pay.alipay.OrderInfoUtil2_0.getSign
import com.jn.kikukt.common.utils.ContextUtils
import com.jn.kikukt.common.utils.logI
import com.jn.kikukt.common.utils.showToast

/**
 * Author：Stevie.Chen Time：2020/09/08 12:06
 * Class Comment：支付宝支付管理, 真实App里, privateKey等数据严禁放在客户端, 加签过程务必要放在服务端完成
 */
class AliPayManage private constructor() : Handler.Callback, DefaultLifecycleObserver {
    private var mHandler: Handler? = null
    private var onSuccess: (() -> Unit)? = null
    private var onFailure: ((errorCode: String?) -> Unit)? = null

    companion object {
        private const val SDK_PAY_FLAG = 1 //支付
        private const val SDK_AUTH_FLAG = 2 //授权

        val instance: AliPayManage by lazy(mode = LazyThreadSafetyMode.NONE) {
            AliPayManage()
        }
    }

    /**
     * 支付
     *
     * @param activity activity
     * App组装参数和加密信息
     */
    fun pay(
        activity: Activity,
        onSuccess: (() -> Unit)? = null,
        onFailure: ((errorCode: String?) -> Unit)? = null
    ) {
        if (mHandler == null) mHandler = Handler(this)
        this.onSuccess = onSuccess
        this.onFailure = onFailure
        val isRsa2 = TtpConstants.ALIPAY_RSA2_PRIVATE.length > 0 //是否使用rsa2秘钥
        val privateKey =
            if (isRsa2) TtpConstants.ALIPAY_RSA2_PRIVATE else TtpConstants.ALIPAY_RSA_PRIVATE
        val params: Map<String, String?> = buildOrderParamMap(TtpConstants.ALIPAY_APPID, isRsa2)
        val orderParam = buildOrderParam(params)
        val sign = getSign(params, privateKey, isRsa2)
        val orderInfo = "$orderParam&$sign"
        val payRunnable = Runnable {
            val alipay = PayTask(activity)
            val result = alipay.payV2(orderInfo, true)
            "AliPayResult：$result".logI()
            val msg = Message()
            msg.what = SDK_PAY_FLAG
            msg.obj = result
            mHandler!!.sendMessage(msg)
        }
        val payThread = Thread(payRunnable)
        payThread.start()
    }

    /**
     * 支付
     *
     * @param activity  activity
     * @param orderInfo 订单信息
     *
     * 服务器组装参数和加密信息
     *
     */
    fun pay(
        activity: Activity,
        orderInfo: String?,
        onSuccess: (() -> Unit)? = null,
        onFailure: ((errorCode: String?) -> Unit)? = null
    ) {
        if (mHandler == null) mHandler = Handler(this)
        this.onSuccess = onSuccess
        this.onFailure = onFailure
        val payRunnable = Runnable {
            val alipay = PayTask(activity)
            val result = alipay.payV2(orderInfo, true)
            "AliPayResult：$result".logI()
            val msg = Message()
            msg.what = SDK_PAY_FLAG
            msg.obj = result
            mHandler!!.sendMessage(msg)
        }
        val payThread = Thread(payRunnable)
        payThread.start()
    }

    /**
     * 授权
     *
     * @param activity activity
     */
    fun authV2(
        activity: Activity,
        onSuccess: (() -> Unit)? = null,
        onFailure: ((errorCode: String?) -> Unit)? = null
    ) {
        if (mHandler == null) mHandler = Handler(this)
        this.onSuccess = onSuccess
        this.onFailure = onFailure
        val isRsa2 = TtpConstants.ALIPAY_RSA2_PRIVATE.length > 0 //是否使用rsa2秘钥
        val privateKey =
            if (isRsa2) TtpConstants.ALIPAY_RSA2_PRIVATE else TtpConstants.ALIPAY_RSA_PRIVATE
        val authInfoMap: Map<String, String?> = buildAuthInfoMap(
            TtpConstants.ALIPAY_PID,
            TtpConstants.ALIPAY_APPID,
            TtpConstants.ALIPAY_TARGET_ID,
            isRsa2
        )
        val info = buildOrderParam(authInfoMap)
        val sign = getSign(authInfoMap, privateKey, isRsa2)
        val authInfo = "$info&$sign"
        val authRunnable = Runnable {

            // 构造AuthTask 对象
            val authTask = AuthTask(activity)
            // 调用授权接口，获取授权结果
            val result = authTask.authV2(authInfo, true)
            val msg = Message()
            msg.what = SDK_AUTH_FLAG
            msg.obj = result
            mHandler!!.sendMessage(msg)
        }

        // 必须异步调用
        val authThread = Thread(authRunnable)
        authThread.start()
    }

    /**
     * 授权
     *
     * @param activity activity
     * @param authInfo 授权信息
     *
     * 服务器组装参数和加密信息
     *
     */
    fun authV2(
        activity: Activity, authInfo: String?,
        onSuccess: (() -> Unit)? = null,
        onFailure: ((errorCode: String?) -> Unit)? = null
    ) {
        if (mHandler == null) mHandler = Handler(this)
        this.onSuccess = onSuccess
        this.onFailure = onFailure
        val authRunnable = Runnable {

            // 构造AuthTask 对象
            val authTask = AuthTask(activity)
            // 调用授权接口，获取授权结果
            val result = authTask.authV2(authInfo, true)
            val msg = Message()
            msg.what = SDK_AUTH_FLAG
            msg.obj = result
            mHandler!!.sendMessage(msg)
        }

        // 必须异步调用
        val authThread = Thread(authRunnable)
        authThread.start()
    }

    override fun handleMessage(msg: Message): Boolean {
        when (msg.what) {
            SDK_PAY_FLAG -> {
                val aliPayResult = AliPayResult(msg.obj as? Map<String?, String?>)
                /*
                 对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                 9000 	订单支付成功
                 8000 	正在处理中，支付结果未知（有可能已经支付成功），请查询商户订单列表中订单的支付状态
                 4000 	订单支付失败
                 5000 	重复请求
                 6001 	用户中途取消
                 6002 	网络连接出错
                 6004 	支付结果未知（有可能已经支付成功），请查询商户订单列表中订单的支付状态
                 其它 	其它支付错误
                 */
                //String resultInfo = aliPayResult.getResult();// 同步返回需要验证的信息
                val resultStatus = aliPayResult.resultStatus
                // 判断resultStatus 为9000则代表支付成功
                if (TextUtils.equals(resultStatus, "9000")) {
                    // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                    ContextUtils.context.showToast("支付成功")
                    onSuccess?.invoke()
                } else if (TextUtils.equals(resultStatus, "6001")) { //取消支付
                    ContextUtils.context.showToast("取消支付")
                } else {
                    // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                    ContextUtils.context.showToast("支付失败")
                    onFailure?.invoke(resultStatus)
                }
            }
            SDK_AUTH_FLAG -> {
                val aliAuthResult = AliAuthResult(msg.obj as? Map<String?, String?>, true)
                val resultStatus = aliAuthResult.resultStatus

                // 判断resultStatus 为“9000”且result_code
                // 为“200”则代表授权成功，具体状态码代表含义可参考授权接口文档
                if (TextUtils.equals(
                        resultStatus,
                        "9000"
                    ) && TextUtils.equals(aliAuthResult.resultCode, "200")
                ) {
                    // 获取alipay_open_id，调支付时作为参数extern_token 的value
                    // 传入，则支付账户为该授权账户
                    ContextUtils.context.showToast("授权成功")
                    onSuccess?.invoke()
                } else {
                    // 其他状态值则为授权失败
                    ContextUtils.context.showToast("授权失败")
                    onFailure?.invoke(resultStatus)
                }
                String.format("authCode:%s", aliAuthResult.authCode).logI()
            }
            else -> {
            }
        }
        return false
    }

    private fun onDestroy() {
        if (onSuccess != null) onSuccess = null
        if (onFailure != null) onFailure = null
        if (mHandler != null) {
            mHandler?.removeCallbacksAndMessages(null)
            mHandler = null
        }
    }

    override fun onDestroy(owner: LifecycleOwner) {
        onDestroy()
    }
}