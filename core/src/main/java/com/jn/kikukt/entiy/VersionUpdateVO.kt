package com.jn.kikukt.entiy

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Author：Stevie.Chen Time：2019/7/11
 * Class Comment：版本更新信息
 */
@Parcelize
data class VersionUpdateVO(
    val appName: String?,//App名称
    val appIconResId: Int,//App图标
    val downLoadUrl: String?,//下载地址
    val versionNum: Int,//版本号
    val content: String?,//更新内容
    val forceUpdate: Int//是否强制更新 0:不强制  1：强制
) : Parcelable