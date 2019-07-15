package com.jn.kikukt.activity

import android.os.Bundle
import com.jn.kikukt.mvp.IBPresenter
import com.jn.kikukt.mvp.IBView

/**
 * Author：Stevie.Chen Time：2019/7/11
 * Class Comment：
 */
abstract class RootTbPresenterActivity<P : IBPresenter<V, *>, V : IBView> : RootTbActivity() {

    protected var mPresenter: P? = null

    protected abstract fun createPresenter(): P

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mPresenter = createPresenter()
        @Suppress("UNCHECKED_CAST")
        mPresenter?.attachView(this as? V)
    }

    override fun onDestroy() {
        mPresenter?.detachView()
        mPresenter = null
        super.onDestroy()
    }
}