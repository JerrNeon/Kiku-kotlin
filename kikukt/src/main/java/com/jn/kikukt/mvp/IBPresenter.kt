package com.jn.kikukt.mvp

import android.arch.lifecycle.DefaultLifecycleObserver
import android.arch.lifecycle.LifecycleOwner
import com.jn.kikukt.common.api.IDisposableView

/**
 * Author：Stevie.Chen Time：2019/7/11
 * Class Comment：
 */
interface IBPresenter<V : IBView, M : IBModel> : IDisposableView, DefaultLifecycleObserver {

    var mView: V?
    var mModel: M?

    @Suppress("UNCHECKED_CAST")
    fun attachView(view: IBView?) {
        mView = view as? V
    }

    fun detachView() {
        mView = null
    }

    fun getModel(): M

    override fun onDestroy(owner: LifecycleOwner) {
        dispose()
        detachView()
    }
}