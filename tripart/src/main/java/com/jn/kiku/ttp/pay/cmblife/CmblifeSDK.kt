// -------------------------------------------------------------------------------------
// CMB Confidential
//
// Copyright (C) 2015年8月3日 China Merchants Bank Co., Ltd. All rights reserved.
//
// No part of this file may be reproduced or transmitted in any form or by any
// means,
// electronic, mechanical, photocopying, recording, or otherwise, without prior
// written permission of China Merchants Bank Co., Ltd.
//
// -------------------------------------------------------------------------------------
package com.jn.kiku.ttp.pay.cmblife

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Base64
import org.json.JSONObject
import java.net.URLEncoder
import java.util.*

/**
 * 掌上生活SDK
 */
object CmblifeSDK {
    /**
     * 获取掌上生活SDK版本
     */
    const val version = "1"
    private const val CMBLIFE_PACKAGE_NAME = "com.cmbchina.ccd.pluto.cmbActivity"
    private const val CMBLIFE_ENTRANCE_ACTIVITY =
        "com.cmbchina.ccd.pluto.cmbActivity.open.OpenSplashActivity"
    private const val URL_CMBLIFE_REDIRECT =
        "http://cmblife.cmbchina.com/cmblife/download/mchAppRedirect.html"
    private const val URL_CMBLIFE_DOWNLOAD =
        "http://cmblife.cmbchina.com/cmblife/download/mchAppDownload.html"
    private const val ACTION_TYPE = "android.intent.action.VIEW"
    private const val PROTOCOL = "protocol"
    private const val REQUEST_CODE = "requestCode"
    private const val RESULT = "result"
    private const val PACKAGE_NAME = "packageName"
    private const val CALL_BACK_ACTIVITY = "callBackActivity"
    private const val CMBLIFE_SIGN =
        ("MIICQjCCAasCBB4pSYMwDQYJKoZIhvcNAQEFBQAwZzEQMA4GA1UEAxMHY21ibGlmZTEQMA4GA1UE"
                + "CxMHVW5rbm93bjEQMA4GA1UEChMHVW5rbm93bjEQMA4GA1UEBxMHVW5rbm93bjEQMA4GA1UECBMH"
                + "VW5rbm93bjELMAkGA1UEBhMCQ04wIBcNMTIxMTE5MTYwMDAwWhgPMjEwMDExMTkxNjAwMDBaMGcx"
                + "EDAOBgNVBAMTB2NtYmxpZmUxEDAOBgNVBAsTB1Vua25vd24xEDAOBgNVBAoTB1Vua25vd24xEDAO"
                + "BgNVBAcTB1Vua25vd24xEDAOBgNVBAgTB1Vua25vd24xCzAJBgNVBAYTAkNOMIGfMA0GCSqGSIb3"
                + "DQEBAQUAA4GNADCBiQKBgQCXP4lLiYD95wrV0k+2eawqnkTA7WkCt17NaBGTJzBYSfFKerD65D0t"
                + "TXKWe5GmST/+ckfOSnhXQK2Mk5euvOEAJqHkW83WIXBx5WZAkenUvm0d4y7vnbAjPtIDmEAZdsIK"
                + "WhVE/qfXSi2Phu00xENZ4uXWPiADm37wGMR2sBp64wIDAQABMA0GCSqGSIb3DQEBBQUAA4GBADsx"
                + "jU5EPWfi/J2Ju6BceL0JVzBKTj0MDLDMsfyH3qkVwcNN2ZLXaX5ik2IinVc3FytvptrDSp9sKHzB"
                + "o33yvvjhLTtFPs1TWa60VaUmwODnFAuOnnus0vb0YybtP73EeJRH3dGwcI18pfSAutenhl4HEWdH"
                + "mZNCxffPQ+cqI3Wy")
    private const val TIPS_1 = "您的掌上生活版本过低或未能正确安装，请从官方渠道重新下载"
    private const val TIPS_2 = "您的掌上生活可能已被篡改，请从官方渠道重新下载"

    /**
     * 判断掌上生活是否安装
     */
    @JvmStatic
    fun isInstall(context: Context): Boolean {
        return try {
            val pm = context.packageManager
            pm.getPackageInfo(CMBLIFE_PACKAGE_NAME, 0)
            true
        } catch (e: Exception) {
            // 若发生异常，则认为没有安装
            false
        }
    }

    /**
     * 跳转至下载页面下载掌上生活
     */
    fun downloadCmblife(context: Context) {
        startWebExplorer(context, URL_CMBLIFE_DOWNLOAD)
    }

    /**
     * 重定向至浏览器
     */
    private fun startRedirectPage(context: Context, protocol: String) {
        startWebExplorer(context, URL_CMBLIFE_REDIRECT + "?protocol=" + URLEncoder.encode(protocol))
    }

    /**
     * 唤起浏览器
     */
    private fun startWebExplorer(context: Context, url: String) {
        val intent = Intent()
        intent.action = ACTION_TYPE
        val contentUrl = Uri.parse(url)
        intent.data = contentUrl
        context.startActivity(intent)
    }

    /**
     * 唤起掌上生活
     */
    @JvmStatic
    fun startCmblife(
        context: Context,
        protocol: String?, callBackActivity: Class<*>, requestCode: String?
    ): String {
        return try {
            val intent = Intent()
            val cn = ComponentName(CMBLIFE_PACKAGE_NAME, CMBLIFE_ENTRANCE_ACTIVITY)
            intent.component = cn
            intent.putExtra(PACKAGE_NAME, context.packageName)
            intent.putExtra(CALL_BACK_ACTIVITY, callBackActivity.name)
            intent.putExtra(PROTOCOL, protocol)
            intent.putExtra(REQUEST_CODE, requestCode)
            intent.flags =
                Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED or Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
            ""
        } catch (e: Exception) {
            e.printStackTrace()
            TIPS_1
        }
    }

    /**
     * 处理掌上生活回调逻辑
     */
    @JvmStatic
    @Throws(Exception::class)
    fun handleCallBack(listener: ICmblifeListener?, intent: Intent?) {
        if (null == listener || null == intent) {
            return
        }
        val result = intent.getStringExtra(RESULT)
        val requestCode = intent.getStringExtra(REQUEST_CODE)
        if (null != result && "" != result) {
            val resultMap = jsonStringToMap(result)
            listener.onCmblifeCallBack(requestCode, resultMap)
        }
    }

    /**
     * json报文转map
     */
    @Throws(Exception::class)
    private fun jsonStringToMap(jsonString: String): Map<String, String> {
        val jsonObject = JSONObject(jsonString)
        val resultMap: MutableMap<String, String> = HashMap()
        val iterator = jsonObject.keys()
        var key: String
        var value: String
        while (iterator.hasNext()) {
            key = iterator.next()
            value = jsonObject.getString(key)
            resultMap[key] = value
        }
        return resultMap
    }

    /**
     * 获取掌上生活公钥
     */
    private fun getPubKey(context: Context): String? {
        return try {
            val pm = context.packageManager
            val info = pm.getPackageInfo(CMBLIFE_PACKAGE_NAME, PackageManager.GET_SIGNATURES)
            var sign = Base64.encodeToString(info.signatures[0].toByteArray(), Base64.DEFAULT)
            sign = sign.replace("\n", "")
            sign
        } catch (e: Exception) {
            null
        }
    }
}