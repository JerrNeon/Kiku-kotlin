package com.jn.kikukt.utils.glide

import android.content.Context
import androidx.recyclerview.widget.RecyclerView

/**
 * Author：Stevie.Chen Time：2019/7/15
 * Class Comment：RecyclerView中Glide加载优化
 */
class GlideOnScrollListener(val context: Context) : RecyclerView.OnScrollListener() {

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)
        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
            GlideUtil.resumeRequests(context)
        } else {
            GlideUtil.pauseRequests(context)
        }
    }
}