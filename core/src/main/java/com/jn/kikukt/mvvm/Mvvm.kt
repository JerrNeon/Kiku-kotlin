package com.jn.kikukt.mvvm

import android.app.Application
import androidx.lifecycle.Observer
import com.jn.kikukt.common.api.IBaseView
import com.jn.kikukt.common.utils.Clazz
import com.jn.kikukt.dialog.ProgressDialogFragment
import com.jn.kikukt.net.coroutines.HttpViewModel
import com.jn.kikukt.net.retrofit.Complete
import com.jn.kikukt.net.retrofit.HttpResponse
import com.jn.kikukt.net.retrofit.Start

/**
 * Author：Stevie.Chen Time：2020/7/31
 * Class Comment：
 */

open class BaseRepository

open class BaseViewModel<T>(application: Application) : HttpViewModel(application) {
    val repository: T by lazy { Clazz.getClass<T>(this).newInstance() }
}

//需要显示Loading的[Observer]
open class LoadingObserver(
    private val view: IBaseView? = null,
    private val onChanged: ((response: HttpResponse) -> Unit)? = null
) : Observer<HttpResponse> {

    override fun onChanged(response: HttpResponse?) {
        response?.let {
            when (it) {
                is Start -> {
                    view?.showProgressDialog(ProgressDialogFragment.TYPE_WHITE)
                }
                is Complete -> {
                    view?.dismissProgressDialog()
                }
                else -> {
                    onChanged?.invoke(it)
                }
            }
        }
    }
}