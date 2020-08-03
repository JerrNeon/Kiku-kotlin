package com.jn.kikukt.net.coroutines

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

/**
 * Author：Stevie.Chen Time：2020/8/3
 * Class Comment：带协程的ViewModel
 */
open class CoroutineViewModel(application: Application) : AndroidViewModel(application) {

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