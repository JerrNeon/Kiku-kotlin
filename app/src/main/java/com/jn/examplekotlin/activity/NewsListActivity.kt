package com.jn.examplekotlin.activity

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.jn.examplekotlin.adapter.NewAdapter
import com.jn.examplekotlin.entiy.NewsVO
import com.jn.examplekotlin.mvvm.NewsViewModel
import com.jn.kikukt.activity.RootRvActivity
import com.jn.kikukt.adapter.BaseRvAdapter
import com.jn.kikukt.common.utils.cast
import com.jn.kikukt.net.coroutines.Failure
import com.jn.kikukt.net.coroutines.Success

/**
 * Author：Stevie.Chen Time：2020/1/17
 * Class Comment：
 */
class NewsListActivity : RootRvActivity<NewsVO>() {

    private val viewModel: NewsViewModel by lazy {
        ViewModelProviders
            .of(this)
            .get(NewsViewModel::class.java)
    }

    override fun getAdapter(): BaseRvAdapter<NewsVO> = NewAdapter(this)

    override fun sendRequest() {
        super.sendRequest()
        viewModel.getNewsList(getPageIndex(), getPageSize())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTitleText("News ViewModel")
        viewModel.liveData.observe(this, Observer {
            when (it) {
                is Success<*> -> {
                    it.data?.cast<List<NewsVO>>()?.let { data ->
                        showLoadSuccessView(data)
                    }
                }
                is Failure -> {
                    showLoadErrorView()
                }
            }
        })
    }
}