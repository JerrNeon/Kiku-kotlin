package com.jn.kiku.ttp.pay.alipay

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Author：Stevie.Chen Time：2020/09/08 11:56
 * Class Comment：支付宝支付信息实体(自己服务器返回)
 */
@Parcelize
data class AliPayInfoVO(
    var orderNumber: String? = null,
    var subject: String? = null,
    var body: String? = null,
    var price: String? = null
) : Parcelable