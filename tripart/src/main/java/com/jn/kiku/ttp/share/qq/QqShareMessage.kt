package com.jn.kiku.ttp.share.qq

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Author：Stevie.Chen Time：2020/09/08 15:25
 * Class Comment：QQ分享实体
 */
@Parcelize
data class QqShareMessage(
    var appName: String?,//App名称
    var title: String?, //标题
    var imageUrl: String?, //图片地址
    var summary: String?,//描述
    var targetUrl: String?//链接地址
) : Parcelable