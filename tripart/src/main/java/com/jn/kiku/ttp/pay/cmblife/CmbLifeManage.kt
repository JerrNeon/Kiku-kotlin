package com.jn.kiku.ttp.pay.cmblife

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.jn.kiku.ttp.pay.cmblife.CmblifeSDK.handleCallBack
import com.jn.kiku.ttp.pay.cmblife.CmblifeSDK.isInstall
import com.jn.kiku.ttp.pay.cmblife.CmblifeSDK.startCmblife
import com.jn.kikukt.common.utils.ContextUtils
import com.jn.kikukt.common.utils.showToast

/**
 * Author：Stevie.Chen Time：2020/09/08 14:59
 * Class Comment：招行分期支付管理
 */
class CmbLifeManage private constructor() : ICmblifeListener, DefaultLifecycleObserver {
    private val mContext: Context
        get() = ContextUtils.context
    private var onSuccess: (() -> Unit)? = null
    private var onFailure: (() -> Unit)? = null

    companion object {
        private const val payRequestCode = "CmbLifePay" //发起支付请求code
        private const val payResultCode = "respCode" //支付结果code
        private const val paySuccessResultCode = "1000" //支付成功结果code
        private const val payCancelResultCode = "2000" //支付取消结果code

        val instance: CmbLifeManage by lazy(mode = LazyThreadSafetyMode.NONE) { CmbLifeManage() }
    }

    /**
     * 支付
     *
     * @param context  上下文
     * @param protocol 支付参数(掌上生活cmblife协议)
     */
    fun pay(
        context: Context?,
        protocol: String?,
        callBackActivity: Class<*>?,
        onSuccess: (() -> Unit)? = null,
        onFailure: (() -> Unit)? = null
    ) {
        val isInstall = isInstall(context!!)
        if (isInstall) {
            this.onSuccess = onSuccess
            this.onFailure = onFailure
            startCmblife(mContext, protocol, callBackActivity!!, payRequestCode)
        } else {
            showAppNotInstallToast()
        }
    }

    override fun onCmblifeCallBack(requestCode: String?, resultMap: Map<String?, String?>?) {
        if (payRequestCode == requestCode) {
            onCmblifeCallBack(resultMap)
        }
    }

    /**
     * 支付结果回调
     */
    private fun onCmblifeCallBack(resultMap: Map<String?, String?>?) {
        if (resultMap != null) {
            when (resultMap[payResultCode]) {
                paySuccessResultCode -> {
                    showToast("支付成功")
                    onSuccess?.invoke()
                }
                payCancelResultCode -> {
                    showToast("取消支付")
                }
                else -> {
                    showToast("支付失败")
                    onFailure?.invoke()
                }
            }
        }
    }

    /**
     * 处理回调
     */
    fun handleCallBack(intent: Intent?) {
        try {
            handleCallBack(this, intent)
        } catch (e: Exception) {
            e.printStackTrace()
            onFailure?.invoke()
            showToast("支付失败")
        }
    }

    private fun showToast(message: String) {
        mContext.showToast(message)
    }

    /**
     * 显示掌上生活App未安装的提示
     */
    private fun showAppNotInstallToast() {
        Toast.makeText(mContext, "您还未安装掌上生活,请安装掌上生活客户端", Toast.LENGTH_SHORT).show()
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