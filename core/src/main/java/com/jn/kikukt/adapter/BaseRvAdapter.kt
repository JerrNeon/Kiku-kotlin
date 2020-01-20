package com.jn.kikukt.adapter

import android.app.Activity
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import com.chad.library.adapter.base.BaseQuickAdapter

/**
 * Author：Stevie.Chen Time：2019/7/10
 * Class Comment：BaseAdapter
 */
abstract class BaseRvAdapter<T> : BaseQuickAdapter<T, BaseAdapterViewHolder> {

    protected var mActivity: Activity? = null
    protected var mFragment: Fragment? = null

    abstract val layoutResourceId: Int

    init {
        mLayoutResId = layoutResourceId
    }

    constructor(activity: Activity) : super(null) {
        mActivity = activity
    }

    constructor(fragment: Fragment) : super(null) {
        mActivity = fragment.activity
        mFragment = fragment
    }

    /**
     * If you have added headeview, the notification view refreshes.
     * Do not need to care about the number of headview, only need to pass in the position of the final view
     */
    fun refreshNotifyItemChanged(position: Int, @Nullable payload: Any) {
        notifyItemChanged(position, payload)
    }

    override fun onBindViewHolder(holder: BaseAdapterViewHolder, position: Int) {
        if (mFragment != null)
            holder.bindImageContext(mFragment!!)
        else
            holder.bindImageContext(mActivity!!)
        super.onBindViewHolder(holder, position)
    }
}