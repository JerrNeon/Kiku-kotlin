package com.jn.kikukt.entiy

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Author：Stevie.Chen Time：2019/7/15
 * Class Comment：分享实体
 */
@Parcelize
data class ShareVO(val img: Int, val title: String?) : Parcelable