package com.jn.kikukt.mvp

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.jn.kikukt.common.api.IDisposableView
import com.jn.kikukt.common.utils.Clazz
import com.jn.kikukt.net.retrofit.observer.RxBaseObserver
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable

/**
 * Author：Stevie.Chen Time：2020/7/15
 * Class Comment：MVP
 */
interface IBModel
interface IBView
interface IBPresenter : IDisposableView, DefaultLifecycleObserver {

    fun attachView(view: IBView?)

    fun detachView()

    override fun onDestroy(owner: LifecycleOwner) {
        dispose()
        detachView()
    }
}

open class BaseModel : IBModel

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

abstract class RxBasePresenterObserver<T, V>(
    private val iBPresenter: IBPresenter,
    errorType: Int
) :
    RxBaseObserver<T, V>(errorType) {

    override fun onSubscribe(d: Disposable) {
        super.onSubscribe(d)
        iBPresenter.addDisposable(d)
    }
}