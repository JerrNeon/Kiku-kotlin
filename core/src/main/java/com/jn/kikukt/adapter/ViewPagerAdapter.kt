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
abstract class BasePagerAdapter<T>(
    private val layoutResId: Int,
    var list: MutableList<T> = mutableListOf()
) : PagerAdapter() {

    private var mViews: SparseArray<View?> = SparseArray()
    var onItemCLick: ((position: Int, bean: T) -> Unit)? = null
    var onItemLongClick: ((position: Int, bean: T) -> Unit)? = null
    open val onView: ((view: View?, position: Int, bean: T) -> Unit)? = null

    override fun getCount(): Int {
        return list.size
    }

    override fun isViewFromObject(p0: View, p1: Any): Boolean {
        return p0 == p1
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view: View = if (mViews.get(position) != null) {
            mViews.get(position)!!
        } else {
            val view1 =
                LayoutInflater.from(container.context).inflate(layoutResId, container, false)
            mViews.put(position, view1)
            view1
        }.apply {
            setOnClickListener {
                onItemCLick?.invoke(position, list[position])
            }
            setOnLongClickListener(View.OnLongClickListener {
                onItemLongClick?.invoke(position, list[position])
                return@OnLongClickListener onItemLongClick != null
            })
        }
        onView?.invoke(view, position, list[position])
        container.addView(view)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        super.destroyItem(container, position, `object`)
        container.removeView(mViews.get(position))
    }
}

class BaseFragmentPagerAdapter(
    fm: FragmentManager,
    private val fragments: List<Fragment>,
    behavior: Int = BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
) :
    FragmentPagerAdapter(fm, behavior) {

    private val array: SparseArray<Fragment> = SparseArray()

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

class BaseFragmentStatePagerAdapter(
    fm: FragmentManager,
    private val fragments: List<Fragment>,
    behavior: Int = BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
) :
    FragmentStatePagerAdapter(fm, behavior) {

    private val array: SparseArray<Fragment> = SparseArray()

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