package com.jn.kikukt.widget.viewpager

import android.support.v4.view.ViewPager
import android.view.View

/**
 * Author：Stevie.Chen Time：2019/7/12
 * Class Comment：
 */
class NonPageTransformer : ViewPager.PageTransformer {

    companion object {
        internal val INSTANCE = NonPageTransformer()
    }

    override fun transformPage(page: View, position: Float) {
        page.scaleX = 0.999f
    }
}