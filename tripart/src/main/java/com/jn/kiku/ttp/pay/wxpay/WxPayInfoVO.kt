package com.jn.kiku.ttp.pay.wxpay

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Author：Stevie.Chen Time：2020/09/08 15:25
 * Class Comment：微信支付信息实体(自己服务器返回)
 */
@Parcelize
data class WxPayInfoVO(
    var appid: String?= null,//	String	公众账号ID
    var partnerid: String?= null,//	String	商户号
    var prepayid: String?= null, //	String	预支付交易会话ID
    var noncestr: String?= null,//	String	随机字符串
    var timestamp: String?= null,//	String	时间戳
    var sign: String?= null, //String	签名
    var orderid: String?= null//	String	商户订单号
) : Parcelable