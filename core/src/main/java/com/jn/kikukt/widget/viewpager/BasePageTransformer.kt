package com.jn.kikukt.widget.viewpager

import android.annotation.TargetApi
import android.os.Build
import androidx.viewpager.widget.ViewPager
import android.view.View

/**
 * Author：Stevie.Chen Time：2019/7/12
 * Class Comment：
 */
abstract class BasePageTransformer : ViewPager.PageTransformer {

    internal var mPageTransformer: ViewPager.PageTransformer? = NonPageTransformer.INSTANCE

    companion object {
        internal const val DEFAULT_CENTER = 0.5f
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    override fun transformPage(view: View, position: Float) {
        if (mPageTransformer != null) {
            mPageTransformer!!.transformPage(view, position)
        }
        pageTransform(view, position)
    }

    protected abstract fun pageTransform(view: View, position: Float)
}