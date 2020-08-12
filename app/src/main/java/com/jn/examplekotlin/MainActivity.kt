package com.jn.examplekotlin

import android.os.Bundle
import com.jn.examplekotlin.activity.NewsListActivity
import com.jn.examplekotlin.adapter.NewAdapter
import com.jn.examplekotlin.entiy.NewsVO
import com.jn.examplekotlin.mvp.NewsContract
import com.jn.examplekotlin.mvp.NewsPresenter
import com.jn.kikukt.activity.RootRvActivity
import com.jn.kikukt.adapter.listener
import com.jn.kikukt.common.ext.startActivity
import com.jn.kikukt.mvp.presenters


class MainActivity : RootRvActivity<NewsVO>(), NewsContract.IView {

    override val presenter by presenters<NewsPresenter>()

    override val mAdapter by lazy {
        NewAdapter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTitleText("News RxJava")

        mAdapter.listener {
            onItemClick { _, _, _, _ ->
                startActivity<NewsListActivity>()
            }
        }
    }

    override fun onRequest() {
        super.onRequest()
        presenter.getNewList()
    }
}
