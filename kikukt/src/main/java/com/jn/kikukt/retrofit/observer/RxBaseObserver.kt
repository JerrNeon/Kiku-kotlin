package com.jn.kikukt.retrofit.observer

import android.content.Context
import android.support.annotation.IntDef
import com.jn.kikukt.retrofit.exception.OkHttpErrorHelper
import com.jn.kikukt.utils.showToast
import io.reactivex.Observer
import io.reactivex.disposables.Disposable

/**
 * Author：Stevie.Chen Time：2019/7/15
 * Class Comment：
 */
abstract class RxBaseObserver<T, V> : Observer<T> {

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

    @ErrorType
    protected var mErrorType: Int = 0

    /**
     * @param errorType toast显示的错误类型
     */
    constructor(@ErrorType errorType: Int) {
        mErrorType = errorType
    }

    override fun onSubscribe(d: Disposable) {}

    override fun onNext(tXaResult: T) {

    }

    override fun onError(e: Throwable) {
        e.printStackTrace()
        var errorMsg = ""
        if (mErrorType != NONE)
            errorMsg = OkHttpErrorHelper.getMessage(e, getContext()) ?: ""
        onFailure(e, errorMsg)
    }

    override fun onComplete() {

    }

    /**
     * 请求成功
     *
     */
    abstract fun onSuccess(v: V)

    abstract fun getContext(): Context

    /**
     * 请求失败
     *
     * @param e        异常信息
     * @param errorMsg 错误提示信息
     */
    fun onFailure(e: Throwable, errorMsg: String?) {
        if (null != errorMsg && "" != errorMsg) {
            getContext().showToast(errorMsg)
        }
    }
}