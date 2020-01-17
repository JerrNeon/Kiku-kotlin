package com.jn.kikukt.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.jn.kikukt.R
import com.jn.kikukt.annonation.*
import com.jn.kikukt.common.api.IRefreshView
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.footer.ClassicsFooter
import com.scwang.smartrefresh.layout.header.ClassicsHeader
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener
import kotlinx.android.synthetic.main.common_refresh_layout.view.*

/**
 * Author：Stevie.Chen Time：2019/7/11
 * Class Comment：
 */
abstract class RootRefreshFragment : RootFragment(), IRefreshView, OnRefreshLoadMoreListener {

    override var mPageIndex: Int = mInitPageIndex//page info
    override var mPageSize: Int = mInitPageSize//page info
    override var mTotalSize: Int = 0//page info
    override var mTotalPage: Int = 0//page info
    override var mRefreshOperateType: Int = 0//operate type
    override var mSmartRefreshLayout: SmartRefreshLayout? = null//root layout
    override var mClassicsHeader: ClassicsHeader? = null//refresh layout
    override var mClassicsFooter: ClassicsFooter? = null//load more layout
    override var mFlRootContainer: FrameLayout? = null//show content layout

    override fun getLayoutResourceId(): Int {
        return R.layout.common_refresh_layout
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mRefreshOperateType = ON_CREATE
        initRefreshView()
    }

    override fun showProgressDialog() {
        if (mRefreshOperateType == ON_CREATE || mRefreshOperateType == ON_RELOAD)
            super.showProgressDialog()
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        mRefreshOperateType = ON_REFRESH
        mSmartRefreshLayout?.resetNoMoreData()//reset no more data origin status
        mPageIndex = mInitPageIndex
        sendRequest()
    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {
        mRefreshOperateType = ON_ONLOADMORE
        if (mLoadMoreEnableType == EMPTY) {
            mPageIndex++
            sendRequest()
        } else {
            if (isLoadMoreEnable) {
                mPageIndex++
                sendRequest()
            } else {
                mSmartRefreshLayout?.finishLoadMoreWithNoMoreData()//load complete and no more data
            }
        }
    }

    override fun initRefreshView() {
        mSmartRefreshLayout = mView!!.srl_root
        mClassicsHeader = mView!!.srlH_root
        mClassicsFooter = mView!!.srlF_root
        mFlRootContainer = mView!!.fl_rootContainer
        if (layoutItemResourceId != 0) {
            mFlRootContainer?.addView(
                LayoutInflater.from(mContext).inflate(layoutItemResourceId, null, false),
                FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.MATCH_PARENT
                )
            )
        }

        mSmartRefreshLayout?.run {
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
            setOnRefreshLoadMoreListener(this@RootRefreshFragment)//refresh and more listener
            setEnableAutoLoadMore(true)//SmartRefreshLayout Api
            setEnableScrollContentWhenLoaded(true)//SmartRefreshLayout Api
            setEnableFooterFollowWhenNoMoreData(true)//SmartRefreshLayout Api
        }
    }

}