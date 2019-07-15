package com.jn.kikukt.adapter

import android.app.Activity
import android.support.annotation.LayoutRes
import android.support.v4.app.Fragment
import com.chad.library.adapter.base.BaseQuickAdapter

/**
 * Author：Stevie.Chen Time：2019/7/10
 * Class Comment：BaseAdapter
 */
abstract class BaseRvAdapter<T> : BaseQuickAdapter<T, BaseAdapterViewHolder> {

    private var mImageContext: Any? = null//用于显示图片的context对象

    init {
        mLayoutResId = getLayoutResourceId()
    }

    constructor(activity: Activity) : super(0, null) {
        mImageContext = activity
    }

    constructor(fragment: Fragment) : super(0, null) {
        mImageContext = fragment
    }

    fun getImageContext(): Any? = mImageContext

    @LayoutRes
    abstract fun getLayoutResourceId(): Int
}