package com.jn.kikukt.utils

import android.app.Activity
import android.content.Context
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.jn.kikukt.R
import com.jn.kikukt.common.api.IBaseView
import com.jn.kikukt.common.utils.statusbar.StatusBarUtils
import com.jn.kikukt.dialog.ProgressDialogFragment
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.functions.Consumer
import org.greenrobot.eventbus.EventBus

/**
 * Author：Stevie.Chen Time：2019/9/11
 * Class Comment：
 */
class BaseManager : IBaseView, DefaultLifecycleObserver {

    override var mActivity: Activity
    override lateinit var mAppCompatActivity: AppCompatActivity
    override var mContext: Context
    override var mRxPermissions: RxPermissions? = null
    override var mProgressDialog: ProgressDialogFragment? = null
    override var mBaseManager: BaseManager? = null
    private var mFragment: Fragment? = null

    constructor(@NonNull activity: Activity) {
        mActivity = activity
        if (activity is AppCompatActivity)
            mAppCompatActivity = activity
        mContext = activity.applicationContext
    }

    constructor(@NonNull fragment: Fragment) {
        mFragment = fragment
        mActivity = fragment.requireActivity()
        if (mActivity is AppCompatActivity)
            mAppCompatActivity = mActivity as AppCompatActivity
        mContext = mActivity.applicationContext
    }

    override fun initEventBus() {
        val subscriber = mFragment ?: mActivity
        EventBus.getDefault().register(subscriber)
    }

    override fun unregisterEventBus() {
        val subscriber = mFragment ?: mActivity
        EventBus.getDefault().unregister(subscriber)
    }

    override fun initRxPermissions() {
        if (mRxPermissions == null)
            mRxPermissions = RxPermissions(mActivity)
    }

    override fun requestPermission(permissionType: Int, consumer: Consumer<Boolean>?) {
        initRxPermissions()
        RxPermissionsManager.requestPermission(
            mActivity,
            mRxPermissions,
            permissionType,
            mContext.resources.getString(R.string.app_name),
            consumer
        )
    }

    fun requestAllPermissions(consumer: Consumer<Boolean>, permissions: Array<String>) {
        initRxPermissions()
        RxPermissionsManager.requestAllPermissions(
            mRxPermissions,
            consumer,
            permissions
        )
    }

    override fun setStatusBar() {
        StatusBarUtils.setColor(
            mActivity,
            ContextCompat.getColor(mContext, R.color.colorPrimary),
            0
        )
    }

    override fun onDestroy(owner: LifecycleOwner) {
        unregisterEventBus()
    }
}