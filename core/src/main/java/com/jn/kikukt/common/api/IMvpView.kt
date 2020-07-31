package com.jn.kikukt.common.api

import com.jn.kikukt.mvp.IBPresenter

/**
 * Author：Stevie.Chen Time：2019/9/12
 * Class Comment：
 */
interface IMvpView {
    val presenter: IBPresenter?
}