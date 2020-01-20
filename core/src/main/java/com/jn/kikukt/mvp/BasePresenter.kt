package com.jn.kikukt.mvp

import com.jn.kikukt.common.utils.Clazz
import io.reactivex.disposables.CompositeDisposable

/**
 * Author：Stevie.Chen Time：2019/7/11
 * Class Comment：
 */
abstract class BasePresenter<V : IBView, M : IBModel> : IBPresenter {

    protected lateinit var mView: V
    protected val mModel: M by lazy { Clazz.getClass<M>(this, argumentsPosition = 1).newInstance() }
    override var mCompositeDisposable: CompositeDisposable? = null

    @Suppress("UNCHECKED_CAST")
    override fun attachView(view: IBView?) {
        mView = view as V
    }

    override fun detachView() {

    }

}