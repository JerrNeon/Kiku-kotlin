package com.jn.kikukt.utils

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.jn.kikukt.utils.glide.loadImage

/**
 * Author：Stevie.Chen Time：2020/8/6
 * Class Comment：
 */
object DataBindingAdapter {
    @JvmStatic
    @BindingAdapter("imageUrl")
    fun loadImage(view: ImageView, url: String) {
        view.loadImage(view.context, url)
    }
}