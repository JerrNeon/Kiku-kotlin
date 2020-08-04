package com.jn.examplekotlin.request.rxjava

import com.jn.examplekotlin.entiy.ApiResult
import com.jn.kikukt.mvp.IBPresenter
import com.jn.kikukt.mvp.RxBasePresenterObserver
import io.reactivex.rxjava3.disposables.Disposable

/**
 * Author：Stevie.Chen Time：2019/9/12
 * Class Comment：
 */
class RxObserver<T>(
    ibPresenter: IBPresenter, errorType: Int = ALL,
    onSubscribe: ((d: Disposable) -> Unit)? = null,
    onSuccess: (v: T) -> Unit,
    onResponse: ((t: ApiResult<T>) -> Unit)? = null,
    onFailure: ((e: Throwable, errorMsg: String) -> Unit)? = null
) :
    RxBasePresenterObserver<ApiResult<T>, T>(
        ibPresenter,
        errorType,
        onSubscribe,
        onSuccess,
        onResponse,
        onFailure
    )