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
    isCache: Boolean = true,
    isGif: Boolean = false
): BaseViewHolder {
    val view = getView<ImageView>(viewId)
    val context = itemView.tag as? RequestManager ?: view.context
    view.loadImage(
        context,
        url,
        isCache = isCache,
        isGif = isGif,
    )
    return this
}