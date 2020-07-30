package com.jn.kikukt.common.utils.recyclerview

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

/**
 * Author：Stevie.Chen Time：2019/7/15
 * Class Comment：用于设置RecyclerView布局为GridLayoutManager或StaggeredGridLayoutManager时合并多个item
 */

fun RecyclerView.onAttachedToRecyclerView(
    innerAdapter: RecyclerView.Adapter<*>,
    callback: (
        layoutManager: GridLayoutManager,
        oldLookup: GridLayoutManager.SpanSizeLookup,
        position: Int
    ) -> Int
) {
    innerAdapter.onAttachedToRecyclerView(this)
    val layoutManager = layoutManager
    if (layoutManager is GridLayoutManager) {
        val spanSizeLookup = layoutManager.spanSizeLookup
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return callback.invoke(layoutManager, spanSizeLookup, position)
            }
        }
        layoutManager.spanCount = layoutManager.spanCount
    }
}

fun RecyclerView.ViewHolder.setFullSpan() {
    val lp = itemView.layoutParams
    if (lp is StaggeredGridLayoutManager.LayoutParams) {
        lp.isFullSpan = true
    }
}