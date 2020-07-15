package com.jn.kikukt.adapter

import android.view.View
import android.widget.ImageView
import androidx.annotation.IdRes
import com.bumptech.glide.RequestManager
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jn.kikukt.utils.glide.displayImage

/**
 * Author：Stevie.Chen Time：2020/7/14
 * Class Comment：BaseViewHolder Extension
 */
fun BaseViewHolder.setSelected(@IdRes viewId: Int, selected: Boolean): BaseViewHolder {
    getView<View>(viewId).isSelected = selected
    return this
}

fun BaseViewHolder.displayImage(
    viewId: Int,
    requestManager: RequestManager?,
    url: String,
    isCache: Boolean = true,
    isGif: Boolean = false,
    isCircle: Boolean = false,
    radius: Int? = null
): BaseViewHolder {
    requestManager?.let {
        getView<ImageView>(viewId).displayImage(
            it,
            url,
            isCache = isCache,
            isGif = isGif,
            isCircle = isCircle,
            radius = radius
        )
    }
    return this
}