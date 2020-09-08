package com.jn.kiku.ttp.pay.unionpay

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Author：Stevie.Chen Time：2020/09/08 15:06
 * Class Comment：银联支付结果实体
 */
@Parcelize
data class UnionPayInfoVO(
    var tn: String? = null,//	String	交易流水号
    var orderId: String? = null,//	String	商户订单号
    var txnAmt: String? = null,//	String	交易金额(单位:分)
    var notify_url: String? = null//	String	服务器异步通知页面路径
) : Parcelable