package com.jn.kikukt.net.rxjava

import android.content.Context
import androidx.annotation.IntDef
import com.jn.kikukt.common.utils.ContextUtils
import com.jn.kikukt.common.utils.log
import com.jn.kikukt.common.utils.showToast
import com.jn.kikukt.net.retrofit.BaseHttpResult
import com.jn.kikukt.net.retrofit.HttpStatus
import com.jn.kikukt.net.retrofit.exception.OkHttpErrorHelper
import com.jn.kikukt.net.retrofit.exception.OkHttpException
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable

/**
 * Author：Stevie.Chen Time：2019/7/15
 * Class Comment：
 */
open class RxBaseObserver<T : BaseHttpResult<V>, V>(
    @ErrorType private val errorType: Int = ALL,
    private val onSubscribe: ((d: Disposable) -> Unit)? = null,
    private val onSuccess: (v: V) -> Unit,
    private val onResponse: ((t: T) -> Unit)? = null,
    private val onFailure: ((e: Throwable, errorMsg: String) -> Unit)? = null
) :
    Observer<T> {

    val context: Context
        get() = ContextUtils.context

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

    @IntDef(
        NONE,
        EXCEPTION,
        ALL
    )
    @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
    internal annotation class ErrorType

    override fun onSubscribe(d: Disposable) {
        onSubscribe?.invoke(d)
    }

    override fun onNext(result: T) {
        if (result.rCode.toString() != HttpStatus.CODE_200) {
            val okHttpException = OkHttpException(result.rCode.toString(), result.rMessage ?: "")
            val errorMsg = if (errorType == ALL) okHttpException.errorMsg ?: "" else ""
            onFailure(okHttpException, errorMsg)
        } else {
            onResponse(result)
            onSuccess(result.rData)
        }
    }

    override fun onError(e: Throwable) {
        e.log()
        val errorMsg =
            if (errorType != NONE) OkHttpErrorHelper.getMessage(e, context) ?: "" else ""
        onFailure(e, errorMsg)
    }

    override fun onComplete() {}

    /**
     * 成功
     * @param v 数据类型
     */
    open fun onSuccess(v: V) {
        onSuccess.invoke(v)
    }

    /**
     * 成功
     * @param t 实体类型
     */
    open fun onResponse(t: T) {
        onResponse?.invoke(t)
    }

    /**
     * 请求失败
     *
     * @param e        异常信息
     * @param errorMsg 错误提示信息
     */
    open fun onFailure(e: Throwable, errorMsg: String) {
        if (errorMsg.isNotEmpty()) context.showToast(errorMsg)
        onFailure?.invoke(e, errorMsg)
    }
}