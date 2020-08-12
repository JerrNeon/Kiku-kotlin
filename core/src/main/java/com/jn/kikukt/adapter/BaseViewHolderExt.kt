package com.jn.kikukt.adapter

import android.view.View
import android.widget.ImageView
import androidx.annotation.IdRes
import com.bumptech.glide.RequestManager
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jn.kikukt.utils.glide.loadImage

/**
 * Author：Stevie.Chen Time：2020/7/14
 * Class Comment：BaseViewHolder Extension
 */
fun BaseViewHolder.setSelected(@IdRes viewId: Int, selected: Boolean): BaseViewHolder {
    getView<View>(viewId).isSelected = selected
    return this
}

fun BaseViewHolder.loadImage(
    viewId: Int,
    url: String,
    requestManager: RequestManager? = null,
    isCache: Boolean = true,
    isGif: Boolean = false,
    isCircle: Boolean = false,
    radius: Int? = null
): BaseViewHolder {
    val view = getView<ImageView>(viewId)
    view.loadImage(
        requestManager ?: view.context,
        url,
        isCache = isCache,
        isGif = isGif,
        isCircle = isCircle,
        radius = radius
    )
    return this
}