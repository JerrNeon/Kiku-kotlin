package com.jn.examplekotlin.request.rxjava

import com.jn.examplekotlin.entiy.ApiResult
import com.jn.kikukt.mvp.IBPresenter
import com.jn.kikukt.mvp.RxBasePresenterObserver
import com.jn.kikukt.net.retrofit.exception.OkHttpException

/**
 * Author：Stevie.Chen Time：2019/9/12
 * Class Comment：
 */
abstract class RxObserver<T> : RxBasePresenterObserver<ApiResult<T>, T> {

    constructor(ibPresenter: IBPresenter) : this(ibPresenter, ALL)

    constructor(ibPresenter: IBPresenter, errorType: Int) : super(ibPresenter, errorType)

    override fun onNext(result: ApiResult<T>) {
        super.onNext(result)
        if (result.code.toString() != ApiStatus.CODE_200) {
            val okHttpException =
                OkHttpException(result.code.toString(), result.message ?: "")
            val errorMsg = if (mErrorType == ALL) okHttpException.errorMsg else ""
            onFailure(okHttpException, errorMsg)
        } else {
            try {
                onResponse(result)
                onSuccess(result.result)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}