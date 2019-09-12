package com.jn.kikukt.activity

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
import kotlinx.android.synthetic.main.common_refresh_layout.*

/**
 * Author：Stevie.Chen Time：2019/7/10
 * Class Comment：
 */
abstract class RootRefreshActivity : RootTbActivity(), IRefreshView, OnRefreshLoadMoreListener {

    override var mPageIndex: Int = 0//page info
    override var mPageSize: Int = 0//page info
    override var mTotalSize: Int = 0//page info
    override var mTotalPage: Int = 0//page info
    @RefreshOperateType
    override var mRefreshOperateType: Int = 0//operate type
    override var mSmartRefreshLayout: SmartRefreshLayout? = null//root layout
    override var mClassicsHeader: ClassicsHeader? = null//refresh layout
    override var mClassicsFooter: ClassicsFooter? = null//load more layout
    override var mFlRootContainer: FrameLayout? = null//show content layout

    override fun getLayoutResourceId(): Int {
        return R.layout.common_refresh_layout
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setRefreshOperateType(ON_CREATE)
        initRefreshView()
    }

    override fun showProgressDialog() {
        if (mRefreshOperateType == ON_CREATE || mRefreshOperateType == ON_RELOAD)
            super.showProgressDialog()
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        setRefreshOperateType(ON_REFRESH)
        mSmartRefreshLayout?.resetNoMoreData()//reset no more data origin status
        mPageIndex = getPageIndex()
        sendRequest()
    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {
        setRefreshOperateType(ON_ONLOADMORE)
        if (getLoadMoreEnableType() == EMPTY) {
            mPageIndex++
            sendRequest()
        } else {
            if (isLoadMoreEnable()) {
                mPageIndex++
                sendRequest()
            } else {
                mSmartRefreshLayout?.finishLoadMoreWithNoMoreData()//load complete and no more data
            }
        }
    }

    override fun initRefreshView() {
        mPageIndex = getInitPageIndex()
        mPageSize = getInitPageSize()

        mSmartRefreshLayout = srl_root
        mClassicsHeader = srlH_root
        mClassicsFooter = srlF_root
        mFlRootContainer = fl_rootContainer
        if (getLayoutItemResourceId() != 0) {
            mFlRootContainer?.addView(
                LayoutInflater.from(mContext).inflate(getLayoutItemResourceId(), null, false),
                FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.MATCH_PARENT
                )
            )
        }

        when (getRefreshViewType()) {
            ALL -> {
                mSmartRefreshLayout?.setEnableRefresh(true)
                mSmartRefreshLayout?.setEnableLoadMore(true)
            }
            NONE -> {
                mSmartRefreshLayout?.setEnableRefresh(false)
                mSmartRefreshLayout?.setEnableLoadMore(false)
            }
            ONLY_REFRESH -> {
                mSmartRefreshLayout?.setEnableRefresh(true)
                mSmartRefreshLayout?.setEnableLoadMore(false)
            }
            else -> {
                mSmartRefreshLayout?.setEnableRefresh(false)
                mSmartRefreshLayout?.setEnableLoadMore(true)
            }
        }
        mSmartRefreshLayout?.setOnRefreshLoadMoreListener(this)//refresh and more listener
        mSmartRefreshLayout?.setEnableAutoLoadMore(true)//SmartRefreshLayout Api
        mSmartRefreshLayout?.setEnableScrollContentWhenLoaded(true)//SmartRefreshLayout Api
        mSmartRefreshLayout?.setEnableFooterFollowWhenNoMoreData(true)//SmartRefreshLayout Api
    }

}