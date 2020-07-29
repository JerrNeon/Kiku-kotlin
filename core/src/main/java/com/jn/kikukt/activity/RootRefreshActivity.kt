package com.jn.kikukt.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.jn.kikukt.R
import com.jn.kikukt.annonation.*
import com.jn.kikukt.common.api.IMvpView
import com.jn.kikukt.common.api.IRefreshView
import com.jn.kikukt.mvp.IBPresenter
import com.jn.kikukt.mvp.IBView
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.ClassicsHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.scwang.smart.refresh.layout.api.RefreshLayout
import kotlinx.android.synthetic.main.common_refresh_layout.*

/**
 * Author：Stevie.Chen Time：2019/7/10
 * Class Comment：
 */
abstract class RootRefreshActivity : RootTbActivity(), IRefreshView {

    override var mPageIndex: Int = mInitPageIndex//page info
    override var mPageSize: Int = mInitPageSize//page info
    override var mTotalSize: Int = 0//page info
    override var mTotalPage: Int = 0//page info
    override var mRefreshOperateType: Int = 0//operate type
    override lateinit var mSmartRefreshLayout: SmartRefreshLayout//root layout
    override var mClassicsHeader: ClassicsHeader? = null//refresh layout
    override var mClassicsFooter: ClassicsFooter? = null//load more layout
    override var mFlRootContainer: FrameLayout? = null//show content layout

    override val layoutResourceId: Int = R.layout.common_refresh_layout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mRefreshOperateType = ON_CREATE
        initRefreshView()
    }

    override fun showProgressDialog(type: Int) {
        if (mRefreshOperateType == ON_CREATE || mRefreshOperateType == ON_RELOAD)
            super.showProgressDialog(type)
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
        mSmartRefreshLayout = srl_root
        mClassicsHeader = srlH_root
        mClassicsFooter = srlF_root
        mFlRootContainer = fl_rootContainer
        if (layoutItemResourceId != 0) {
            mFlRootContainer?.addView(
                LayoutInflater.from(mContext).inflate(layoutItemResourceId, null, false),
                FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.MATCH_PARENT
                )
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
            setOnRefreshLoadMoreListener(this@RootRefreshActivity)//refresh and more listener
            setEnableAutoLoadMore(true)//SmartRefreshLayout Api
            setEnableScrollContentWhenLoaded(true)//SmartRefreshLayout Api
            setEnableFooterFollowWhenNoMoreData(true)//SmartRefreshLayout Api
        }
    }
}

abstract class RootRefreshPresenterActivity<P : IBPresenter> : RootRefreshActivity(),
    IMvpView<P> {

    override var mPresenter: P? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initPresenter()
    }

    override fun initPresenter() {
        super.initPresenter()
        mPresenter?.let {
            it.attachView(this as? IBView)
            lifecycle.addObserver(it)
        }
    }
}