package com.jn.examplekotlin.request

import com.jn.examplekotlin.entiy.XaResult
import com.jn.kikukt.mvp.IBPresenter
import com.jn.kikukt.mvp.RxBasePresenterObserver
import com.jn.kikukt.net.retrofit.exception.OkHttpException

/**
 * Author：Stevie.Chen Time：2019/9/12
 * Class Comment：
 */
abstract class RxObserver<T> : RxBasePresenterObserver<XaResult<T>, T> {

    constructor(ibPresenter: IBPresenter) : this(ibPresenter, ALL)

    constructor(ibPresenter: IBPresenter, errorType: Int) : super(ibPresenter, errorType)

    override fun onNext(tXaResult: XaResult<T>) {
        super.onNext(tXaResult)
        if (tXaResult.code.toString() != ApiStatus.CODE_200) {
            val okHttpException =
                OkHttpException(tXaResult.code.toString(), tXaResult.message ?: "")
            val errorMsg = if (mErrorType == ALL) okHttpException.errorMsg else ""
            onFailure(okHttpException, errorMsg)
        } else {
            try {
                onResponse(tXaResult)
                onSuccess(tXaResult.result)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}