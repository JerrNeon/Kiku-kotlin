package com.jn.kikukt.activity

import android.os.Bundle
import com.jn.kikukt.common.api.IMvpView
import com.jn.kikukt.mvp.IBPresenter
import com.jn.kikukt.mvp.IBView

/**
 * Author：Stevie.Chen Time：2019/7/11
 * Class Comment：
 */
abstract class RootPresenterActivity<P : IBPresenter> : RootActivity(),
    IMvpView<P> {

    override var mPresenter: P? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initPresenter()
    }

    override fun initPresenter() {
        super.initPresenter()
        mPresenter?.let {
            it.attachView(this as? IBView)
            lifecycle.addObserver(it)
        }
    }

}