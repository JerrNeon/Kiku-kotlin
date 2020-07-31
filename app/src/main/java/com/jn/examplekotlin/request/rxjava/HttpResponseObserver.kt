package com.jn.examplekotlin.request.rxjava

import androidx.lifecycle.Observer
import com.jn.kikukt.common.api.IBaseView
import com.jn.kikukt.dialog.ProgressDialogFragment
import com.jn.kikukt.net.coroutines.Complete
import com.jn.kikukt.net.coroutines.HttpResponse
import com.jn.kikukt.net.coroutines.Start

/**
 * Author：Stevie.Chen Time：2020/1/16
 * Class Comment：需要显示Loading的[Observer]
 */
open class HttpResponseObserver(private val view: IBaseView? = null) : Observer<HttpResponse> {

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
                }
            }
        }
    }
}