package com.jn.kikukt.common.api

import com.jn.kikukt.annonation.*
import com.jn.kikukt.common.Config
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.ClassicsHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener

/**
 * Author：Stevie.Chen Time：2019/7/10
 * Class Comment：刷新和加载更多
 */
interface IRefreshView : OnRefreshLoadMoreListener {

    val mInitPageIndex: Int
        get() = Config.PAGE_INDEX//page info(init page index)
    val mInitPageSize: Int
        get() = Config.PAGE_SIZE//page info(init page size)
    var mPageIndex: Int //page info(page index)
    var mPageSize: Int//page info(page size)
    var mTotalSize: Int//page info(total size)
    var mTotalPage: Int//page info(total page)
    @RefreshViewType
    val mRefreshViewType: Int
        get() = ALL//refresh type(刷新类型)
    @LoadMoreEnableType
    val mLoadMoreEnableType: Int
        get() = EMPTY//load more type(是否可以加载更多的判断标志)
    val isLoadMoreEnable: Boolean
        get() = true//isLoadMoreEnable(是否可以加载更多)
    @RefreshOperateType
    var mRefreshOperateType: Int//operate type(Refresh所在界面当前操作类型)
    var mSmartRefreshLayout: SmartRefreshLayout//root layout
    var mClassicsHeader: ClassicsHeader?//refresh layout
    var mClassicsFooter: ClassicsFooter?//load more layout

    val layoutItemResId: Int//获取主区域内容布局Id

    /**
     * 初始化刷新相关控件
     */
    fun initRefreshView()
}