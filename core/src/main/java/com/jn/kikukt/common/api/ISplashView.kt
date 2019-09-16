package com.jn.kikukt.common.api

import androidx.annotation.DrawableRes

/**
 * Author：Stevie.Chen Time：2019/7/11
 * Class Comment：引导页
 */
interface ISplashView {

    /**
     * 获取引导页图片资源
     *
     * @return
     */
    @DrawableRes
    fun getImgResourceId(): Int

    /**
     * 获取所有需要用到的权限
     *
     * @return
     */
    fun getAllPermissions(): Array<String>

    /**
     * 初始化App中需要用到的所有权限
     */
    fun requestAllPermission()

    /**
     * 打开引导页
     */
    fun openGuideActivity()

    /**
     * 打开首页
     */
    fun openMainActivity()
}