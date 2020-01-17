package com.jn.kikukt.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.PagerAdapter

/**
 * Author：Stevie.Chen Time：2019/7/11
 * Class Comment：BaseFragmentStatePagerAdapter
 */
open class BaseFragmentStatePagerAdapter(
    fm: FragmentManager,
    private val mFragments: MutableList<Fragment>?,
    private val mTitles: MutableList<String>?
) :
    FragmentStatePagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return mFragments?.get(position)!!
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return mTitles?.get(position)
    }

    override fun getCount(): Int {
        return mFragments?.size ?: 0
    }

    override fun getItemPosition(`object`: Any): Int {
        return PagerAdapter.POSITION_NONE
    }

    fun add(position: Int, title: String?, fragment: Fragment?) {
        if (title != null && position < mTitles?.size ?: 0)
            mTitles?.add(position, title)
        if (fragment != null && position < mFragments?.size ?: 0)
            mFragments?.add(position, fragment)
    }

    fun remove(position: Int) {
        if (position < mTitles?.size ?: 0) {
            mTitles?.removeAt(position)
            mFragments?.removeAt(position)
        }
    }
}