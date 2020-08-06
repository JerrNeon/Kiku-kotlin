package com.jn.kikukt.utils

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.jn.kikukt.utils.glide.displayImage

/**
 * Author：Stevie.Chen Time：2020/8/6
 * Class Comment：
 */
object DataBindingAdapter {
    @JvmStatic
    @BindingAdapter("imageUrl")
    fun loadImage(view: ImageView, url: String) {
        view.displayImage(view.context, url)
    }
}