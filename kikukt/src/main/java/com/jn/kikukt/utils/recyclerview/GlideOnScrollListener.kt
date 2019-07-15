package com.jn.kikukt.utils.recyclerview

import android.content.Context
import android.support.v7.widget.RecyclerView
import com.jn.kikukt.utils.glide.GlideUtil

/**
 * Author：Stevie.Chen Time：2019/7/15
 * Class Comment：RecyclerView中Glide加载优化
 */
class GlideOnScrollListener(val context: Context) : RecyclerView.OnScrollListener() {

    override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)
        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
            GlideUtil.resumeRequests(context)
        } else {
            GlideUtil.pauseRequests(context)
        }
    }
}