package com.jn.kikukt.activity

import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.viewpager.widget.PagerAdapter
import com.jn.kikukt.R
import com.jn.kikukt.adapter.BaseFragmentPagerAdapter
import com.jn.kikukt.adapter.BaseFragmentStatePagerAdapter
import com.jn.kikukt.common.api.ITabLayoutView
import com.jn.kikukt.common.utils.getScreenWidth
import com.jn.kikukt.common.utils.setTabLayoutIndicatorMargin
import kotlinx.android.synthetic.main.common_tab_layout.*

/**
 * Author：Stevie.Chen Time：2019/7/11
 * Class Comment：
 */
abstract class RootTabActivity : RootTbActivity(), ITabLayoutView {

    abstract val fragments: MutableList<Fragment>
    abstract val titles: MutableList<String>
    private var mAdapter: PagerAdapter
    override val tabItemMargin: Int? by lazy {
        mContext.getScreenWidth() / fragments.size / 10
    }

    init {
        mAdapter = BaseTabFragmentAdapter(supportFragmentManager, fragments, titles)
    }

    override val layoutResourceId: Int = R.layout.common_tab_layout

    override fun initTabView() {
        viewpager_RootTab.adapter = mAdapter
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
            //
            isTabIndicatorFullWidth = this@RootTabActivity.isTabIndicatorFullWidth
        }
        //set TabLayout item marginLeft and marginRight
        //setTabLayoutIndicatorMargin(tabItemMargin, tabItemMargin)
    }

    override fun setOffscreenPageLimit() {
        fragments.let { viewpager_RootTab.offscreenPageLimit = it.size }
    }

    override fun setTabLayoutIndicatorMargin(marginLeft: Int?, marginRight: Int?) {
        tabLayout_RootTab.setTabLayoutIndicatorMargin(mContext, marginLeft!!, marginRight!!)
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