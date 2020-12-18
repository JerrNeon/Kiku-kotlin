package com.jn.kikukt.activity

import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.jn.kikukt.R
import com.jn.kikukt.adapter.BaseFragmentStateAdapter
import com.jn.kikukt.common.api.ITabLayoutView

/**
 * Author：Stevie.Chen Time：2020/7/15
 * Class Comment：TabLayout + ViewPager2
 */
abstract class RootTabActivity : RootTbActivity(R.layout.common_tab_layout_viewpager2),
    ITabLayoutView, TabLayoutMediator.TabConfigurationStrategy {

    abstract val fragments: MutableList<Fragment>
    abstract val titles: MutableList<String>

    open val mAdapter: FragmentStateAdapter
        get() = BaseFragmentStateAdapter(this, fragments)

    protected lateinit var mViewPager: ViewPager2
    protected lateinit var mTabLayout: TabLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initTabView()
        setTabLayoutAttribute()
    }

    override fun initTabView() {
        mViewPager = findViewById(R.id.viewpager_RootTab)
        mTabLayout = findViewById(R.id.tabLayout_RootTab)
        mViewPager.adapter = mAdapter
        TabLayoutMediator(mTabLayout, mViewPager, this).attach()
    }

    override fun setTabLayoutAttribute() {
        mTabLayout.run {
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