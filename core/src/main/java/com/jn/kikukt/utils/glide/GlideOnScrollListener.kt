package com.jn.kikukt.utils.glide

import android.app.Activity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager

/**
 * Author：Stevie.Chen Time：2019/7/15
 * Class Comment：RecyclerView中Glide加载优化
 */
class GlideOnScrollListener : RecyclerView.OnScrollListener {

    private var requestManager: RequestManager

    constructor(activity: Activity) : super() {
        requestManager = activity.requestManager()
    }

    constructor(fragment: Fragment) : super() {
        requestManager = fragment.requestManager()
    }

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)
        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
            GlideUtil.resumeRequests(requestManager)
        } else {
            GlideUtil.pauseRequests(requestManager)
        }
    }
}