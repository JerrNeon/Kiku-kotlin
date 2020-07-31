package com.jn.kikukt.mvvm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.jn.kikukt.net.coroutines.Failure
import com.jn.kikukt.net.coroutines.HttpResponse
import com.jn.kikukt.net.retrofit.exception.OkHttpException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

/**
 * Author：Stevie.Chen Time：2020/7/31
 * Class Comment：
 */

open class BaseRepository

open class BaseViewModel(application: Application) : AndroidViewModel(application) {

    val liveData by lazy { MutableLiveData<HttpResponse>() }

    open fun defaultCatchBlock(
        e: Throwable,
        isToastAll: Boolean = true,
        error: ((Failure) -> Unit)? = null
    ) {
        error?.invoke(Failure(if (e is OkHttpException) e else OkHttpException()))
    }

    private suspend fun tryCatch(
        tryBlock: suspend CoroutineScope.() -> Unit,
        catchBlock: suspend CoroutineScope.(e: Throwable) -> Unit,
        finallyBlock: suspend CoroutineScope.() -> Unit
    ) {
        coroutineScope {
            try {
                tryBlock()
            } catch (e: Throwable) {
                catchBlock(e)
            } finally {
                finallyBlock()
            }
        }
    }

    fun launchOnMain(
        tryBlock: suspend CoroutineScope.() -> Unit,
        catchBlock: suspend CoroutineScope.(e: Throwable) -> Unit = {},
        finallyBlock: suspend CoroutineScope.() -> Unit = {}
    ) {
        viewModelScope.launch {
            tryCatch(tryBlock, catchBlock, finallyBlock)
        }
    }

    fun launchOnIO(
        tryBlock: suspend CoroutineScope.() -> Unit,
        catchBlock: suspend CoroutineScope.(e: Throwable) -> Unit = {},
        finallyBlock: suspend CoroutineScope.() -> Unit = {}
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            tryCatch(tryBlock, catchBlock, finallyBlock)
        }
    }
}