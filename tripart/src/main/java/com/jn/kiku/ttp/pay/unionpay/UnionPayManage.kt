package com.jn.kiku.ttp.pay.unionpay

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.os.Handler
import android.os.Message
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.jn.kikukt.common.utils.logE
import com.jn.kikukt.common.utils.logI
import com.unionpay.UPPayAssistEx
import org.json.JSONException
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.net.URL

/**
 * 银联支付
 */
class UnionPayManage private constructor() : Runnable, DefaultLifecycleObserver {

    //Mode参数解释： "00" - 启动银联正式环境 "01" - 连接银联测试环境
    private val mMode = UnionPayConstants.UNION_OFFICIAL_CONNECT
    private var mHandler: Handler? = null

    //支付结果监听
    private var onSuccess: (() -> Unit)? = null
    private var onFailure: (() -> Unit)? = null

    //支付加载监听
    private var showProgress: (() -> Unit)? = null
    private var dismissProgress: (() -> Unit)? = null

    companion object {
        val instance: UnionPayManage by lazy(mode = LazyThreadSafetyMode.NONE) { UnionPayManage() }
    }

    private fun init(activity: Activity) {
        mHandler = Handler { msg: Message ->
            "UnionPayTn: ${msg.obj}".logI()
            dismissProgress?.invoke()
            if (msg.obj == null || (msg.obj as String).length == 0) {
                AlertDialog.Builder(activity)
                    .setTitle("错误提示")
                    .setMessage("网络连接失败,请重试!")
                    .setPositiveButton("确定") { dialog: DialogInterface?, which: Int -> }.create()
                    .show()
            } else {
                //通过银联工具类启动支付插件
                doStartUnionPayPlugin(activity, msg.obj as String, mMode)
            }
            false
        }
    }

    /**
     * 网络获取交易流水号后进行支付(TN)
     */
    fun startPay(
        activity: Activity,
        onSuccess: (() -> Unit)? = null,
        onFailure: (() -> Unit)? = null,
        showProgress: (() -> Unit)? = null,
        dismissProgress: (() -> Unit)? = null
    ) {
        init(activity)
        this.onSuccess = onSuccess
        this.onFailure = onFailure
        this.showProgress = showProgress
        this.dismissProgress = dismissProgress
        showProgress?.invoke()
        Thread(this@UnionPayManage).start()
    }

    /**
     * 外部获取流水号后进行支付
     *
     * @param tn 流水号
     */
    fun setTnAndStartPay(
        activity: Activity,
        tn: String?,
        onSuccess: (() -> Unit)? = null,
        onFailure: (() -> Unit)? = null,
        showProgress: (() -> Unit)? = null,
        dismissProgress: (() -> Unit)? = null
    ) {
        init(activity)
        this.onSuccess = onSuccess
        this.onFailure = onFailure
        this.showProgress = showProgress
        this.dismissProgress = dismissProgress
        showProgress?.invoke()
        val msg = mHandler?.obtainMessage()
        msg?.obj = tn
        if (msg != null)
            mHandler!!.sendMessage(msg)
    }

    /**
     * 测试的时候采用此方法，从指定网站获取流水号
     */
    override fun run() {
        var tn: String? = null
        val `is`: InputStream
        try {
            val myURL = URL(UnionPayConstants.GET_TN_URL)
            val ucon = myURL.openConnection()
            ucon.connectTimeout = 120000
            `is` = ucon.getInputStream()
            var i = -1
            val baos = ByteArrayOutputStream()
            while (`is`.read().also { i = it } != -1) {
                baos.write(i)
            }
            tn = baos.toString()
            `is`.close()
            baos.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        val msg = mHandler!!.obtainMessage()
        msg.obj = tn
        mHandler!!.sendMessage(msg)
    }

    /**
     * @param mode 0 - 启动银联正式环境,1 - 连接银联测试环境
     */
    private fun doStartUnionPayPlugin(activity: Activity, tn: String, mode: String) {
        val ret = UPPayAssistEx.startPay(activity, null, null, tn, mode)
        "UnionPayResult: $ret".logI()
        if (!UPPayAssistEx.checkWalletInstalled(activity)) //是否安装了银联Apk
            return
        if (ret == UnionPayConstants.PLUGIN_NEED_UPGRADE || ret == UnionPayConstants.PLUGIN_NOT_INSTALLED) {
            // 需要重新安装控件(更新)
            " plugin not found or need upgrade!!!".logI()
            AlertDialog.Builder(activity)
                .setTitle("提示")
                .setMessage("完成购买需要安装银联支付控件，是否安装？")
                .setPositiveButton("确定") { _: DialogInterface?, _: Int ->
                    // 目前使用的内置在assets文件夹中的apk，如果不考虑版本问题，应该使用下载链接
                    UPPayAssistEx.installUPPayPlugin(activity)
                }
                .setNegativeButton("取消") { _: DialogInterface?, _: Int -> }
                .create().show()
        }
    }

    /**
     * 支付界面onActivityResult()方法中实现此方法
     * 支付控件返回字符串:success、fail、cancel 分别代表支付成功，支付失败，支付取消
     */
    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (data == null) return
        val bundle = data.extras
        var str: String? = null
        if (bundle != null) {
            str = bundle.getString("pay_result")
        }
        if (str == null) return
        if (str.equals("success", ignoreCase = true)) {
            // 如果想对结果数据验签，可使用下面这段代码，但建议不验签，直接去商户后台查询交易结果
            if (data.hasExtra("result_data")) {
                val result = data.extras!!.getString("result_data")
                try {
                    val resultJson: JSONObject
                    var sign: String? = ""
                    var dataOrg: String? = ""
                    if (result != null) {
                        resultJson = JSONObject(result)
                        sign = resultJson.getString("sign")
                        dataOrg = resultJson.getString("data")
                    }
                    // 此处的verify建议送去商户后台做验签
                    // 如要放在手机端验，则代码必须支持更新证书
                    val ret = RSAUtil.verify(dataOrg, sign, mMode)
                    if (ret) {
                        // 验签成功，显示支付结果
                        "onActivityResult: 验签后支付成功".logI()
                        onSuccess?.invoke()
                    } else {
                        // 验签失败
                        "onActivityResult: 验签后支付失败".logE()
                    }
                } catch (e: JSONException) {
                    //
                }
            }
            // 结果result_data为成功时，去商户后台查询一下再展示成功
            "onActivityResult: 验签前支付成功".logI()
        } else if (str.equals("fail", ignoreCase = true)) {
            "onActivityResult: 支付失败".logE()
            onFailure?.invoke()
        } else if (str.equals("cancel", ignoreCase = true)) {
            "onActivityResult: 用户取消了支付".logI()
        }
    }

    private fun onDestroy() {
        if (onSuccess != null) onSuccess = null
        if (onFailure != null) onFailure = null
        if (showProgress != null) showProgress = null
        if (dismissProgress != null) dismissProgress = null
        if (mHandler != null) mHandler = null
    }

    override fun onDestroy(owner: LifecycleOwner) {
        onDestroy()
        super.onDestroy(owner)
    }
}