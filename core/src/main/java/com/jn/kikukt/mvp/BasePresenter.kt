package com.jn.kikukt.mvp

import io.reactivex.disposables.CompositeDisposable

/**
 * Author：Stevie.Chen Time：2019/7/11
 * Class Comment：
 */
abstract class BasePresenter<V : IBView, M : IBModel> : IBPresenter {

    protected lateinit var mView: V
    protected lateinit var mModel: M
    override var mCompositeDisposable: CompositeDisposable? = null

    @Suppress("UNCHECKED_CAST")
    override fun attachView(view: IBView?) {
        mView = view as V
        mModel = getModel()
    }

    override fun detachView() {

    }

    abstract fun getModel(): M
}