package com.jn.kikukt.common.api

import android.app.Activity

/**
 * Author：Stevie.Chen Time：2019/7/11
 * Class Comment：引导页
 */
interface ISplashView {

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
    fun openGuideActivity(activity: Activity)

    /**
     * 打开首页
     */
    fun openMainActivity(activity: Activity)
}