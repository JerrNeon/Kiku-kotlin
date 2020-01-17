package com.jn.examplekotlin

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.jn.examplekotlin.activity.NewsListActivity
import com.jn.examplekotlin.adapter.NewAdapter
import com.jn.examplekotlin.entiy.NewsVO
import com.jn.examplekotlin.mvp.contract.NewsContract
import com.jn.examplekotlin.mvp.presenter.NewsPresenter
import com.jn.kikukt.activity.RootRvPresenterActivity
import com.jn.kikukt.adapter.BaseRvAdapter


class MainActivity : RootRvPresenterActivity<NewsContract.IPresenter, NewsVO>(),
    NewsContract.IView {

    override fun createPresenter(): NewsContract.IPresenter? = NewsPresenter()

    override fun getAdapter(): BaseRvAdapter<NewsVO> = NewAdapter(this)

    override fun sendRequest() {
        super.sendRequest()
        mPresenter?.getNewList()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTitleText("新闻列表")
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, item: NewsVO) {
        super.onItemClick(adapter, view, item)
        startActivity(Intent(this, NewsListActivity::class.java))
    }
}
