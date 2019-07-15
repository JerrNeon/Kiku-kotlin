package com.jn.kikukt.mvp

/**
 * Author：Stevie.Chen Time：2019/7/11
 * Class Comment：
 */
abstract class BasePresenter<V : IBView, M : IBModel> : IBPresenter<V, M> {

    override var mView: V? = null
    override var mModel: M? = null

    init {
        mModel = getModel()
    }

}