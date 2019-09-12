package com.jn.kikukt.mvp

import io.reactivex.disposables.CompositeDisposable

/**
 * Author：Stevie.Chen Time：2019/7/11
 * Class Comment：
 */
abstract class BasePresenter<V : IBView, M : IBModel> : IBPresenter<V, M> {

    override var mView: V? = null
    override var mModel: M? = null
    override var mCompositeDisposable: CompositeDisposable? = null

    init {
        mModel = getModel()
    }

}