package com.jn.kikukt.fragment

import android.content.Context
import com.jn.kikukt.mvp.IBPresenter
import com.jn.kikukt.mvp.IBView

/**
 * Author：Stevie.Chen Time：2019/7/11
 * Class Comment：
 */
abstract class RootRvPresenterFragment<P : IBPresenter<V, *>, V : IBView, T> : RootRvFragment<T>() {

    protected var mPresenter: P? = null

    protected abstract fun createPresenter(): P

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mPresenter = createPresenter()
        @Suppress("UNCHECKED_CAST")
        mPresenter?.attachView(this as? V)
    }

    override fun onDestroyView() {
        mPresenter?.detachView()
        mPresenter = null
        super.onDestroyView()
    }
}