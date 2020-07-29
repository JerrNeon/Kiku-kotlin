package com.jn.kikukt.common.api

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.jn.kikukt.dialog.ProgressDialogFragment

/**
 * Author：Stevie.Chen Time：2019/7/11
 * Class Comment：基础
 */
interface IBaseView {

    var mAppCompatActivity: AppCompatActivity
    var mContext: Context
    var mProgressDialog: ProgressDialogFragment?

    /**
     * 显示加载框
     */
    fun showProgressDialog(type: Int = ProgressDialogFragment.TYPE_BLACK) {
        if (mProgressDialog == null)
            mProgressDialog = ProgressDialogFragment.newInstance(type)
        mProgressDialog?.show(
            mAppCompatActivity.supportFragmentManager,
            ProgressDialogFragment::class.java.simpleName
        )
    }

    /**
     * 取消显示加载框
     */
    fun dismissProgressDialog() {
        mProgressDialog?.cancel()
        mProgressDialog = null
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
    fun onRequest() {}
}