package com.jn.kikukt.common.api

/**
 * Author：Stevie.Chen Time：2019/7/11
 * Class Comment：Fragment懒加载
 */
interface ILazyFragmentView {

    /**
     * 是否带懒加载的Fragment
     *
     * @return true：带懒加载 false：不带懒加载
     */
    fun isLazyLoadFragment(): Boolean

    /**
     * 页面可见
     */
    fun onFragmentVisible()

    /**
     * 页面不可见
     */
    fun onFragmentInvisible()

    /**
     * 懒加载
     */
    fun onFragmentLazyLoad()
}