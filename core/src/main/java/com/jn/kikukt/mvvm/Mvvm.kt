package com.jn.kikukt.mvvm

import android.app.Application
import com.jn.kikukt.common.utils.Clazz
import com.jn.kikukt.net.coroutines.HttpViewModel

/**
 * Author：Stevie.Chen Time：2020/7/31
 * Class Comment：
 */

open class BaseRepository

open class BaseViewModel<T>(application: Application) : HttpViewModel(application) {
    val repository: T by lazy { Clazz.getClass<T>(this).newInstance() }
}