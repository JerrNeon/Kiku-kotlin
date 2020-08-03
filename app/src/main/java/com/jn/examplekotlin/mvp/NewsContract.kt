package com.jn.examplekotlin.mvp

import com.jn.examplekotlin.entiy.NewsVO
import com.jn.examplekotlin.request.rxjava.ApiManager
import com.jn.examplekotlin.request.rxjava.RxObserver
import com.jn.kikukt.common.api.IRefreshView
import com.jn.kikukt.common.api.IRvView
import com.jn.kikukt.mvp.*
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

/**
 * Author：Stevie.Chen Time：2019/9/12
 * Class Comment：
 */
interface NewsContract {

    interface IPresenter : IBPresenter {

        fun getNewList()
    }

    interface IView : IBView, IRefreshView, IRvView<NewsVO>

    interface IModel : IBModel {

        fun getNewList(pageIndex: Int, pageSize: Int, observer: RxObserver<List<NewsVO>>)
    }
}

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

class NewsModel : BaseModel(), NewsContract.IModel {

    override fun getNewList(pageIndex: Int, pageSize: Int, observer: RxObserver<List<NewsVO>>) {
        ApiManager.service
            .getNewList(pageIndex, pageSize, "video")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(observer)
    }
}