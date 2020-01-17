package com.jn.kikukt.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.jn.kikukt.R
import com.jn.kikukt.annonation.*
import com.jn.kikukt.common.api.IRvView
import com.jn.kikukt.common.utils.cast
import com.jn.kikukt.common.utils.isConnected
import com.jn.kikukt.net.coroutines.Failure
import com.jn.kikukt.net.coroutines.HttpResponse
import com.jn.kikukt.net.coroutines.Success
import com.scwang.smartrefresh.layout.api.RefreshLayout
import kotlinx.android.synthetic.main.common_rv.view.*

/**
 * Author：Stevie.Chen Time：2019/7/11
 * Class Comment：
 */
abstract class RootRvFragment<T> : RootRefreshFragment(), IRvView<T>,
    BaseQuickAdapter.OnItemClickListener,
    BaseQuickAdapter.OnItemLongClickListener, BaseQuickAdapter.OnItemChildClickListener,
    BaseQuickAdapter.OnItemChildLongClickListener {

    override var mRecyclerView: RecyclerView? = null//RecyclerView
    override var mEmptyView: View? = null//empty or failure view
    override var mIvLoadingFailure: ImageView? = null//empty or failure icon
    override var mTvLoadingFailure: TextView? = null//empty or failure hint text
    //RecyclerView.LayoutManager
    override val mLayoutManager: RecyclerView.LayoutManager by lazy { LinearLayoutManager(context) }
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
        get() = when (mLoadMoreEnableType) {
            TOTAL -> mAdapter.itemCount < mTotalSize
            PAGE -> mPageIndex < mTotalPage
            else -> super.isLoadMoreEnable
        }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initRvView()
        //非懒加载Fragment在onActivityCreated(方法中发起请求)
        if (!isLazyLoadFragment())
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
        mRecyclerView = mView!!.rv_common
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
            onItemClickListener = this@RootRvFragment
            onItemChildClickListener = this@RootRvFragment
            onItemLongClickListener = this@RootRvFragment
            onItemChildLongClickListener = this@RootRvFragment
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
                if (!mContext.isConnected()) {//no net
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

    override fun showLoadSuccessView(data: List<T>) {
        showLoadCompleteView(SUCCESS, data)
    }

    override fun showLoadSuccessView(total: Int, data: List<T>) {
        if (mLoadMoreEnableType == TOTAL)
            mTotalSize = total
        else if (mLoadMoreEnableType == PAGE)
            mTotalPage = total
        showLoadSuccessView(data)
    }

    override fun showLoadErrorView() {
        showLoadCompleteView(ERROR, emptyList())
    }

    override fun setLoadFailureResource(loadFailureDrawableRes: Int, loadFailureStringRes: Int) {
        mIvLoadingFailure?.setImageResource(loadFailureDrawableRes)
        mTvLoadingFailure?.setText(loadFailureStringRes)
    }

    override fun onClickLoadFailure(view: View) {
        mRefreshOperateType = ON_RELOAD
        mPageIndex = mInitPageIndex
        sendRequest()
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        onItemClick(adapter!!, view!!, mAdapter.getItem(position)!!)
    }

    override fun onItemLongClick(
        adapter: BaseQuickAdapter<*, *>?,
        view: View?,
        position: Int
    ): Boolean {
        return onItemLongClick(adapter!!, view!!, mAdapter.getItem(position)!!)
    }

    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        onItemChildClick(adapter!!, view!!, mAdapter.getItem(position)!!)
    }

    override fun onItemChildLongClick(
        adapter: BaseQuickAdapter<*, *>?,
        view: View?,
        position: Int
    ): Boolean {
        return onItemChildLongClick(adapter!!, view!!, mAdapter.getItem(position)!!)
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, item: T) {
    }

    override fun onItemLongClick(adapter: BaseQuickAdapter<*, *>, view: View, item: T): Boolean {
        return false
    }

    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>, view: View, item: T) {
    }

    override fun onItemChildLongClick(
        adapter: BaseQuickAdapter<*, *>,
        view: View,
        item: T
    ): Boolean {
        return false
    }
}