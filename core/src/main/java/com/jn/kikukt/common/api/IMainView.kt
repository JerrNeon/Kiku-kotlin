package com.jn.kikukt.common.api

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.IntRange
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction

/**
 * Author：Stevie.Chen Time：2019/7/11
 * Class Comment：主界面
 */
interface IMainView {

    /**
     * 获取主界面模块需要显示的所有菜单选中时的文字颜色资源
     *
     * @return
     */
    @ColorRes
    fun getMenuSelectedTextColorResources(): IntArray

    /**
     * 获取主界面模块需要显示的所有菜单选中时的图标资源
     *
     * @return
     */
    @DrawableRes
    fun getMenuSelectedImgResources(): IntArray

    /**
     * 获取主界面模块需要显示的所有菜单未选中时的图标资源
     *
     * @return
     */
    @DrawableRes
    fun getMenuUnSelectedImgResources(): IntArray

    /**
     * 获取主界面模块需要显示的所有菜单标题
     *
     * @return
     */
    fun getMenuTitles(): Array<String>

    /**
     * 获取主界面模块需要显示所有Fragment
     *
     * @return
     */
    fun getMenuFragments(): Array<Fragment>

    /**
     * 初始化主界面View
     */
    fun initMainView()

    /**
     * 切换菜单时需切换Fragment
     *
     * @param position
     */
    fun changeFragment(@IntRange(from = 0) position: Int)

    /**
     * 隐藏所有Fragment,切换Fragment时会用到
     *
     * @param fragmentTransaction FragmentTransaction
     */
    fun hideAllFragment(fragmentTransaction: FragmentTransaction)

    /**
     * 是否退出主界面
     *
     * @return
     */
    fun isExit(): Boolean

    /**
     * 注册版本更新广播
     */
    fun registerVersionUpdateReceiver()

    /**
     * 取消注册版本更新广播
     */
    fun unregisterVersionUpdateReceiver()

    /**
     * 显示版本更新对话框
     */
    fun showVersionUpdateDialog()
}