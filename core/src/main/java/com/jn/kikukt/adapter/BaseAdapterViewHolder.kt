package com.jn.kikukt.adapter

import android.app.Activity
import android.content.Context
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.ImageView
import com.chad.library.adapter.base.BaseViewHolder
import com.jn.kikukt.utils.glide.*

/**
 * Author：Stevie.Chen Time：2019/7/15
 * Class Comment：
 */
class BaseAdapterViewHolder(view: View?) : BaseViewHolder(view) {

    private var mContext: Context? = null
    private var mImageContext: Any? = null//用于显示图片的context对象

    fun setVisibility(viewId: Int, visibility: Int): BaseAdapterViewHolder {
        val view = getView<View>(viewId)
        view.visibility = visibility
        return this
    }

    fun setSelected(viewId: Int, selected: Boolean): BaseAdapterViewHolder {
        val view = getView<View>(viewId)
        view.isSelected = selected
        return this
    }

    override fun setText(viewId: Int, value: CharSequence?): BaseViewHolder {
        return super.setText(viewId, value ?: "")
    }

    fun bindImageContext(activity: Activity) {
        mContext = activity.applicationContext
        mImageContext = activity
    }

    fun bindImageContext(fragment: Fragment) {
        mContext = fragment.context
        mImageContext = fragment
    }

    fun getImageContext(): Any? {
        return mImageContext
    }

    fun displayImage(viewId: Int, url: String): BaseAdapterViewHolder {
        val view = getView<ImageView>(viewId)
        mImageContext?.let { view.displayImage(it, url) }
        return this
    }

    fun displayRoundImage(viewId: Int, url: String, radius: Int): BaseAdapterViewHolder {
        val view = getView<ImageView>(viewId)
        mImageContext?.let { view.displayRoundImage(it, url, radius) }
        return this
    }

    fun displayCircleImage(viewId: Int, url: String): BaseAdapterViewHolder {
        val view = getView<ImageView>(viewId)
        mImageContext?.let { view.displayCircleImage(it, url) }
        return this
    }

    fun displayAvatar(viewId: Int, url: String): BaseAdapterViewHolder {
        val view = getView<ImageView>(viewId)
        mImageContext?.let { view.displayAvatar(it, url) }
        return this
    }

    fun displayCircleAvatar(viewId: Int, url: String): BaseAdapterViewHolder {
        val view = getView<ImageView>(viewId)
        mImageContext?.let { view.displayCircleAvatar(it, url) }
        return this
    }

}