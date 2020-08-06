package com.jn.kikukt.activity

import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.jn.kikukt.R
import com.jn.kikukt.adapter.BaseFragmentStateAdapter
import com.jn.kikukt.common.api.ITabLayoutView
import kotlinx.android.synthetic.main.common_tab_layout_viewpager2.*

/**
 * Author：Stevie.Chen Time：2020/7/15
 * Class Comment：TabLayout + ViewPager2
 */
abstract class RootTabActivity : RootTbActivity(), ITabLayoutView,
    TabLayoutMediator.TabConfigurationStrategy {

    abstract val fragments: MutableList<Fragment>
    abstract val titles: MutableList<String>

    open var mAdapter: FragmentStateAdapter = object : BaseFragmentStateAdapter(this) {
        override val fragments: List<Fragment>
            get() = this@RootTabActivity.fragments
    }

    override val layoutResId: Int = R.layout.common_tab_layout_viewpager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initTabView()
        setTabLayoutAttribute()
    }

    override fun initTabView() {
        viewpager_RootTab.adapter = mAdapter
        TabLayoutMediator(tabLayout_RootTab, viewpager_RootTab, this).attach()
    }

    override fun setTabLayoutAttribute() {
        tabLayout_RootTab.run {
            //indicator color
            setSelectedTabIndicatorColor(
                ContextCompat.getColor(
                    applicationContext,
                    tabIndicatorColorId
                )
            )
            //text color
            setTabTextColors(
                ContextCompat.getColor(applicationContext, tabNormalTextColorId),
                ContextCompat.getColor(applicationContext, tabSelectedTextColorId)
            )
            //TabIndicatorFullWidth
            isTabIndicatorFullWidth = this@RootTabActivity.isTabIndicatorFullWidth
        }
    }

    override fun setOffscreenPageLimit() {}

    override fun onConfigureTab(tab: TabLayout.Tab, position: Int) {
        tab.text = titles[position]
    }
}