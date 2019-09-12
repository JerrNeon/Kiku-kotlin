package com.jn.kikukt.entiy

import android.os.Parcel
import android.os.Parcelable

/**
 * Author：Stevie.Chen Time：2019/7/11
 * Class Comment：版本更新信息
 */
data class VersionUpdateVO(
    val appName: String?,//App名称
    val appIconResId: Int,//App图标
    val downLoadUrl: String?,//下载地址
    val versionNum: Int,//版本号
    val content: String?,//更新内容
    val forceUpdate: Int//是否强制更新 0:不强制  1：强制
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(appName)
        parcel.writeInt(appIconResId)
        parcel.writeString(downLoadUrl)
        parcel.writeInt(versionNum)
        parcel.writeString(content)
        parcel.writeInt(forceUpdate)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<VersionUpdateVO> {
        override fun createFromParcel(parcel: Parcel): VersionUpdateVO {
            return VersionUpdateVO(parcel)
        }

        override fun newArray(size: Int): Array<VersionUpdateVO?> {
            return arrayOfNulls(size)
        }
    }
}