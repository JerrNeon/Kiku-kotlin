package com.jn.kikukt.utils

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable

/**
 * Author：Stevie.Chen Time：2020/7/31
 * Class Comment：
 */
object DisposableManager {

    private var compositeDisposable: CompositeDisposable? = null

    fun addDisposable(disposable: Disposable, lifecycleOwner: LifecycleOwner) {
        if (compositeDisposable == null)
            compositeDisposable = CompositeDisposable()
        compositeDisposable?.add(disposable)
        lifecycleOwner.lifecycle.addObserver(object : LifecycleEventObserver {
            override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                if (Lifecycle.Event.ON_DESTROY == event) {
                    dispose()
                }
            }
        })
    }

    private fun dispose() {
        compositeDisposable?.let {
            if (!it.isDisposed)
                it.dispose()
        }
    }
}