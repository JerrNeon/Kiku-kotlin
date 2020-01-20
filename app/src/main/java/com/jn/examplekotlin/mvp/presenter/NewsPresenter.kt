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
class NewsPresenter : BasePresenter<NewsContract.IView, NewsModel>(),
    NewsContract.IPresenter {

    override fun getNewList() {
        mModel.getNewList(
            mView.mPageIndex,
            mView.mPageSize,
            object : RxObserver<List<NewsVO>>(this@NewsPresenter) {
                override fun onSuccess(v: List<NewsVO>) {
                    mView.showLoadSuccessView(v)
                }

                override fun onFailure(e: Throwable, errorMsg: String?) {
                    super.onFailure(e, errorMsg)
                    mView.showLoadErrorView()
                }
            })
    }
}