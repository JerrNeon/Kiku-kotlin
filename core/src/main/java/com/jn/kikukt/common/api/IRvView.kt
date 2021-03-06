package com.jn.kikukt.common.api

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jn.kikukt.R
import com.jn.kikukt.annonation.ERROR
import com.jn.kikukt.annonation.LoadCompleteType
import com.jn.kikukt.annonation.SUCCESS
import com.jn.kikukt.net.retrofit.HttpResponse

/**
 * Author：Stevie.Chen Time：2019/7/10
 * Class Comment：
 */
interface IRvView<T> {

    var mRecyclerView: RecyclerView//RecyclerView
    val emptyViewResId: Int//empty or failure view id
    val mAdapter: BaseQuickAdapter<T, out BaseViewHolder>//adapter
    val mLayoutManager: RecyclerView.LayoutManager//LayoutManager
    val observer: Observer<HttpResponse>//Observer

    /**
     * 对LiveData进行observe
     */
    fun onObserve()

    /**
     * 初始化刷新相关控件
     */
    fun initRvView()

    /**
     * 显示完成布局
     *
     * @param loadCompleteType 加载完成类型
     * @param data             数据
     */
    fun showLoadCompleteView(@LoadCompleteType loadCompleteType: Int, data: List<T>)

    /**
     * 显示成功布局
     *
     * @param data 数据
     */
    fun showLoadSuccessView(data: List<T>) {
        showLoadCompleteView(SUCCESS, data)
    }

    /**
     * 显示成功布局
     *
     * @param total 总数 / 总页数 根据[IRefreshView.mLoadMoreEnableType]来判断
     * @param data  数据
     */
    fun showLoadSuccessView(total: Int, data: List<T>)

    /**
     * 显示失败布局
     */
    fun showLoadErrorView() {
        showLoadCompleteView(ERROR, emptyList())
    }

    /**
     * 设置加载失败或数据为空布局资源
     *
     * @param loadFailureDrawableRes 加载失败或数据为空显示图标资源
     * @param loadFailureStringRes   加载失败或数据为空显示提示文字资源
     */
    fun setLoadFailureResource(
        @DrawableRes loadFailureDrawableRes: Int,
        @StringRes loadFailureStringRes: Int
    ) {
        mAdapter.emptyLayout?.run {
            val iv = findViewById<ImageView>(R.id.iv_commonLoadingFailure)
            val tv = findViewById<TextView>(R.id.tv_commonLoadingFailure)
            if (loadFailureDrawableRes != 0) {
                iv.setImageResource(loadFailureDrawableRes)
                iv.visibility = View.VISIBLE
            } else {
                iv.visibility = View.GONE
            }
            if (loadFailureStringRes != 0) {
                tv.setText(loadFailureStringRes)
                tv.visibility = View.VISIBLE
            } else {
                tv.visibility = View.GONE
            }
        }
    }

    /**
     * 点击加载失败或数据为空布局
     *
     * @param view
     */
    fun onClickLoadFailure(view: View)
}