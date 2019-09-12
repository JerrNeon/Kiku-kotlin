package com.jn.kikukt.fragment

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.jn.kikukt.R
import com.jn.kikukt.adapter.BaseRvAdapter
import com.jn.kikukt.annonation.*
import com.jn.kikukt.common.api.IRvView
import com.jn.kikukt.utils.isConnected
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
    override var mAdapter: BaseRvAdapter<T>? = null//adapter

    override fun getLayoutItemResourceId(): Int {
        return R.layout.common_rv
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
        mAdapter?.setNewData(null)//clear data
        super.onRefresh(refreshLayout)
    }

    override fun isLoadMoreEnable(): Boolean {
        if (getLoadMoreEnableType() == TOTAL)
            return mAdapter?.itemCount ?: 0 < getTotalSize()
        else if (getLoadMoreEnableType() == PAGE)
            return getPageSize() < getTotalPage()
        return super.isLoadMoreEnable()
    }

    override fun getLayoutManager(): RecyclerView.LayoutManager {
        return LinearLayoutManager(mContext)
    }

    override fun initRvView() {
        mRecyclerView = mView!!.rv_common
        mAdapter = getAdapter()
        mRecyclerView?.layoutManager = getLayoutManager()
        mRecyclerView?.adapter = mAdapter

        mAdapter?.bindToRecyclerView(mRecyclerView)
        mEmptyView = LayoutInflater.from(mContext)
            .inflate(R.layout.common_loadingfailure, mFlRootContainer, false)
        mIvLoadingFailure = mEmptyView?.findViewById(R.id.iv_commonLoadingFailure)
        mTvLoadingFailure = mEmptyView?.findViewById(R.id.tv_commonLoadingFailure)
        mEmptyView?.setOnClickListener { view -> onClickLoadFailure(view) }
        mAdapter?.emptyView = mEmptyView//set empty or failure view
        mAdapter?.isUseEmpty(false)//don t show empty or failure view first
        mAdapter?.onItemClickListener = this
        mAdapter?.onItemChildClickListener = this
        mAdapter?.onItemLongClickListener = this
        mAdapter?.onItemChildLongClickListener = this
    }

    override fun showLoadCompleteView(loadCompleteType: Int, data: List<T>) {
        dismissProgressDialog()
        mSmartRefreshLayout?.finishRefresh()//refresh complete
        mSmartRefreshLayout?.finishLoadMore()//load more complete
        val refreshViewType = getRefreshViewType()
        if (loadCompleteType == SUCCESS) {//load success
            if (data.isNotEmpty()) {//有数据
                mSmartRefreshLayout?.setEnableRefresh(refreshViewType == ONLY_REFRESH || refreshViewType == ALL)
                mSmartRefreshLayout?.setEnableLoadMore(refreshViewType == ONLY_LOADMORE || refreshViewType == ALL)
                mAdapter?.isUseEmpty(false)//don t show empty or failure view
                if (mPageIndex == getInitPageIndex())
                    mAdapter?.setNewData(data)
                else
                    mAdapter?.addData(data)
            } else if (mPageIndex == getInitPageIndex()) {//first page no data
                setLoadFailureResource(R.drawable.ic_kiku_nodata, R.string.kiku_load_nodata)
                mSmartRefreshLayout?.setEnableRefresh(false)//invalid refresh
                mSmartRefreshLayout?.setEnableLoadMore(false)//invalid load more
                mAdapter?.isUseEmpty(true)//show empty or failure view
                mAdapter?.setNewData(null)
            } else if (mPageIndex > getInitPageIndex()) {//next page no data
                if (getLoadMoreEnableType() == EMPTY) {
                    mAdapter?.isUseEmpty(false)//don t show empty or failure view
                    mSmartRefreshLayout?.finishLoadMoreWithNoMoreData()//all data load complete
                }
            }
            mEmptyView?.isEnabled = false//enable false
        } else if (loadCompleteType == ERROR) {//no net or load failure
            if (mPageIndex == getInitPageIndex()) {//first page
                mSmartRefreshLayout?.setEnableRefresh(false)//invalid refresh
                mSmartRefreshLayout?.setEnableLoadMore(false)//invalid load more
                mAdapter?.isUseEmpty(true)//show empty or failure view
                mAdapter?.setNewData(null)
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
        val loadMoreEnableType = getLoadMoreEnableType()
        if (loadMoreEnableType == TOTAL)
            mTotalSize = total
        else if (loadMoreEnableType == PAGE)
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
        setRefreshOperateType(ON_RELOAD)
        mPageIndex = getInitPageIndex()
        sendRequest()
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        onItemClick(adapter!!, view!!, mAdapter?.getItem(position)!!)
    }

    override fun onItemLongClick(
        adapter: BaseQuickAdapter<*, *>?,
        view: View?,
        position: Int
    ): Boolean {
        return onItemLongClick(adapter!!, view!!, mAdapter?.getItem(position)!!)
    }

    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        onItemChildClick(adapter!!, view!!, mAdapter?.getItem(position)!!)
    }

    override fun onItemChildLongClick(
        adapter: BaseQuickAdapter<*, *>?,
        view: View?,
        position: Int
    ): Boolean {
        return onItemChildLongClick(adapter!!, view!!, mAdapter?.getItem(position)!!)
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