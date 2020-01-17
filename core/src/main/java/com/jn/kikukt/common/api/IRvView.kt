package com.jn.kikukt.common.api

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.jn.kikukt.adapter.BaseRvAdapter
import com.jn.kikukt.annonation.LoadCompleteType
import com.jn.kikukt.net.coroutines.HttpResponse

/**
 * Author：Stevie.Chen Time：2019/7/10
 * Class Comment：
 */
interface IRvView<T> {

    var mRecyclerView: RecyclerView?//RecyclerView
    var mEmptyView: View?//empty or failure view
    var mIvLoadingFailure: ImageView?//empty or failure icon
    var mTvLoadingFailure: TextView?//empty or failure hint text
    val mAdapter: BaseRvAdapter<T>//adapter
    val mLayoutManager: RecyclerView.LayoutManager//LayoutManager
    val observer: Observer<HttpResponse>//Observer

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
    fun showLoadSuccessView(data: List<T>)

    /**
     * 显示成功布局
     *
     * @param total 总数 / 总页数 根据[IRefreshView.getLoadMoreEnableType]来判断
     * @param data  数据
     */
    fun showLoadSuccessView(total: Int, data: List<T>)

    /**
     * 显示失败布局
     */
    fun showLoadErrorView()

    /**
     * 设置加载失败或数据为空布局资源
     *
     * @param loadFailureDrawableRes 加载失败或数据为空显示图标资源
     * @param loadFailureStringRes   加载失败或数据为空显示提示文字资源
     */
    fun setLoadFailureResource(@DrawableRes loadFailureDrawableRes: Int, @StringRes loadFailureStringRes: Int)

    /**
     * 点击加载失败或数据为空布局
     *
     * @param view
     */
    fun onClickLoadFailure(view: View)

    /**
     * 子项点击事件
     *
     * @param adapter
     * @param view
     * @param item
     */
    fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, item: T)

    /**
     * 子项长按时间
     *
     * @param adapter
     * @param view
     * @param item
     * @return
     */
    fun onItemLongClick(adapter: BaseQuickAdapter<*, *>, view: View, item: T): Boolean

    /**
     * 子项控件点击事件
     *
     * @param adapter
     * @param view
     * @param item
     */
    fun onItemChildClick(adapter: BaseQuickAdapter<*, *>, view: View, item: T)

    /**
     * 子项控件长按时间
     *
     * @param adapter
     * @param view
     * @param item
     * @return
     */
    fun onItemChildLongClick(adapter: BaseQuickAdapter<*, *>, view: View, item: T): Boolean
}