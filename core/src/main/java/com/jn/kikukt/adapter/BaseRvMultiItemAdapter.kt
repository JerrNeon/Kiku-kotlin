package com.jn.kikukt.adapter

import android.app.Activity
import androidx.fragment.app.Fragment
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.entity.MultiItemEntity

/**
 * Author：Stevie.Chen Time：2019/7/11
 * Class Comment：
 */
abstract class BaseRvMultiItemAdapter<T : MultiItemEntity> :
    BaseMultiItemQuickAdapter<T, BaseAdapterViewHolder> {

    private var mImageContext: Any? = null//用于显示图片的context对象

    abstract val itemType: IntArray
    abstract val layoutResourceId: IntArray

    init {
        setItemType()
    }

    constructor(activity: Activity) : super(null) {
        mImageContext = activity
    }

    constructor(fragment: Fragment) : super(null) {
        mImageContext = fragment
    }

    open fun setItemType() {
        val itemTypeArray = itemType
        val layoutResourceIdArray = layoutResourceId
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

}