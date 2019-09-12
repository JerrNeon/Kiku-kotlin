package com.jn.kikukt.common.api

import android.support.annotation.LayoutRes
import android.widget.FrameLayout
import com.jn.kikukt.annonation.*
import com.jn.kikukt.common.Config
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.footer.ClassicsFooter
import com.scwang.smartrefresh.layout.header.ClassicsHeader

/**
 * Author：Stevie.Chen Time：2019/7/10
 * Class Comment：刷新和加载更多
 */
interface IRefreshView {

    var mPageIndex: Int //page info
    var mPageSize: Int//page info
    var mTotalSize: Int//page info
    var mTotalPage: Int//page info
    @RefreshOperateType
    var mRefreshOperateType: Int//operate type
    var mSmartRefreshLayout: SmartRefreshLayout?//root layout
    var mClassicsHeader: ClassicsHeader?//refresh layout
    var mClassicsFooter: ClassicsFooter?//load more layout
    var mFlRootContainer: FrameLayout?//show content layout

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
    fun getInitPageIndex(): Int = Config.PAGE_INDEX

    /**
     * 获取初始分页大小
     *
     */
    fun getInitPageSize(): Int = Config.PAGE_SIZE

    /**
     * 获取分页索引值
     *
     */
    fun getPageIndex(): Int = mPageIndex

    /**
     * 获取分页大小
     *
     */
    fun getPageSize(): Int = mPageSize

    /**
     * 获取总大小
     *
     */
    fun getTotalSize(): Int = mTotalSize

    /**
     * 获取总页大小
     *
     */
    fun getTotalPage(): Int = mTotalPage

    /**
     * 初始化刷新相关控件
     */
    fun initRefreshView()

    /**
     * 获取刷新类型
     *
     */
    @RefreshViewType
    fun getRefreshViewType(): Int = ALL

    /**
     * 设置Refresh所在界面当前操作类型
     *
     * @param operateType @RefreshOperateType
     */
    fun setRefreshOperateType(@RefreshOperateType operateType: Int) {
        mRefreshOperateType = operateType
    }

    /**
     * 获取是否可以加载更多的判断标志
     */
    @LoadMoreEnableType
    fun getLoadMoreEnableType(): Int = EMPTY

    /**
     * 是否可以加载更多
     *
     * @return true：可以加载更多
     */
    fun isLoadMoreEnable(): Boolean = true
}