package com.jn.examplekotlin.mvp.presenter

import com.jn.examplekotlin.entiy.NewsVO
import com.jn.examplekotlin.mvp.contract.NewsContract
import com.jn.examplekotlin.mvp.model.NewsModel
import com.jn.examplekotlin.request.RxObserver
import com.jn.kikukt.mvp.BasePresenter

/**
 * Author：Stevie.Chen Time：2019/9/12
 * Class Comment：
 */
class NewsPresenter : BasePresenter<NewsContract.IView, NewsContract.IModel>(),
    NewsContract.IPresenter {

    override fun getModel(): NewsContract.IModel = NewsModel()

    override fun getNewList() {
        mModel.getNewList(
            mView.getPageIndex(),
            mView.getPageSize(),
            object : RxObserver<List<NewsVO>>(this@NewsPresenter) {
                override fun onSuccess(v: List<NewsVO>) {
                    mView.showLoadSuccessView(v)
                }
            })
    }
}