package com.jn.kikukt.entiy

import android.os.Parcel
import android.os.Parcelable

/**
 * Author：Stevie.Chen Time：2019/7/15
 * Class Comment：分享实体
 */
data class ShareVO(val img: Int, val title: String?) : Parcelable {
    constructor(source: Parcel) : this(
        source.readInt(),
        source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeInt(img)
        writeString(title)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<ShareVO> = object : Parcelable.Creator<ShareVO> {
            override fun createFromParcel(source: Parcel): ShareVO = ShareVO(source)
            override fun newArray(size: Int): Array<ShareVO?> = arrayOfNulls(size)
        }
    }
}