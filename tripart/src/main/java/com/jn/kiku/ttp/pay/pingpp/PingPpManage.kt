package com.jn.kiku.ttp.pay.pingpp

import android.app.Activity
import android.content.Intent
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.jn.kikukt.common.utils.ContextUtils
import com.jn.kikukt.common.utils.logE
import com.jn.kikukt.common.utils.showToast
import com.pingplusplus.android.Pingpp

/**
 * Author：Stevie.Chen Time：2020/09/08 15:04
 * Class Comment：Ping + + 管理类
 */
class PingPpManage private constructor() : DefaultLifecycleObserver {

    private var onSuccess: (() -> Unit)? = null
    private var onFailure: (() -> Unit)? = null

    companion object {
        val instance: PingPpManage by lazy(mode = LazyThreadSafetyMode.NONE) { PingPpManage() }
    }

    /**
     * 发起支付（除QQ钱包外）
     *
     * @param activity 表示当前调起支付的Activity
     * @param data     表示获取到的charge或order的JSON字符串
     */
    fun pay(
        activity: Activity?, data: String?,
        onSuccess: (() -> Unit)? = null,
        onFailure: (() -> Unit)? = null
    ) {
        this.onSuccess = onSuccess
        this.onFailure = onFailure
        Pingpp.createPayment(activity, data)
    }

    /**
     * @param fragment 表示当前调起支付的Fragment
     * @param data     表示获取到的charge或order的JSON字符串
     */
    fun pay(
        fragment: Fragment?, data: String?, onSuccess: (() -> Unit)? = null,
        onFailure: (() -> Unit)? = null
    ) {
        this.onSuccess = onSuccess
        this.onFailure = onFailure
        Pingpp.createPayment(fragment, data)
    }

    /**
     * 是否开启日志
     *
     * @param isDebugEnable true:开启 false:关闭
     */
    fun setDebugEnable(isDebugEnable: Boolean) {
        //开启调试模式
        Pingpp.DEBUG = isDebugEnable
    }

    /**
     * 支付回调
     *
     * @param data
     *
     *
     * pay_result ->处理返回值
     * "success" - 支付成功
     * "fail"    - 支付失败
     * "cancel"  - 取消支付
     * "invalid" - 支付插件未安装（一般是微信客户端未安装的情况）
     * "unknown" - app进程异常被杀死(一般是低内存状态下,app进程被杀死)
     *
     */
    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        //支付页面返回处理
        if (requestCode == Pingpp.REQUEST_CODE_PAYMENT && resultCode == Activity.RESULT_OK) {
            val bundle = data.extras
            var result: String? = null
            if (bundle != null) {
                result = bundle.getString("pay_result")
            }
            if (result != null && "" != result) {
                when (result) {
                    "success" -> {
                        showToast("支付成功")
                        onSuccess?.invoke()
                    }
                    "fail" -> {
                        showToast("支付失败")
                        onFailure?.invoke()
                    }
                    "cancel" -> showToast("取消支付")
                    "invalid" -> {
                        "支付插件未安装".logE()
                        showToast("支付失败")
                        onFailure?.invoke()
                    }
                    "unknown" -> {
                        "app进程异常被杀死".logE()
                        showToast("支付失败")
                        onFailure?.invoke()
                    }
                    else -> {
                    }
                }
            }
            if (bundle != null) {
                val errorMsg = bundle.getString("error_msg") // 错误信息
                val extraMsg = bundle.getString("extra_msg") // 错误信息
            }
        }
    }

    private fun showToast(message: String) {
        ContextUtils.context.showToast(message)
    }

    private fun onDestroy() {
        if (onSuccess != null) onSuccess = null
        if (onFailure != null) onFailure = null
    }

    override fun onDestroy(owner: LifecycleOwner) {
        onDestroy()
        super.onDestroy(owner)
    }
}