package com.jn.kikukt.net.coroutines

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.jn.kikukt.common.utils.log
import com.jn.kikukt.common.utils.showToast
import com.jn.kikukt.net.retrofit.*
import com.jn.kikukt.net.retrofit.exception.OkHttpErrorHelper
import com.jn.kikukt.net.retrofit.exception.OkHttpException
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope

/**
 * Author：Stevie.Chen Time：2020/1/15
 * Class Comment：
 */
open class HttpViewModel(application: Application) : CoroutineViewModel(application) {

    open val liveData by lazy { MutableLiveData<HttpResponse>() }

    open fun onFailure(
        e: Throwable,
        isToastAll: Boolean = true,
        error: ((Failure) -> Unit)? = null
    ) {
        val showErrorMsg = {
            val application = getApplication<Application>()
            val errorMsg = OkHttpErrorHelper.getMessage(e, application)
            if (errorMsg != null && errorMsg.isNotEmpty()) {
                application.showToast(errorMsg)
            }
        }
        when (e) {
            is CancellationException -> {
                e.log()
            }
            is OkHttpException -> {
                error?.invoke(Failure(e))
                if (isToastAll) {
                    showErrorMsg.invoke()
                }
            }
            else -> {
                showErrorMsg.invoke()
            }
        }
    }

    fun <R> BaseHttpResult<R>.execute(
        success: ((Success<R>) -> Unit)?,
        error: ((Failure) -> Unit)? = null
    ) {
        val result: HttpResponse
        if (rCode.toString() != HttpStatus.CODE_200) {
            OkHttpException(rCode.toString(), rMessage ?: "").let {
                result = Failure(it)
                error?.invoke(result)
                throw Throwable(it)
            }
        } else {
            result = Success(this.rData)
            success?.invoke(result)
        }
    }

    fun <R> launch(block: Execute<R>.() -> Unit) {
        launch(block, data = null, isLoading = false)
    }

    fun <R> launch(
        block: Execute<R>.() -> Unit,
        data: MutableLiveData<HttpResponse>? = liveData,
        isLoading: Boolean = true
    ) {
        if (isLoading) {
            Execute<R>().apply {
                onStart {
                    data?.value = Start()
                }
                block()
                onComplete {
                    data?.value = Complete()
                }
            }
        } else {
            Execute<R>().apply(block)
        }
    }

    inner class Execute<R> {
        private var startBlock: (() -> Unit)? = null
        private var successBlock: ((Success<R>?) -> Unit)? = null
        private var failureBlock: ((Failure) -> Unit)? = null
        private var exceptionBlock: ((Throwable?) -> Unit)? = null
        private var completeBlock: (() -> Unit)? = null

        fun onStart(block: () -> Unit) {
            startBlock = block
        }

        fun request(
            isToastAll: Boolean = true,
            block: suspend CoroutineScope.() -> BaseHttpResult<R>?
        ) {
            startBlock?.invoke()
            launchOnMain(tryBlock = {
                block()?.execute(successBlock)
            }, catchBlock = { e ->
                onFailure(e, isToastAll, error = {
                    failureBlock?.invoke(it)
                })
                exceptionBlock?.invoke(e)
            }, finallyBlock = {
                completeBlock?.invoke()
            })
        }

        fun onSuccess(block: (Success<R>?) -> Unit) {
            successBlock = block
        }

        fun onFailure(block: (Failure) -> Unit) {
            failureBlock = block
        }

        fun onException(block: (Throwable?) -> Unit) {
            exceptionBlock = block
        }

        fun onComplete(block: () -> Unit) {
            completeBlock = block
        }
    }
}