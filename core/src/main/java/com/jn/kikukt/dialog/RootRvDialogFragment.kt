package com.jn.kikukt.dialog

import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jn.kikukt.R
import com.jn.kikukt.annonation.*
import com.jn.kikukt.common.api.IRvView
import com.jn.kikukt.common.utils.cast
import com.jn.kikukt.common.utils.isConnected
import com.jn.kikukt.net.retrofit.Failure
import com.jn.kikukt.net.retrofit.HttpResponse
import com.jn.kikukt.net.retrofit.Success
import com.scwang.smart.refresh.layout.api.RefreshLayout

/**
 * Author：Stevie.Chen Time：2021/7/28
 * Class Comment：
 */
abstract class RootRvDialogFragment<T> : RootRefreshDialogFragment(R.layout.common_rv), IRvView<T> {

    override lateinit var mRecyclerView: RecyclerView//RecyclerView
    override val emptyViewResId: Int
        get() = R.layout.common_loadingfailure//empty or failure view id

    //RecyclerView.LayoutManager
    override val mLayoutManager: RecyclerView.LayoutManager
        get() = LinearLayoutManager(context)
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
                else -> {
                }
            }
        }
    }

    override val isLoadMoreEnable: Boolean
        get() = when (mLoadMoreEnableType) {
            TOTAL -> mAdapter.itemCount < mTotalSize
            PAGE -> mPageIndex < mTotalPage
            else -> super.isLoadMoreEnable
        }

    override fun initView(view: View) {
        super.initView(view)
        initRvView()
        onRequest()
        onObserve()
    }

    override fun onRequest() {
        super.onRequest()
        showProgressDialog()
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        mAdapter.setList(null)//clear data
        super.onRefresh(refreshLayout)
    }

    override fun onObserve() {
        viewModel?.liveData?.observe(viewLifecycleOwner, observer)
    }

    override fun initRvView() {
        mRecyclerView = requireView().findViewById(R.id.rv_common)
        mRecyclerView.let {
            it.layoutManager = mLayoutManager
            it.adapter = mAdapter
        }
        mAdapter.run {
            recyclerView = this@RootRvDialogFragment.mRecyclerView
            setEmptyView(emptyViewResId)//set empty or failure view
            emptyLayout?.setOnClickListener { view -> onClickLoadFailure(view) }
            isUseEmpty = false//don t show empty or failure view first
        }
    }

    override fun showLoadCompleteView(loadCompleteType: Int, data: List<T>) {
        dismissProgressDialog()
        mSmartRefreshLayout.run {
            finishRefresh()//refresh complete
            finishLoadMore()//load more complete
        }
        val refreshViewType = mRefreshViewType
        if (loadCompleteType == SUCCESS) {//load success
            if (data.isNotEmpty()) {//有数据
                mSmartRefreshLayout.run {
                    setEnableRefresh(refreshViewType == ONLY_REFRESH || refreshViewType == ALL)
                    setEnableLoadMore(refreshViewType == ONLY_LOADMORE || refreshViewType == ALL)
                }
                mAdapter.run {
                    isUseEmpty = false//don t show empty or failure view
                    if (mPageIndex == mInitPageIndex)
                        setList(data)
                    else
                        addData(data)
                }
            } else if (mPageIndex == mInitPageIndex) {//first page no data
                setLoadFailureResource(R.drawable.ic_kiku_nodata, R.string.kiku_load_nodata)
                mSmartRefreshLayout.run {
                    setEnableRefresh(false)//invalid refresh
                    setEnableLoadMore(false)//invalid load more
                }
                mAdapter.run {
                    isUseEmpty = true//show empty or failure view
                    setList(null)
                }
            } else if (mPageIndex > mInitPageIndex) {//next page no data
                if (mLoadMoreEnableType == EMPTY) {
                    mAdapter.isUseEmpty = false//don t show empty or failure view
                    mSmartRefreshLayout.finishLoadMoreWithNoMoreData()//all data load complete
                }
            }
            mAdapter.emptyLayout?.isEnabled = false//enable false
        } else if (loadCompleteType == ERROR) {//no net or load failure
            if (mPageIndex == mInitPageIndex) {//first page
                mSmartRefreshLayout.run {
                    setEnableRefresh(false)//invalid refresh
                    setEnableLoadMore(false)//invalid load more
                }
                mAdapter.run {
                    isUseEmpty = true//show empty or failure view
                    setList(null)
                }
                if (!requireContext().isConnected()) {//no net
                    setLoadFailureResource(R.drawable.ic_kiku_nonet, R.string.kiku_load_nonet)
                } else {//other reason(connect failure or server error)
                    setLoadFailureResource(R.drawable.ic_kiku_nonet, R.string.kiku_load_failure)
                }
                mAdapter.emptyLayout?.isEnabled = true//enable true
            } else {
                if (refreshViewType == ONLY_LOADMORE || refreshViewType == ALL) {
                    mPageIndex--//reset pageIndex when load more failure
                    mSmartRefreshLayout.finishLoadMore(false)//load more failure
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
        onRequest()
    }
}