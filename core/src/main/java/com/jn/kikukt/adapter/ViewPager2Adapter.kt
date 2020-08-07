package com.jn.kikukt.adapter

import android.util.SparseArray
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

/**
 * Author：Stevie.Chen Time：2020/7/15
 * Class Comment：ViewPager2Adapter
 */

class BaseFragmentStateAdapter : FragmentStateAdapter {

    private val array: SparseArray<Fragment> = SparseArray()

    private var fragments: List<Fragment>

    constructor(fragment: Fragment, fragments: List<Fragment>) : super(fragment) {
        this.fragments = fragments
    }

    constructor(fragmentActivity: FragmentActivity, fragments: List<Fragment>) : super(
        fragmentActivity
    ) {
        this.fragments = fragments
    }

    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment {
        if (position in 0..itemCount) {
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
}