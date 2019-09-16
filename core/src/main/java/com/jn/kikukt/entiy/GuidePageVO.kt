package com.jn.kikukt.entiy

import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.DrawableRes

/**
 * Author：Stevie.Chen Time：2019/7/10
 * Class Comment：引导页
 */
data class GuidePageVO(
    val position: Int,//位置
    @DrawableRes val imgRes: Int,//本地图片资源
    val imgUrl: String? = "",//网络图片资源
    val imgType: Int,//0：网络图片  1：本地图片
    val isLast: Boolean//是否最后一页
) :
    Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readByte() != 0.toByte()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(position)
        parcel.writeInt(imgRes)
        parcel.writeString(imgUrl)
        parcel.writeInt(imgType)
        parcel.writeByte(if (isLast) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<GuidePageVO> {
        override fun createFromParcel(parcel: Parcel): GuidePageVO {
            return GuidePageVO(parcel)
        }

        override fun newArray(size: Int): Array<GuidePageVO?> {
            return arrayOfNulls(size)
        }
    }
}