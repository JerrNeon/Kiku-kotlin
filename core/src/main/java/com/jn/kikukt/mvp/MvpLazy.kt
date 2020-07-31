package com.jn.kikukt.mvp

import androidx.activity.ComponentActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import kotlin.reflect.KClass

/**
 * Author：Stevie.Chen Time：2020/7/31
 * Class Comment：
 */
inline fun <reified MVP : IBPresenter> ComponentActivity.presenters(): Lazy<MVP> {
    return MvpLazy(MVP::class, this as? IBView, lifecycle)
}

inline fun <reified MVP : IBPresenter> Fragment.presenters(): Lazy<MVP> {
    return MvpLazy(MVP::class, this as? IBView, lifecycle)
}

class MvpLazy<MVP : IBPresenter>(
    private val mvpClass: KClass<MVP>,
    private val view: IBView?,
    private val lifecycle: Lifecycle
) : Lazy<MVP> {
    private var cached: MVP? = null

    override val value: MVP
        get() {
            val mvp = cached
            return mvp ?: mvpClass.java.newInstance().also {
                it.attachView(view)
                lifecycle.addObserver(it)
                cached = it
            }
        }

    override fun isInitialized(): Boolean = cached != null
}