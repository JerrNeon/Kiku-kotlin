package com.jn.kikukt.widget.viewpager

import android.annotation.TargetApi
import android.os.Build
import android.support.v4.view.ViewPager
import android.view.View

/**
 * Author：Stevie.Chen Time：2019/7/12
 * Class Comment：
 */
class ScaleInTransformer : BasePageTransformer {

    private var mMinScale = DEFAULT_MIN_SCALE

    companion object {
        private const val DEFAULT_MIN_SCALE = 0.85f
    }

    constructor()

    constructor(minScale: Float) : this(minScale, NonPageTransformer.INSTANCE)

    constructor(pageTransformer: ViewPager.PageTransformer) : this(DEFAULT_MIN_SCALE, pageTransformer)

    constructor(minScale: Float, pageTransformer: ViewPager.PageTransformer) {
        mMinScale = minScale
        mPageTransformer = pageTransformer
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public override fun pageTransform(view: View, position: Float) {
        val pageWidth = view.width
        val pageHeight = view.height

        view.pivotY = (pageHeight / 2).toFloat()
        view.pivotX = (pageWidth / 2).toFloat()
        if (position < -1) { // [-Infinity,-1)
            // This page is way off-screen to the left.
            view.scaleX = mMinScale
            view.scaleY = mMinScale
            view.pivotX = pageWidth.toFloat()
        } else if (position <= 1) { // [-1,1]
            // Modify the default slide transition to shrink the page as well
            if (position < 0)
            //1-2:1[0,-1] ;2-1:1[-1,0]
            {

                val scaleFactor = (1 + position) * (1 - mMinScale) + mMinScale
                view.scaleX = scaleFactor
                view.scaleY = scaleFactor

                view.pivotX =
                    pageWidth * (BasePageTransformer.DEFAULT_CENTER + BasePageTransformer.DEFAULT_CENTER * -position)

            } else
            //1-2:2[1,0] ;2-1:2[0,1]
            {
                val scaleFactor = (1 - position) * (1 - mMinScale) + mMinScale
                view.scaleX = scaleFactor
                view.scaleY = scaleFactor
                view.pivotX = pageWidth * ((1 - position) * BasePageTransformer.DEFAULT_CENTER)
            }


        } else { // (1,+Infinity]
            view.pivotX = 0f
            view.scaleX = mMinScale
            view.scaleY = mMinScale
        }
    }
}