package com.jn.kikukt.common.api

import android.app.Activity
import android.content.Context
import android.support.v7.app.AppCompatActivity
import com.jn.kikukt.annonation.PermissionType
import com.jn.kikukt.dialog.ProgressDialogFragment
import com.jn.kikukt.utils.BaseManager
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.functions.Consumer

/**
 * Author：Stevie.Chen Time：2019/7/11
 * Class Comment：基础
 */
interface IBaseView {

    var mActivity: Activity
    var mAppCompatActivity: AppCompatActivity
    var mContext: Context
    var mRxPermissions: RxPermissions?
    var mProgressDialog: ProgressDialogFragment?
    var mBaseManager: BaseManager?

    /**
     * 初始化EventBus
     */
    fun initEventBus() {
        mBaseManager?.initEventBus()
    }

    /**
     * 取消注册EventBus
     */
    fun unregisterEventBus() {
        mBaseManager?.unregisterEventBus()
    }

    /**
     * 初始化RxPermissions
     */
    fun initRxPermissions() {
        mBaseManager?.initRxPermissions()
    }

    /**
     * 请求权限
     * @param permissionType @PermissionType
     * @param consumer Consumer
     */
    fun requestPermission(@PermissionType permissionType: Int, consumer: Consumer<Boolean>?) {
        mBaseManager?.requestPermission(permissionType, consumer)
    }

    /**
     * 设置状态栏
     */
    fun setStatusBar() {
        mBaseManager?.setStatusBar()
    }

    /**
     * 显示加载框
     */
    fun showProgressDialog() {
        if (mProgressDialog == null)
            mProgressDialog = ProgressDialogFragment()
        mProgressDialog!!.show(mAppCompatActivity.supportFragmentManager, "")
    }

    /**
     * 取消显示加载框
     */
    fun dismissProgressDialog() {
        mProgressDialog?.cancel()
    }

    /**
     * 初始化View
     */
    fun initView() {}

    /**
     * 初始化数据
     */
    fun initData() {}

    /**
     * 发起网络请求
     */
    fun sendRequest() {}
}