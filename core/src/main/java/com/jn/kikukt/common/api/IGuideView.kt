package com.jn.kikukt.common.api

import androidx.annotation.DrawableRes

/**
 * Author：Stevie.Chen Time：2019/7/10
 * Class Comment：引导页
 */
interface IGuideView {

    /**
     * 获取引导页图片资源
     *
     * @return
     */
    @DrawableRes
    fun getImgResourceIds(): IntArray

    /**
     * 打开首页
     */
    fun openMainActivity()
}