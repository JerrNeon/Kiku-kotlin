package com.jn.kikukt.activity

import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.PagerAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.jn.kikukt.R
import com.jn.kikukt.adapter.BaseFragmentPagerAdapter
import com.jn.kikukt.common.api.ITabLayoutView
import com.jn.kikukt.common.utils.getScreenWidth
import kotlinx.android.synthetic.main.common_tab_layout_viewpager.*

/**
 * Author：Stevie.Chen Time：2020/7/15
 * Class Comment：TabLayout + ViewPager
 */
abstract class RootTabActivity2 : RootTbActivity(), ITabLayoutView {

    abstract val fragments: MutableList<Fragment>
    abstract val titles: MutableList<String>

    open var mAdapter: PagerAdapter = object : BaseFragmentPagerAdapter(supportFragmentManager) {
        override val fragments: List<Fragment>
            get() = this@RootTabActivity2.fragments
    }

    override val layoutResourceId: Int = R.layout.common_tab_layout_viewpager

    override fun initTabView() {
        viewpager_RootTab.adapter = mAdapter
        titles.forEach { tabLayout_RootTab.addTab(tabLayout_RootTab.newTab().setText(it)) }
        tabLayout_RootTab.setupWithViewPager(viewpager_RootTab)
    }

    override fun setTabLayoutAttribute() {
        tabLayout_RootTab.run {
            //indicator color
            setSelectedTabIndicatorColor(
                ContextCompat.getColor(
                    mContext,
                    tabIndicatorColorId
                )
            )
            //text color
            setTabTextColors(
                ContextCompat.getColor(mContext, tabNormalTextColorId),
                ContextCompat.getColor(mContext, tabSelectedTextColorId)
            )
            //TabIndicatorFullWidth
            isTabIndicatorFullWidth = this@RootTabActivity2.isTabIndicatorFullWidth
        }
    }

    override fun setOffscreenPageLimit() {
        fragments.let { viewpager_RootTab.offscreenPageLimit = it.size }
    }

}