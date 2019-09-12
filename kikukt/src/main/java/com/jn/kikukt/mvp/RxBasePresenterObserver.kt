package com.jn.kikukt.mvp

import com.jn.kikukt.retrofit.observer.RxBaseObserver
import io.reactivex.disposables.Disposable

/**
 * Author：Stevie.Chen Time：2019/9/12
 * Class Comment：
 */
abstract class RxBasePresenterObserver<T, V>(
    private val iBPresenter: IBPresenter<*, *>,
    errorType: Int
) :
    RxBaseObserver<T, V>(errorType) {

    override fun onSubscribe(d: Disposable) {
        super.onSubscribe(d)
        iBPresenter.addDisposable(d)
    }
}