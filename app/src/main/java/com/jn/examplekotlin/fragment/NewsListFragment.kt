package com.jn.examplekotlin.fragment

import androidx.fragment.app.viewModels
import com.jn.examplekotlin.adapter.NewDataBindingAdapter
import com.jn.examplekotlin.entiy.NewsVO
import com.jn.examplekotlin.mvvm.NewsViewModel
import com.jn.kikukt.fragment.RootRvFragment

/**
 * Author：Stevie.Chen Time：2020/8/6
 * Class Comment：
 */
class NewsListFragment : RootRvFragment<NewsVO>() {

    override val viewModel by viewModels<NewsViewModel>()

    override val mAdapter by lazy {
        NewDataBindingAdapter()
    }

    override val isLazyLoad: Boolean
        get() = true

    override fun onRequest() {
        super.onRequest()
        viewModel.getNewsList(mPageIndex, mPageSize)
        onObserve()
    }
}