package com.jn.kikukt.adapter

import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.PagerAdapter

/**
 * Author：Stevie.Chen Time：2020/7/15
 * Class Comment：ViewPagerAdapter
 */
abstract class BasePagerAdapter<T> : PagerAdapter() {

    private var mList: MutableList<T>? = mutableListOf()
    private var mViews: SparseArray<View>? = SparseArray()
    private var mOnItemClickListener: OnItemClickListener<T>? = null
    private var mOnItemLongClickListener: OnItemLongClickListener<T>? = null

    abstract val layoutResId: Int

    abstract fun getView(view: View?, position: Int, bean: T?)

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
            view = LayoutInflater.from(container.context).inflate(layoutResId, null, false)
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

abstract class BaseFragmentPagerAdapter(
    fm: FragmentManager, behavior: Int = BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
) :
    FragmentPagerAdapter(fm, behavior) {

    protected val array: SparseArray<Fragment> = SparseArray()

    abstract val fragments: List<Fragment>

    override fun getItem(position: Int): Fragment {
        if (position in 0..count) {
            val fragment: Fragment
            if (array[position] == null) {
                fragment = fragments[position]
                array.put(position, fragment)
            } else {
                fragment = array[position]
            }
            return fragment
        }
        return array[0]
    }

    override fun getCount(): Int {
        return fragments.size
    }

    override fun getItemPosition(`object`: Any): Int {
        return PagerAdapter.POSITION_NONE
    }
}

abstract class BaseFragmentStatePagerAdapter(
    fm: FragmentManager, behavior: Int = BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
) :
    FragmentStatePagerAdapter(fm, behavior) {

    protected val array: SparseArray<Fragment> = SparseArray()

    abstract val fragments: List<Fragment>

    override fun getItem(position: Int): Fragment {
        if (position in 0..count) {
            val fragment: Fragment
            if (array[position] == null) {
                fragment = fragments[position]
                array.put(position, fragment)
            } else {
                fragment = array[position]
            }
            return fragment
        }
        return array[0]
    }

    override fun getCount(): Int {
        return fragments.size
    }

    override fun getItemPosition(`object`: Any): Int {
        return PagerAdapter.POSITION_NONE
    }
}