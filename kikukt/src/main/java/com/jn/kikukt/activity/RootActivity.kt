package com.jn.kikukt.activity

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.jn.kikukt.common.api.IBaseView
import com.jn.kikukt.common.api.IDisposableView
import com.jn.kikukt.dialog.ProgressDialogFragment
import com.jn.kikukt.utils.manager.BaseManager
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.disposables.CompositeDisposable

/**
 * Author：Stevie.Chen Time：2019/7/10
 * Class Comment：
 */
abstract class RootActivity : AppCompatActivity(), IBaseView,
    IDisposableView, View.OnClickListener {

    override lateinit var mActivity: Activity
    override lateinit var mAppCompatActivity: AppCompatActivity
    override lateinit var mContext: Context
    override var mRxPermissions: RxPermissions? = null
    override var mProgressDialog: ProgressDialogFragment? = null
    override var mCompositeDisposable: CompositeDisposable? = null
    override var mBaseManager: BaseManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mActivity = this
        mAppCompatActivity = this
        mContext = applicationContext
        mBaseManager = BaseManager(this)
        mBaseManager?.let {
            lifecycle.addObserver(it)
        }
    }

    override fun onDestroy() {
        dispose()
        super.onDestroy()
    }

    override fun onClick(v: View?) {
    }

}