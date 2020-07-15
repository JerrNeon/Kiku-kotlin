package com.jn.examplekotlin.activity

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.jn.examplekotlin.adapter.NewAdapter
import com.jn.examplekotlin.entiy.NewsVO
import com.jn.examplekotlin.mvvm.NewsViewModel
import com.jn.kikukt.activity.RootRvActivity
import com.jn.kikukt.adapter.BaseRvAdapter
import com.jn.kikukt.utils.glide.requestManager

/**
 * Author：Stevie.Chen Time：2020/1/17
 * Class Comment：
 */
class NewsListActivity : RootRvActivity<NewsVO>() {

    override val viewModel: NewsViewModel? by lazy {
        ViewModelProvider(this).get(NewsViewModel::class.java)
    }

    override val mAdapter: BaseRvAdapter<NewsVO> by lazy {
        NewAdapter(requestManager = requestManager())
    }

    override fun sendRequest() {
        super.sendRequest()
        viewModel?.getNewsList(mPageIndex, mPageSize)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTitleText("News ViewModel")
        viewModel?.liveData?.observe(this, observer)
    }
}