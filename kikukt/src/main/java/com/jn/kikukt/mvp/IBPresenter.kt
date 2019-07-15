package com.jn.kikukt.mvp

import io.reactivex.subjects.BehaviorSubject

/**
 * Author：Stevie.Chen Time：2019/7/11
 * Class Comment：
 */
interface IBPresenter<V : IBView, M : IBModel> {

    var mView: V?
    var mModel: M?

    fun attachView(view: V?) {
        mView = view
    }

    fun detachView() {
        mView = null
    }

    fun getModel(): M

    fun setLifecycleBehaviorSubject(subject: BehaviorSubject<*>)
}