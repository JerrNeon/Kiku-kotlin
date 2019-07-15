package com.jn.kikukt.common.api

import android.support.annotation.ColorRes

/**
 * Author：Stevie.Chen Time：2019/7/11
 * Class Comment：TabLayout
 */
interface ITabLayoutView {

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

    /**
     * 设置TabLayout子项左右margin值
     *
     * @param marginLeft  左margin
     * @param marginRight 右margin
     */
    fun setTabLayoutIndicatorMargin(marginLeft: Int?, marginRight: Int?)

    /**
     * 获取指示器颜色
     *
     * @return
     */
    @ColorRes
    fun getTabIndicatorColorId(): Int

    /**
     * 获取未选中文字颜色
     *
     * @return
     */
    @ColorRes
    fun getTabNormalTextColorId(): Int

    /**
     * 获取选中文字颜色
     *
     * @return
     */
    @ColorRes
    fun getTabSelectedTextColorId(): Int

    /**
     * 获取子项左右margin
     *
     * @return
     */
    fun getTabItemMargin(): Int?
}