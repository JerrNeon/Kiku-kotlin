package com.jn.examplekotlin.request.coroutines

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.jn.examplekotlin.entiy.HttpResult
import com.jn.examplekotlin.request.ApiStatus
import com.jn.kikukt.common.utils.Clazz
import com.jn.kikukt.common.utils.log
import com.jn.kikukt.common.utils.showToast
import com.jn.kikukt.mvvm.BaseViewModel
import com.jn.kikukt.net.coroutines.*
import com.jn.kikukt.net.retrofit.exception.OkHttpErrorHelper
import com.jn.kikukt.net.retrofit.exception.OkHttpException
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope

/**
 * Author：Stevie.Chen Time：2020/1/15
 * Class Comment：
 */
open class ApiViewModel<T>(application: Application) : BaseViewModel(application) {

    val repository: T by lazy { Clazz.getClass<T>(this).newInstance() }

    override fun defaultCatchBlock(e: Throwable, isToastAll: Boolean, error: ((Failure) -> Unit)?) {
        super.defaultCatchBlock(e, isToastAll, error)
        when (e) {
            is CancellationException -> {
                e.log()
            }
            else -> {
                if (!isToastAll && (e is OkHttpException)) {
                    return
                }
                val application = getApplication<Application>()
                val errorMsg = OkHttpErrorHelper.getMessage(e, application)
                if (errorMsg != null && errorMsg.isNotEmpty()) {
                    application.showToast(errorMsg)
                }
            }
        }
    }

    fun <R> HttpResult<R>.execute(
        success: ((Success<R>) -> Unit)?,
        error: ((Failure) -> Unit)? = null
    ) {
        val result: HttpResponse
        if (code.toString() != ApiStatus.CODE_200) {
            OkHttpException(code.toString(), message ?: "").let {
                result = Failure(it)
                error?.invoke(result)
                throw Throwable(it)
            }
        } else {
            result = Success(this.result)
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

        fun request(block: suspend CoroutineScope.() -> HttpResult<R>?) {
            startBlock?.invoke()
            launchOnMain(tryBlock = {
                block()?.execute(successBlock)
            }, catchBlock = { e ->
                defaultCatchBlock(e, error = {
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