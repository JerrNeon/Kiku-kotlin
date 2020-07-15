package com.jn.kikukt.common.api

import com.jn.kikukt.R

/**
 * Author：Stevie.Chen Time：2019/7/11
 * Class Comment：TabLayout
 */
interface ITabLayoutView {

    val tabIndicatorColorId: Int
        get() = R.color.colorPrimaryDark//指示器颜色
    val tabNormalTextColorId: Int
        get() = R.color.colorPrimary//未选中文字颜色
    val tabSelectedTextColorId: Int
        get() = R.color.colorPrimaryDark//选中文字颜色
    val isTabIndicatorFullWidth: Boolean
        get() = false//Indicator是否是与Tab同宽度(false：与文字同宽度)

    /**
     * 初始化TabLayout相关控件
     */
    fun initTabView()

    /**
     * 设置TabLayout样式
     */
    fun setTabLayoutAttribute()

    /**
     * 设置ViewPager的预加载数量(没有使用懒加载的Fragment无需调用此方法)
     * 注：设置了预加载为总Tab的个数后，每次点击Tab时就不会再去重新请求数据(还没找到更好的方法解决真正的懒加载问题)
     */
    fun setOffscreenPageLimit()

}