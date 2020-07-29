package com.jn.kikukt.common.api

import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable


/**
 * Author：Stevie.Chen Time：2019/7/11
 * Class Comment：
 */
interface IDisposableView {

    var mCompositeDisposable: CompositeDisposable?

    fun addDisposable(disposable: Disposable) {
        if (mCompositeDisposable == null)
            mCompositeDisposable = CompositeDisposable()
        mCompositeDisposable?.add(disposable)
    }

    fun dispose() {
        mCompositeDisposable?.let {
            if (!it.isDisposed)
                it.dispose()
        }
    }

}