package com.jn.kikukt.common.api

import com.jn.kikukt.annonation.PermissionType
import io.reactivex.functions.Consumer

/**
 * Author：Stevie.Chen Time：2019/7/11
 * Class Comment：基础
 */
interface IBaseView {

    /**
     * 初始化EventBus
     */
    fun initEventBus()

    /**
     * 取消注册EventBus
     */
    fun unregisterEventBus()

    /**
     * 初始化RxPermissions
     */
    fun initRxPermissions()

    /**
     * 请求权限
     * @param permissionType @PermissionType
     * @param consumer Consumer
     */
    fun requestPermission(@PermissionType permissionType: Int, consumer: Consumer<Boolean>?)

    /**
     * 设置状态栏
     */
    fun setStatusBar()

    /**
     * 显示加载框
     */
    fun showProgressDialog()

    /**
     * 取消显示加载框
     */
    fun dismissProgressDialog()

    /**
     * 初始化View
     */
    fun initView()

    /**
     * 初始化数据
     */
    fun initData()

    /**
     * 发起网络请求
     */
    fun sendRequest()
}