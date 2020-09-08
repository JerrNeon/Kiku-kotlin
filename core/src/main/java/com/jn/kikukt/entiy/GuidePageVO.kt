package com.jn.kikukt.entiy

import android.os.Parcelable
import androidx.annotation.DrawableRes
import kotlinx.android.parcel.Parcelize

/**
 * Author：Stevie.Chen Time：2019/7/10
 * Class Comment：引导页
 */
@Parcelize
data class GuidePageVO(
    val position: Int,//位置
    @DrawableRes val imgRes: Int,//本地图片资源
    val imgUrl: String? = "",//网络图片资源
    val imgType: Int,//0：网络图片  1：本地图片
    val isLast: Boolean//是否最后一页
) : Parcelable