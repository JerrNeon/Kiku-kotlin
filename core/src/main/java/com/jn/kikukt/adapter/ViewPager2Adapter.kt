package com.jn.kikukt.adapter

import android.util.SparseArray
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

/**
 * Author：Stevie.Chen Time：2020/7/15
 * Class Comment：ViewPager2Adapter
 */

abstract class BaseFragmentStateAdapter : FragmentStateAdapter {

    protected val array: SparseArray<Fragment> = SparseArray()

    abstract val fragments: List<Fragment>

    constructor(fragment: Fragment) : super(fragment)

    constructor(fragmentActivity: FragmentActivity) : super(fragmentActivity)

    constructor(fragmentManager: FragmentManager, lifecycle: Lifecycle) : super(
        fragmentManager,
        lifecycle
    )

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