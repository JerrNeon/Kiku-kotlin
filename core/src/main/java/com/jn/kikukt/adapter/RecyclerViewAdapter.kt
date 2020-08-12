package com.jn.kikukt.adapter

import androidx.annotation.LayoutRes
import com.bumptech.glide.RequestManager
import com.chad.library.adapter.base.BaseDelegateMultiAdapter
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.delegate.BaseMultiTypeDelegate
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.chad.library.adapter.base.viewholder.BaseViewHolder

/**
 * Author：Stevie.Chen Time：2020/7/15
 * Class Comment：RecyclerViewAdapter
 */
abstract class BaseRvAdapter<T>(
    @LayoutRes layoutResId: Int,
    protected val requestManager: RequestManager? = null,
    data: MutableList<T>? = null
) :
    BaseQuickAdapter<T, BaseViewHolder>(layoutResId, data)

/**
 * 多类型
 */
abstract class BaseRvMultiItemAdapter<T : MultiItemEntity> :
    BaseMultiItemQuickAdapter<T, BaseViewHolder>() {

    abstract val itemTypes: IntArray
    abstract val layoutResIds: IntArray

    init {
        setItemType()
    }

    open fun setItemType() {
        val itemTypeArray = itemTypes
        val layoutResIdArray = layoutResIds
        if (itemTypeArray.isEmpty())
            return
        if (layoutResIdArray.isEmpty())
            return
        if (itemTypeArray.size != layoutResIdArray.size)
            return
        for (i in itemTypeArray.indices) {
            addItemType(itemTypeArray[i], layoutResIdArray[i])
        }
    }
}

/**
 * 多类型
 */
abstract class BaseRvDelegateMultiAdapter<T> : BaseDelegateMultiAdapter<T, BaseViewHolder>() {

    abstract val itemTypes: IntArray
    abstract val layoutResIds: IntArray

    abstract fun getItemViewType(data: List<T>, position: Int): Int

    init {
        setItemType()
    }

    private fun setItemType() {
        val itemTypeArray = itemTypes
        val layoutResIdArray = layoutResIds
        if (itemTypeArray.isEmpty())
            return
        if (layoutResIdArray.isEmpty())
            return
        if (itemTypeArray.size != layoutResIdArray.size)
            return
        setMultiTypeDelegate(object : BaseMultiTypeDelegate<T>() {
            override fun getItemType(data: List<T>, position: Int): Int {
                return getItemViewType(data, position)
            }
        })
        val multiTypeDelegate = getMultiTypeDelegate()
        for (i in itemTypeArray.indices) {
            multiTypeDelegate?.addItemType(itemTypeArray[i], layoutResIdArray[i])
        }
    }
}