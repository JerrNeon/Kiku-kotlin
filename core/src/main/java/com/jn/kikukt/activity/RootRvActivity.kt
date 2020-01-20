package com.jn.kikukt.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jn.kikukt.R
import com.jn.kikukt.annonation.*
import com.jn.kikukt.common.api.IRvView
import com.jn.kikukt.common.utils.cast
import com.jn.kikukt.common.utils.isConnected
import com.jn.kikukt.net.coroutines.Failure
import com.jn.kikukt.net.coroutines.HttpResponse
import com.jn.kikukt.net.coroutines.Success
import com.scwang.smartrefresh.layout.api.RefreshLayout
import kotlinx.android.synthetic.main.common_rv.*

/**
 * Author：Stevie.Chen Time：2019/7/10
 * Class Comment：
 */
abstract class RootRvActivity<T> : RootRefreshActivity(), IRvView<T> {

    override var mRecyclerView: RecyclerView? = null//RecyclerView
    override var mEmptyView: View? = null//empty or failure view
    override var mIvLoadingFailure: ImageView? = null//empty or failure icon
    override var mTvLoadingFailure: TextView? = null//empty or failure hint text
    //RecyclerView.LayoutManager
    override val mLayoutManager: RecyclerView.LayoutManager by lazy {
        LinearLayoutManager(
            applicationContext
        )
    }
    //[Observer]
    override val observer: Observer<HttpResponse> by lazy {
        Observer<HttpResponse> {
            when (it) {
                is Success<*> -> {
                    it.data?.cast<List<T>>()?.let { data ->
                        showLoadSuccessView(data)
                    }
                }
                is Failure -> {
                    showLoadErrorView()
                }
            }
        }
    }

    override val layoutItemResourceId: Int = R.layout.common_rv

    override val isLoadMoreEnable: Boolean
        get() =
            when (mLoadMoreEnableType) {
                TOTAL -> mAdapter.itemCount < mTotalSize
                PAGE -> mPageIndex < mTotalPage
                else -> super.isLoadMoreEnable
            }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initRvView()
        sendRequest()
    }

    override fun sendRequest() {
        super.sendRequest()
        showProgressDialog()
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        mAdapter.setNewData(null)//clear data
        super.onRefresh(refreshLayout)
    }

    override fun initRvView() {
        mRecyclerView = rv_common
        mRecyclerView?.run {
            layoutManager = mLayoutManager
            adapter = mAdapter
        }

        mEmptyView = LayoutInflater.from(mContext)
            .inflate(R.layout.common_loadingfailure, mFlRootContainer, false)
        mEmptyView?.run {
            mIvLoadingFailure = findViewById(R.id.iv_commonLoadingFailure)
            mTvLoadingFailure = findViewById(R.id.tv_commonLoadingFailure)
            setOnClickListener { view -> onClickLoadFailure(view) }
        }
        mAdapter.run {
            bindToRecyclerView(mRecyclerView)
            emptyView = mEmptyView//set empty or failure view
            isUseEmpty(false)//don t show empty or failure view first
            onItemClickListener = this@RootRvActivity
            onItemChildClickListener = this@RootRvActivity
            onItemLongClickListener = this@RootRvActivity
            onItemChildLongClickListener = this@RootRvActivity
        }
    }

    override fun showLoadCompleteView(loadCompleteType: Int, data: List<T>) {
        dismissProgressDialog()
        mSmartRefreshLayout?.run {
            finishRefresh()//refresh complete
            finishLoadMore()//load more complete
        }
        val refreshViewType = mRefreshViewType
        if (loadCompleteType == SUCCESS) {//load success
            if (data.isNotEmpty()) {//有数据
                mSmartRefreshLayout?.run {
                    setEnableRefresh(refreshViewType == ONLY_REFRESH || refreshViewType == ALL)
                    setEnableLoadMore(refreshViewType == ONLY_LOADMORE || refreshViewType == ALL)
                }
                mAdapter.run {
                    isUseEmpty(false)//don t show empty or failure view
                    if (mPageIndex == mInitPageIndex)
                        setNewData(data)
                    else
                        addData(data)
                }
            } else if (mPageIndex == mInitPageIndex) {//first page no data
                setLoadFailureResource(R.drawable.ic_kiku_nodata, R.string.kiku_load_nodata)
                mSmartRefreshLayout?.run {
                    setEnableRefresh(false)//invalid refresh
                    setEnableLoadMore(false)//invalid load more
                }
                mAdapter.run {
                    isUseEmpty(true)//show empty or failure view
                    setNewData(null)
                }
            } else if (mPageIndex > mInitPageIndex) {//next page no data
                if (mLoadMoreEnableType == EMPTY) {
                    mAdapter.isUseEmpty(false)//don t show empty or failure view
                    mSmartRefreshLayout?.finishLoadMoreWithNoMoreData()//all data load complete
                }
            }
            mEmptyView?.isEnabled = false//enable false
        } else if (loadCompleteType == ERROR) {//no net or load failure
            if (mPageIndex == mInitPageIndex) {//first page
                mSmartRefreshLayout?.run {
                    setEnableRefresh(false)//invalid refresh
                    setEnableLoadMore(false)//invalid load more
                }
                mAdapter.run {
                    isUseEmpty(true)//show empty or failure view
                    setNewData(null)
                }
                if (!isConnected()) {//no net
                    setLoadFailureResource(R.drawable.ic_kiku_nonet, R.string.kiku_load_nonet)
                } else {//other reason(connect failure or server error)
                    setLoadFailureResource(R.drawable.ic_kiku_nonet, R.string.kiku_load_failure)
                }
                mEmptyView?.isEnabled = true//enable true
            } else {
                if (refreshViewType == ONLY_LOADMORE || refreshViewType == ALL) {
                    mPageIndex--//reset pageIndex when load more failure
                    mSmartRefreshLayout?.finishLoadMore(false)//load more failure
                }
            }
        }
    }

    override fun showLoadSuccessView(total: Int, data: List<T>) {
        if (mLoadMoreEnableType == TOTAL)
            mTotalSize = total
        else if (mLoadMoreEnableType == PAGE)
            mTotalPage = total
        showLoadSuccessView(data)
    }

    override fun onClickLoadFailure(view: View) {
        mRefreshOperateType = ON_RELOAD
        mPageIndex = mInitPageIndex
        sendRequest()
    }

}