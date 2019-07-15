package com.jn.kikukt.activity

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.content.ContextCompat
import android.support.v4.view.PagerAdapter
import com.jn.kikukt.R
import com.jn.kikukt.adapter.BaseFragmentPagerAdapter
import com.jn.kikukt.adapter.BaseFragmentStatePagerAdapter
import com.jn.kikukt.common.api.ITabLayoutView
import com.jn.kikukt.utils.getScreenWidth
import com.jn.kikukt.utils.setTabLayoutIndicatorMargin
import kotlinx.android.synthetic.main.common_tab_layout.*

/**
 * Author：Stevie.Chen Time：2019/7/11
 * Class Comment：
 */
abstract class RootTabActivity : RootTbActivity(), ITabLayoutView {

    private var fragments: MutableList<Fragment>? = null
    private var mAdapter: PagerAdapter

    init {
        mAdapter = BaseTabFragmentAdapter(supportFragmentManager, getFragments(), getTitles())
        fragments = getFragments()
    }

    abstract fun getFragments(): MutableList<Fragment>

    abstract fun getTitles(): MutableList<String>

    override fun getLayoutResourceId(): Int {
        return R.layout.common_tab_layout
    }

    override fun initTabView() {
        viewpager_RootTab.adapter = mAdapter
        tabLayout_RootTab.setupWithViewPager(viewpager_RootTab)
    }

    override fun setTabLayoutAttribute() {
        //indicator color
        tabLayout_RootTab.setSelectedTabIndicatorColor(ContextCompat.getColor(mContext, getTabIndicatorColorId()))
        //text color
        tabLayout_RootTab.setTabTextColors(
            ContextCompat.getColor(mContext, getTabNormalTextColorId()),
            ContextCompat.getColor(mContext, getTabSelectedTextColorId())
        )
        //set TabLayout item marginLeft and marginRight
        setTabLayoutIndicatorMargin(getTabItemMargin(), getTabItemMargin())
    }

    override fun setOffscreenPageLimit() {
        fragments?.let { viewpager_RootTab.offscreenPageLimit = it.size }
    }

    override fun setTabLayoutIndicatorMargin(marginLeft: Int?, marginRight: Int?) {
        tabLayout_RootTab.setTabLayoutIndicatorMargin(mContext, marginLeft!!, marginRight!!)
    }

    override fun getTabIndicatorColorId(): Int {
        return R.color.colorPrimaryDark
    }

    override fun getTabNormalTextColorId(): Int {
        return R.color.colorPrimary
    }

    override fun getTabSelectedTextColorId(): Int {
        return R.color.colorPrimaryDark
    }

    override fun getTabItemMargin(): Int? {
        return fragments?.let { mContext.getScreenWidth() / it.size / 10 }
    }
}

class BaseTabFragmentAdapter(
    fm: FragmentManager,
    fragments: MutableList<Fragment>,
    titles: MutableList<String>
) : BaseFragmentPagerAdapter(fm, fragments, titles)

class BaseTabFragmentStateAdapter(
    fm: FragmentManager,
    fragments: MutableList<Fragment>,
    titles: MutableList<String>
) : BaseFragmentStatePagerAdapter(fm, fragments, titles)