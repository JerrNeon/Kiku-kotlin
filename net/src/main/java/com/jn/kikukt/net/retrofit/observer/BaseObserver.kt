package com.jn.kikukt.net.retrofit.observer

import android.app.Activity
import android.content.Context
import android.support.annotation.IntDef
import android.support.v7.app.AppCompatActivity
import com.jn.kikukt.common.utils.showToast
import com.jn.kikukt.net.retrofit.exception.OkHttpErrorHelper
import io.reactivex.Observer
import io.reactivex.disposables.Disposable

/**
 * Author：Stevie.Chen Time：2019/7/15
 * Class Comment：
 */
abstract class BaseObserver<T, V> : Observer<T> {

    companion object {
        /**
         * 不显示任何错误信息
         */
        const val NONE = 1
        /**
         * 仅显示异常类信息
         */
        const val EXCEPTION = 2
        /**
         * 显示所有错误信息
         */
        const val ALL = 3
    }

    @IntDef(NONE, EXCEPTION, ALL)
    @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
    internal annotation class ErrorType

    protected var mActivity: Activity? = null
    protected var mAppCompatActivity: AppCompatActivity? = null
    protected var mContext: Context? = null
    protected var mErrorType = ALL
    protected var isShowProgressDialog: Boolean = false

    /**
     * @param activity activity
     */
    constructor(activity: Activity) {
        mActivity = activity
        if (mActivity is AppCompatActivity)
            mAppCompatActivity = mActivity as AppCompatActivity?
        mContext = activity.applicationContext
    }

    /**
     * @param activity             activity
     * @param isShowProgressDialog 是否显示加载框，默认显示
     */
    constructor(activity: Activity, isShowProgressDialog: Boolean) : this(activity) {
        this.isShowProgressDialog = isShowProgressDialog
    }

    /**
     * @param activity  activity
     * @param errorType toast显示的错误类型
     */
    constructor(activity: Activity, @ErrorType errorType: Int) : this(activity) {
        mErrorType = errorType
    }

    /**
     * @param activity             activity
     * @param errorType            toast显示的错误类型
     * @param isShowProgressDialog 是否显示加载框，默认显示
     */
    constructor(activity: Activity, @ErrorType errorType: Int, isShowProgressDialog: Boolean) : this(
        activity,
        errorType
    ) {
        this.isShowProgressDialog = isShowProgressDialog
    }

    override fun onSubscribe(d: Disposable) {
        onBefore()
    }

    override fun onNext(tXaResult: T) {

    }

    override fun onError(e: Throwable) {
        e.printStackTrace()
        var errorMsg = ""
        if (mErrorType != NONE)
            errorMsg = OkHttpErrorHelper.getMessage(e, mContext!!) ?: ""
        onFailure(e, errorMsg)
    }

    override fun onComplete() {
        onAfter()
    }

    /**
     * 请求开始
     */
    fun onBefore() {
        showProgressDialog()
    }

    /**
     * 请求成功
     *
     * @param v
     */
    @Throws(Exception::class)
    abstract fun onSuccess(v: V)

    /**
     * 请求失败
     *
     * @param e        异常信息
     * @param errorMsg 错误提示信息
     */
    fun onFailure(e: Throwable, errorMsg: String?) {
        if (null != errorMsg && "" != errorMsg) {
            mActivity?.showToast(errorMsg)
        }
        dismissProgressDialog()
    }

    /**
     * 请求结束
     */
    fun onAfter() {
        dismissProgressDialog()
    }

    /**
     * 显示对话框
     */
    private fun showProgressDialog() {

    }

    /**
     * 隐藏对话框
     */
    private fun dismissProgressDialog() {

    }
}