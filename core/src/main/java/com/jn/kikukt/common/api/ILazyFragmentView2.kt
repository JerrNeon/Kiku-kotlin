package com.jn.kikukt.common.api

/**
 * Author：Stevie.Chen Time：2019/7/11
 * Class Comment：Fragment懒加载
 */
interface ILazyFragmentView2 {

    /**
     * 标志位，标志已经初始化完成，因为setUserVisibleHint是在onCreateView之前调用的，
     * 在视图未初始化的时候，在lazyLoad当中就使用的话，就会有空指针的异常
     */
    var mIsFragmentViewCreated: Boolean
    var mIsFragmentVisible: Boolean//标志当前页面是否可见

    /**
     * 是否带懒加载的Fragment
     *
     * @return true：带懒加载 false：不带懒加载
     */
    fun isLazyLoad(): Boolean = false

    /**
     * 页面可见
     */
    fun onFragmentVisible() {
        onFragmentLazyLoad()
    }

    /**
     * 页面不可见
     */
    fun onFragmentInvisible() {

    }

    /**
     * 懒加载
     */
    fun onFragmentLazyLoad()

    /**
     * 是否只加载一次
     *
     * @return true：是 false：不是 默认：true
     */
    fun isLoadOnlyOnce(): Boolean {
        return true
    }
}