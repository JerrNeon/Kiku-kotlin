package com.jn.kikukt.common.api

import android.support.annotation.LayoutRes
import com.jn.kikukt.annonation.LoadMoreEnableType
import com.jn.kikukt.annonation.RefreshOperateType
import com.jn.kikukt.annonation.RefreshViewType

/**
 * Author：Stevie.Chen Time：2019/7/10
 * Class Comment：刷新和加载更多
 */
interface IRefreshView {

    /**
     * 获取主内容区域布局
     *
     */
    @LayoutRes
    fun getLayoutItemResourceId(): Int

    /**
     * 获取初始分页索引值
     *
     */
    fun getInitPageIndex(): Int

    /**
     * 获取初始分页大小
     *
     */
    fun getInitPageSize(): Int

    /**
     * 获取分页索引值
     *
     */
    fun getPageIndex(): Int

    /**
     * 获取分页大小
     *
     */
    fun getPageSize(): Int

    /**
     * 获取总大小
     *
     */
    fun getTotalSize(): Int

    /**
     * 获取总页大小
     *
     */
    fun getTotalPage(): Int

    /**
     * 初始化刷新相关控件
     */
    fun initRefreshView()

    /**
     * 获取刷新类型
     *
     */
    @RefreshViewType
    fun getRefreshViewType(): Int

    /**
     * 设置Refresh所在界面当前操作类型
     *
     * @param operateType @RefreshOperateType
     */
    fun setRefreshOperateType(@RefreshOperateType operateType: Int)

    /**
     * 获取是否可以加载更多的判断标志
     */
    @LoadMoreEnableType
    fun getLoadMoreEnableType(): Int

    /**
     * 是否可以加载更多
     *
     * @return true：可以加载更多
     */
    fun isLoadMoreEnable(): Boolean
}