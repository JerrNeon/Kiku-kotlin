package com.jn.kikukt.adapter

import android.app.Activity
import android.content.Context
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.PagerAdapter
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * Author：Stevie.Chen Time：2019/7/10
 * Class Comment：BasePagerAdapter
 */
abstract class BasePagerAdapter<T> : PagerAdapter {

    private var mContext: Context? = null
    private var mImageContext: Any? = null//show network Image context
    private var mList: MutableList<T>? = mutableListOf()
    private var mViews: SparseArray<View>? = SparseArray()
    var mOnItemClickListener: OnItemClickListener<T>? = null
    var mOnItemLongClickListener: OnItemLongClickListener<T>? = null

    @LayoutRes
    abstract fun getLayoutResourceId(): Int

    abstract fun getView(view: View?, position: Int, bean: T?)

    fun getContext(): Context? = mContext

    fun getImageContext(): Any? = mImageContext

    constructor(activity: Activity) : super() {
        mContext = activity.applicationContext
        mImageContext = activity
    }

    constructor(fragment: Fragment) : super() {
        mContext = fragment.activity?.applicationContext
        mImageContext = fragment
    }

    interface OnItemClickListener<T> {
        fun onItemCLick(position: Int, bean: T?)
    }

    interface OnItemLongClickListener<T> {
        fun onItemLongClick(position: Int, bean: T?)
    }

    override fun getCount(): Int {
        return mList?.size ?: 0
    }

    override fun isViewFromObject(p0: View, p1: Any): Boolean {
        return p0 == p1
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        var view: View? = mViews?.get(position)
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(getLayoutResourceId(), null, false)
            mViews?.put(position, view)
        }
        getView(view, position, mList?.get(position))
        container.addView(view)
        view!!.setOnClickListener {
            mOnItemClickListener?.onItemCLick(position, mList?.get(position))
        }
        view.setOnLongClickListener(View.OnLongClickListener {
            mOnItemLongClickListener?.onItemLongClick(position, mList?.get(position))
            return@OnLongClickListener mOnItemLongClickListener != null
        })
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        super.destroyItem(container, position, `object`)
        container.removeView(mViews?.get(position))
    }

    fun getData(): List<T>? {
        return mList
    }

    fun add(bean: T) {
        mList?.add(bean)
    }

    fun addAll(beans: List<T>) {
        if (beans.isNotEmpty()) {
            mList?.addAll(beans)
        }
    }

    fun remove(position: Int): T? {
        return if (position in 0 until count) {
            mList?.removeAt(position)
        } else null
    }

    fun clear() {
        mList?.clear()
    }

    fun isEmpty(): Boolean? {
        return mList?.isEmpty()
    }

    fun isNotEmpty(): Boolean? {
        return mList?.isNotEmpty()
    }

}