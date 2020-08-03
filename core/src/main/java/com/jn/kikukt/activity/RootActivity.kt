package com.jn.kikukt.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.jn.kikukt.common.api.IBaseView
import com.jn.kikukt.common.api.IMvpView
import com.jn.kikukt.common.api.IViewModelView
import com.jn.kikukt.dialog.ProgressDialogFragment
import com.jn.kikukt.mvp.IBPresenter
import com.jn.kikukt.net.coroutines.HttpViewModel

/**
 * Author：Stevie.Chen Time：2019/7/10
 * Class Comment：
 */
abstract class RootActivity : AppCompatActivity(), IBaseView, IViewModelView, IMvpView {

    override lateinit var mAppCompatActivity: AppCompatActivity
    override var mProgressDialog: ProgressDialogFragment? = null
    override val viewModel: HttpViewModel? = null
    override val presenter: IBPresenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAppCompatActivity = this
    }
}