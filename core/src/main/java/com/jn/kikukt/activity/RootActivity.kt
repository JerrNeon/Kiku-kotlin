package com.jn.kikukt.activity

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.jn.kikukt.common.api.IBaseView
import com.jn.kikukt.common.api.IDisposableView
import com.jn.kikukt.common.api.IMvpView
import com.jn.kikukt.common.api.IViewModelView
import com.jn.kikukt.dialog.ProgressDialogFragment
import com.jn.kikukt.mvp.IBPresenter
import com.jn.kikukt.mvp.IBView
import com.jn.kikukt.net.coroutines.BaseViewModel
import io.reactivex.disposables.CompositeDisposable

/**
 * Author：Stevie.Chen Time：2019/7/10
 * Class Comment：
 */
abstract class RootActivity : AppCompatActivity(), IBaseView, IDisposableView, IViewModelView {

    override lateinit var mAppCompatActivity: AppCompatActivity
    override lateinit var mContext: Context
    override var mProgressDialog: ProgressDialogFragment? = null
    override var mCompositeDisposable: CompositeDisposable? = null
    override val viewModel: BaseViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext = applicationContext
        mAppCompatActivity = this
    }

    override fun onDestroy() {
        dispose()
        super.onDestroy()
    }
}

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