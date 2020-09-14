package com.jn.kikukt.adapter

import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter

/**
 * Author：Stevie.Chen Time：2020/8/4
 * Class Comment：BaseQuickAdapter Extension
 */
fun <T> BaseQuickAdapter<T, *>.listener(block: BaseQuickAdapterListener<T>.() -> Unit) {
    BaseQuickAdapterListener<T>().apply {
        block()
        getSpanSize?.let {
            setGridSpanSizeLookup(it)
        }
        onItemClick?.let {
            setOnItemClickListener { adapter, view, position ->
                it.invoke(
                    adapter,
                    view,
                    position,
                    getItem(position)
                )
            }
        }
        onItemChildClick?.let {
            setOnItemChildClickListener { adapter, view, position ->
                it.invoke(
                    adapter,
                    view,
                    position,
                    getItem(position)
                )
            }
        }
        onItemLongClick?.let {
            setOnItemLongClickListener { adapter, view, position ->
                it.invoke(
                    adapter,
                    view,
                    position,
                    getItem(position)
                )
            }
        }
        onItemChildLongClick?.let {
            setOnItemChildLongClickListener { adapter, view, position ->
                it.invoke(
                    adapter,
                    view,
                    position,
                    getItem(position)
                )
            }
        }
    }
}

class BaseQuickAdapterListener<T> {
    internal var getSpanSize: ((gridLayoutManager: GridLayoutManager, viewType: Int, position: Int) -> Int)? =
        null
    internal var onItemClick: ((adapter: BaseQuickAdapter<*, *>, view: View, position: Int, item: T) -> Unit)? =
        null
    internal var onItemChildClick: ((adapter: BaseQuickAdapter<*, *>, view: View, position: Int, item: T) -> Unit)? =
        null
    internal var onItemLongClick: ((adapter: BaseQuickAdapter<*, *>, view: View, position: Int, item: T) -> Boolean)? =
        null
    internal var onItemChildLongClick: ((adapter: BaseQuickAdapter<*, *>, view: View, position: Int, item: T) -> Boolean)? =
        null

    fun getSpanSize(block: (gridLayoutManager: GridLayoutManager, viewType: Int, position: Int) -> Int) {
        getSpanSize = block
    }

    fun onItemClick(block: (adapter: BaseQuickAdapter<*, *>, view: View, position: Int, item: T) -> Unit) {
        onItemClick = block
    }

    fun onItemChildClick(block: (adapter: BaseQuickAdapter<*, *>, view: View, position: Int, item: T) -> Unit) {
        onItemChildClick = block
    }

    fun onItemLongClick(block: (adapter: BaseQuickAdapter<*, *>, view: View, position: Int, item: T) -> Boolean) {
        onItemLongClick = block
    }

    fun onItemChildLongClick(block: (adapter: BaseQuickAdapter<*, *>, view: View, position: Int, item: T) -> Boolean) {
        onItemChildLongClick = block
    }
}