package com.jn.kikukt.adapter

import android.app.Activity
import android.support.annotation.LayoutRes
import android.support.v4.app.Fragment
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.entity.MultiItemEntity

/**
 * Author：Stevie.Chen Time：2019/7/11
 * Class Comment：
 */
abstract class BaseRvMultiItemAdapter<T : MultiItemEntity> : BaseMultiItemQuickAdapter<T, BaseAdapterViewHolder> {

    private var mImageContext: Any? = null//用于显示图片的context对象

    init {
        setItemType()
    }

    constructor(activity: Activity) : super(null) {
        mImageContext = activity
    }

    constructor(fragment: Fragment) : super(null) {
        mImageContext = fragment
    }

    fun setItemType() {
        val itemTypeArray = getItemType()
        val layoutResourceIdArray = getLayoutResourceId()
        if (itemTypeArray.isEmpty())
            return
        if (layoutResourceIdArray.isEmpty())
            return
        if (itemTypeArray.size != layoutResourceIdArray.size)
            return
        for (i in itemTypeArray.indices) {
            addItemType(itemTypeArray[i], layoutResourceIdArray[i])
        }
    }

    abstract fun getItemType(): IntArray

    @LayoutRes
    abstract fun getLayoutResourceId(): IntArray
}