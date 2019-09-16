package com.jn.kikukt.mvp

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.jn.kikukt.common.api.IDisposableView

/**
 * Author：Stevie.Chen Time：2019/7/11
 * Class Comment：
 */
interface IBPresenter : IDisposableView, DefaultLifecycleObserver {

    fun attachView(view: IBView?)

    fun detachView()

    override fun onDestroy(owner: LifecycleOwner) {
        dispose()
        detachView()
    }
}