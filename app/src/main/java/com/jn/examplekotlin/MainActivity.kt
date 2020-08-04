package com.jn.examplekotlin

import android.content.Intent
import android.os.Bundle
import com.jn.examplekotlin.activity.NewsListActivity
import com.jn.examplekotlin.adapter.NewAdapter
import com.jn.examplekotlin.entiy.NewsVO
import com.jn.examplekotlin.mvp.NewsContract
import com.jn.examplekotlin.mvp.NewsPresenter
import com.jn.kikukt.activity.RootRvActivity
import com.jn.kikukt.adapter.BaseRvAdapter
import com.jn.kikukt.adapter.listener
import com.jn.kikukt.mvp.presenters
import com.jn.kikukt.utils.glide.requestManager


class MainActivity : RootRvActivity<NewsVO>(), NewsContract.IView {

    override val presenter by presenters<NewsPresenter>()

    override val mAdapter: BaseRvAdapter<NewsVO> by lazy {
        NewAdapter(requestManager())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTitleText("新闻列表")

        mAdapter.listener {
            onItemClick { _, _, _, _ ->
                startActivity(Intent(this@MainActivity, NewsListActivity::class.java))
            }
        }
    }

    override fun onRequest() {
        super.onRequest()
        presenter.getNewList()
    }
}
