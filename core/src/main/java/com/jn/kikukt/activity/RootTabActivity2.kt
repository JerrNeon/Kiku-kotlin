package com.jn.kikukt.activity

import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.jn.kikukt.R
import com.jn.kikukt.adapter.BaseFragmentPagerAdapter
import com.jn.kikukt.common.api.ITabLayoutView

/**
 * Author：Stevie.Chen Time：2020/7/15
 * Class Comment：TabLayout + ViewPager
 */
abstract class RootTabActivity2 : RootTbActivity(R.layout.common_tab_layout_viewpager),
    ITabLayoutView {

    abstract val fragments: MutableList<Fragment>
    abstract val titles: MutableList<String>

    open val mAdapter: PagerAdapter
        get() = BaseFragmentPagerAdapter(supportFragmentManager, fragments, titles)

    protected lateinit var mViewPager: ViewPager
    protected lateinit var mTabLayout: TabLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initTabView()
        setTabLayoutAttribute()
        setOffscreenPageLimit()
    }

    override fun initTabView() {
        mViewPager = findViewById(R.id.viewpager_RootTab)
        mTabLayout = findViewById(R.id.tabLayout_RootTab)
        mViewPager.adapter = mAdapter
        titles.forEach { title ->
            mTabLayout.addTab(mTabLayout.newTab().setText(title))
        }
        mTabLayout.setupWithViewPager(mViewPager)
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
            isTabIndicatorFullWidth = this@RootTabActivity2.isTabIndicatorFullWidth
        }
    }

    override fun setOffscreenPageLimit() {
        fragments.let { mViewPager.offscreenPageLimit = it.size }
    }

}