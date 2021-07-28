package com.jn.kikukt.dialog

import android.view.View
import android.view.ViewGroup
import com.jn.kikukt.R
import com.jn.kikukt.annonation.*
import com.jn.kikukt.common.api.IRefreshView
import com.jn.kikukt.common.api.IViewModelView
import com.jn.kikukt.net.coroutines.HttpViewModel
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.ClassicsHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.scwang.smart.refresh.layout.api.RefreshLayout

/**
 * Author：Stevie.Chen Time：2021/7/28
 * Class Comment：
 */
abstract class RootRefreshDialogFragment(open val itemLayoutResId: Int) : RootDialogFragment(),
    IRefreshView, IViewModelView {

    final override val mInitPageIndex: Int
        get() = super.mInitPageIndex//page info
    final override val mInitPageSize: Int
        get() = super.mInitPageSize//page info
    override var mPageIndex: Int = mInitPageIndex//page info
    override var mPageSize: Int = mInitPageSize//page info
    override var mTotalSize: Int = 0//page info
    override var mTotalPage: Int = 0//page info
    override var mRefreshOperateType: Int = 0//operate type
    override lateinit var mSmartRefreshLayout: SmartRefreshLayout//root layout
    override var mClassicsHeader: ClassicsHeader? = null//refresh layout
    override var mClassicsFooter: ClassicsFooter? = null//load more layout
    override val viewModel: HttpViewModel? = null
    override val layoutResId: Int
        get() = R.layout.common_refresh_layout

    override fun initView(view: View) {
        super.initView(view)
        mRefreshOperateType = ON_CREATE
        initRefreshView()
    }

    override fun showProgressDialog(type: Int, isCancelable: Boolean) {
        if (mRefreshOperateType == ON_CREATE || mRefreshOperateType == ON_RELOAD)
            super.showProgressDialog(type, isCancelable)
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        mRefreshOperateType = ON_REFRESH
        mSmartRefreshLayout.resetNoMoreData()//reset no more data origin status
        mPageIndex = mInitPageIndex
        onRequest()
    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {
        mRefreshOperateType = ON_ONLOADMORE
        if (mLoadMoreEnableType == EMPTY) {
            mPageIndex++
            onRequest()
        } else {
            if (isLoadMoreEnable) {
                mPageIndex++
                onRequest()
            } else {
                mSmartRefreshLayout.finishLoadMoreWithNoMoreData()//load complete and no more data
            }
        }
    }

    override fun initRefreshView() {
        view?.run {
            mSmartRefreshLayout = findViewById(R.id.srl_root)
            mClassicsHeader = findViewById(R.id.srlH_root)
            mClassicsFooter = findViewById(R.id.srlF_root)
        }
        if (itemLayoutResId != 0) {
            mSmartRefreshLayout.setRefreshContent(
                layoutInflater.inflate(itemLayoutResId, mSmartRefreshLayout, false),
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
            )
        }

        mSmartRefreshLayout.run {
            when (mRefreshViewType) {
                ALL -> {
                    setEnableRefresh(true)
                    setEnableLoadMore(true)
                }
                NONE -> {
                    setEnableRefresh(false)
                    setEnableLoadMore(false)
                }
                ONLY_REFRESH -> {
                    setEnableRefresh(true)
                    setEnableLoadMore(false)
                }
                else -> {
                    setEnableRefresh(false)
                    setEnableLoadMore(true)
                }
            }
            setOnRefreshLoadMoreListener(this@RootRefreshDialogFragment)//refresh and more listener
            setEnableAutoLoadMore(true)//SmartRefreshLayout Api
            setEnableScrollContentWhenLoaded(true)//SmartRefreshLayout Api
            setEnableFooterFollowWhenNoMoreData(true)//SmartRefreshLayout Api
        }
    }
}