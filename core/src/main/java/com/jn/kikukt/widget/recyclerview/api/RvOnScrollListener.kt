package com.jn.kikukt.widget.recyclerview.api

import android.support.v7.widget.RecyclerView
import com.jn.kikukt.common.utils.isSlideToBottom

/**
 * Author：Stevie.Chen Time：2019/7/12
 * Class Comment：
 */
class RvOnScrollListener : RecyclerView.OnScrollListener {

    private var mOnScrollListener: OnScrollListener? = null
    private var mOnScrollLastItemListener: OnScrollLastItemListener? = null

    constructor(onScrollListener: OnScrollListener) : super() {
        mOnScrollListener = onScrollListener
    }

    constructor(onScrollLastItemListener: OnScrollLastItemListener) : super() {
        mOnScrollLastItemListener = onScrollLastItemListener
    }

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)
    }

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        mOnScrollListener?.onScrolled(recyclerView, dx, dy)
        if (mOnScrollLastItemListener != null) {
            if (recyclerView?.isSlideToBottom()!! && dy > 0)
                mOnScrollLastItemListener?.onScrolledLastItem(recyclerView, dx, dy)
        }
    }

    /**
     * RecyclerView滑动监听器
     */
    interface OnScrollListener {
        fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int)
    }

    /**
     * RecyclerView滑动到最后一个监听器
     */
    interface OnScrollLastItemListener {
        fun onScrolledLastItem(recyclerView: RecyclerView?, dx: Int, dy: Int)
    }
}