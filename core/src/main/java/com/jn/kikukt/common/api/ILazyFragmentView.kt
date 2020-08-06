package com.jn.kikukt.common.api

/**
 * Author：Stevie.Chen Time：2020/8/6
 * Class Comment：AndroidX Fragment懒加载
 */
interface ILazyFragmentView {

    var isFirstLoad: Boolean//是否首次加载
    val isLazyLoad: Boolean
        get() = false//是否懒加载
}