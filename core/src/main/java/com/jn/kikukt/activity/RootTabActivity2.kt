package com.jn.kikukt.activity

import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.PagerAdapter
import com.jn.kikukt.R
import com.jn.kikukt.adapter.BaseFragmentPagerAdapter
import com.jn.kikukt.common.api.ITabLayoutView
import kotlinx.android.synthetic.main.common_tab_layout_viewpager.*

/**
 * Author：Stevie.Chen Time：2020/7/15
 * Class Comment：TabLayout + ViewPager
 */
abstract class RootTabActivity2 : RootTbActivity(), ITabLayoutView {

    abstract val fragments: MutableList<Fragment>
    abstract val titles: MutableList<String>

    open val mAdapter: PagerAdapter
        get() = BaseFragmentPagerAdapter(supportFragmentManager, fragments, titles)

    override val layoutResId: Int = R.layout.common_tab_layout_viewpager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initTabView()
        setTabLayoutAttribute()
        setOffscreenPageLimit()
    }

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
        fragments.let { viewpager_RootTab.offscreenPageLimit = it.size }
    }

}