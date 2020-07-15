package com.jn.examplekotlin

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.jn.examplekotlin.activity.NewsListActivity
import com.jn.examplekotlin.adapter.NewAdapter
import com.jn.examplekotlin.entiy.NewsVO
import com.jn.examplekotlin.mvp.NewsContract
import com.jn.examplekotlin.mvp.NewsPresenter
import com.jn.kikukt.activity.RootRvPresenterActivity
import com.jn.kikukt.adapter.BaseRvAdapter
import com.jn.kikukt.utils.glide.requestManager


class MainActivity : RootRvPresenterActivity<NewsContract.IPresenter, NewsVO>(),
    NewsContract.IView {

    override fun createPresenter(): NewsContract.IPresenter? = NewsPresenter()

    override val mAdapter: BaseRvAdapter<NewsVO> by lazy {
        NewAdapter(requestManager())
    }

    override fun sendRequest() {
        super.sendRequest()
        mPresenter?.getNewList()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTitleText("新闻列表")
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, item: NewsVO) {
        startActivity(Intent(this, NewsListActivity::class.java))
    }
}
