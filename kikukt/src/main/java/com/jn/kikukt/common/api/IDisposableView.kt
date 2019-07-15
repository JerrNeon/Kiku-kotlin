package com.jn.kikukt.common.api

import io.reactivex.disposables.Disposable


/**
 * Author：Stevie.Chen Time：2019/7/11
 * Class Comment：
 */
interface IDisposableView {

    fun addDisposable(disposable: Disposable)

    fun dispose()
}