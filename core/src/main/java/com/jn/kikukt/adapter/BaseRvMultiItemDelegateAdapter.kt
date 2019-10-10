package com.jn.kikukt.adapter

import android.app.Activity

import androidx.fragment.app.Fragment

import com.chad.library.adapter.base.util.MultiTypeDelegate

/**
 * Author：Stevie.Chen Time：2019/10/10
 * Class Comment：多类型
 */
abstract class BaseRvMultiItemDelegateAdapter<T> : BaseRvAdapter<T> {

    init {
        setItemType()
    }

    constructor(activity: Activity) : super(activity)

    constructor(fragment: Fragment) : super(fragment)

    private fun setItemType() {
        val itemViewTypes = itemViewTypes
        val layoutResourceIds = layoutResourceIds
        if (itemViewTypes != null && layoutResourceIds != null && itemViewTypes.isNotEmpty()
            && itemViewTypes.size == layoutResourceIds.size
        ) {
            multiTypeDelegate = object : MultiTypeDelegate<T>() {
                override fun getItemType(t: T): Int {
                    return getItemViewType(t)
                }
            }
            for (i in itemViewTypes.indices) {
                multiTypeDelegate.registerItemType(itemViewTypes[i], layoutResourceIds[i])
            }
        }
    }

    override fun getLayoutResourceId(): Int {
        return 0
    }

    abstract fun getItemViewType(item: T): Int

    abstract val itemViewTypes: IntArray?

    abstract val layoutResourceIds: IntArray?
}
