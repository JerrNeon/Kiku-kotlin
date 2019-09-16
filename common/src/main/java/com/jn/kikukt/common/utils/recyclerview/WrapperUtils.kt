package com.jn.kikukt.common.utils.recyclerview

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

/**
 * Author：Stevie.Chen Time：2019/7/15
 * Class Comment：用于设置RecyclerView布局为GridLayoutManager或StaggeredGridLayoutManager时合并多个item
 */
class WrapperUtils {

    companion object{
        fun onAttachedToRecyclerView(
            innerAdapter: RecyclerView.Adapter<*>,
            recyclerView: RecyclerView,
            callback: SpanSizeCallback
        ) {
            innerAdapter.onAttachedToRecyclerView(recyclerView)

            val layoutManager = recyclerView.layoutManager
            if (layoutManager is GridLayoutManager) {
                val spanSizeLookup = layoutManager.spanSizeLookup

                layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                    override fun getSpanSize(position: Int): Int {
                        return callback.getSpanSize(layoutManager, spanSizeLookup, position)
                    }
                }
                layoutManager.spanCount = layoutManager.spanCount
            }
        }

        fun setFullSpan(holder: RecyclerView.ViewHolder) {
            val lp = holder.itemView.layoutParams
            if (lp != null && lp is StaggeredGridLayoutManager.LayoutParams) {
                lp.isFullSpan = true
            }
        }
    }

    interface SpanSizeCallback {
        fun getSpanSize(
            layoutManager: GridLayoutManager,
            oldLookup: GridLayoutManager.SpanSizeLookup,
            position: Int
        ): Int
    }
}