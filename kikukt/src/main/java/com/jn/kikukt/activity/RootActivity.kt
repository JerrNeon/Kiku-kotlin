package com.jn.kikukt.activity

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.jn.kikukt.R
import com.jn.kikukt.common.api.IBaseView
import com.jn.kikukt.common.api.IDisposableView
import com.jn.kikukt.dialog.ProgressDialogFragment
import com.jn.kikukt.utils.statusbar.StatusBarUtils
import com.jn.kikukt.utils.manager.RxPermissionsManager
import com.tbruyelle.rxpermissions2.RxPermissions
import com.trello.rxlifecycle2.android.ActivityEvent
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.subjects.BehaviorSubject
import org.greenrobot.eventbus.EventBus

/**
 * Author：Stevie.Chen Time：2019/7/10
 * Class Comment：
 */
open class RootActivity : AppCompatActivity(), IBaseView, IDisposableView, View.OnClickListener {

    protected lateinit var mActivity: Activity
    protected lateinit var mAppCompatActivity: AppCompatActivity
    protected lateinit var mContext: Context
    protected var mRxPermissions: RxPermissions? = null
    protected var mProgressDialog: ProgressDialogFragment? = null
    protected var mCompositeDisposable: CompositeDisposable? = null
    //Fragment声明周期管理,解决RxJava内存泄漏的问题
    protected val mLifecycleSubject = BehaviorSubject.create<ActivityEvent>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mLifecycleSubject.onNext(ActivityEvent.CREATE)
        mActivity = this
        mAppCompatActivity = this
        mContext = applicationContext
    }

    override fun onStart() {
        super.onStart()
        mLifecycleSubject.onNext(ActivityEvent.START)
    }

    override fun onResume() {
        super.onResume()
        mLifecycleSubject.onNext(ActivityEvent.RESUME)
    }

    override fun onPause() {
        mLifecycleSubject.onNext(ActivityEvent.PAUSE)
        super.onPause()
    }

    override fun onStop() {
        mLifecycleSubject.onNext(ActivityEvent.STOP)
        super.onStop()
    }

    override fun onDestroy() {
        mLifecycleSubject.onNext(ActivityEvent.DESTROY)
        unregisterEventBus()
        dispose()
        super.onDestroy()
    }

    override fun initEventBus() {
        EventBus.getDefault().register(this)
    }

    override fun unregisterEventBus() {
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this)
    }

    override fun initRxPermissions() {
        if (mRxPermissions == null)
            mRxPermissions = RxPermissions(this)
    }

    override fun requestPermission(permissionType: Int, consumer: Consumer<Boolean>?) {
        initRxPermissions()
        RxPermissionsManager.requestPermission(
            this,
            mRxPermissions,
            permissionType,
            resources.getString(R.string.app_name),
            consumer
        )
    }

    override fun setStatusBar() {
        StatusBarUtils.setColor(this, ContextCompat.getColor(applicationContext, R.color.colorPrimary), 0)
    }

    override fun showProgressDialog() {
        if (mProgressDialog == null)
            mProgressDialog = ProgressDialogFragment()
        mProgressDialog!!.show(mAppCompatActivity.supportFragmentManager, "")
    }

    override fun dismissProgressDialog() {
        mProgressDialog?.cancel()
    }

    override fun initView() {
    }

    override fun initData() {
    }

    override fun sendRequest() {
    }

    override fun addDisposable(disposable: Disposable) {
        if (mCompositeDisposable == null)
            mCompositeDisposable = CompositeDisposable()
        mCompositeDisposable?.add(disposable)
    }

    override fun dispose() {
        if (!mCompositeDisposable!!.isDisposed)
            mCompositeDisposable!!.dispose()
    }

    override fun onClick(v: View?) {
    }

}