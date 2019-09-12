package com.jn.examplekotlin.mvp.model

import com.jn.examplekotlin.entiy.NewsVO
import com.jn.examplekotlin.mvp.contract.NewsContract
import com.jn.examplekotlin.request.ApiManager
import com.jn.examplekotlin.request.RxObserver
import com.jn.kikukt.mvp.BaseModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Author：Stevie.Chen Time：2019/9/12
 * Class Comment：
 */
class NewsModel : BaseModel(), NewsContract.IModel {

    override fun getNewList(pageIndex: Int, pageSize: Int, observer: RxObserver<List<NewsVO>>) {
        ApiManager.instance.getApiService()
            .getNewList(pageIndex, pageSize, "video")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(observer)
    }
}