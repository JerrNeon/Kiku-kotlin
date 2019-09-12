package com.jn.examplekotlin.mvp.contract

import com.jn.examplekotlin.entiy.NewsVO
import com.jn.examplekotlin.request.RxObserver
import com.jn.kikukt.common.api.IRefreshView
import com.jn.kikukt.common.api.IRvView
import com.jn.kikukt.mvp.IBModel
import com.jn.kikukt.mvp.IBPresenter
import com.jn.kikukt.mvp.IBView

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