package com.jn.kikukt.mvp

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.jn.kikukt.common.utils.Clazz
import com.jn.kikukt.net.retrofit.BaseHttpResult
import com.jn.kikukt.net.rxjava.RxBaseObserver
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

    private var view: V? = null
    protected val mView: V
        get() = view!!
    protected val mModel: M by lazy { Clazz.getClass<M>(this, argumentsPosition = 1).newInstance() }
    override var mCompositeDisposable: CompositeDisposable? = null

    @Suppress("UNCHECKED_CAST")
    override fun attachView(view: IBView?) {
        this.view = view as? V
    }

    override fun detachView() {
        view = null
    }
}

open class RxBasePresenterObserver<T : BaseHttpResult<V>, V>(
    private val iBPresenter: IBPresenter,
    errorType: Int,
    onSubscribe: ((d: Disposable) -> Unit)? = null,
    onSuccess: (v: V) -> Unit,
    onResponse: ((t: T) -> Unit)? = null,
    onFailure: ((e: Throwable, errorMsg: String) -> Unit)? = null
) :
    RxBaseObserver<T, V>(errorType, onSubscribe, onSuccess, onResponse, onFailure) {

    override fun onSubscribe(d: Disposable) {
        super.onSubscribe(d)
        iBPresenter.addDisposable(d)
    }
}

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