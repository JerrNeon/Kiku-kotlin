package com.jn.kikukt.widget.viewpager

import android.annotation.TargetApi
import android.os.Build
import android.support.v4.view.ViewPager
import android.view.View

/**
 * Author：Stevie.Chen Time：2019/7/12
 * Class Comment：
 */
class RotateYTransformer : BasePageTransformer {

    private var mMaxRotate = DEFAULT_MAX_ROTATE

    companion object {
        private const val DEFAULT_MAX_ROTATE = 35f
    }

    constructor()

    constructor(maxRotate: Float) : this(maxRotate, NonPageTransformer.INSTANCE)

    constructor(pageTransformer: ViewPager.PageTransformer) : this(DEFAULT_MAX_ROTATE, pageTransformer)

    constructor(maxRotate: Float, pageTransformer: ViewPager.PageTransformer) {
        mMaxRotate = maxRotate
        mPageTransformer = pageTransformer
    }


    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public override fun pageTransform(view: View, position: Float) {
        view.pivotY = (view.height / 2).toFloat()

        if (position < -1) { // [-Infinity,-1)
            // This page is way off-screen to the left.
            view.rotationY = -1 * mMaxRotate
            view.pivotX = view.width.toFloat()
        } else if (position <= 1) { // [-1,1]
            // Modify the default slide transition to shrink the page as well

            view.rotationY = position * mMaxRotate

            if (position < 0)
            //[0,-1]
            {
                view.pivotX =
                    view.width * (BasePageTransformer.DEFAULT_CENTER + BasePageTransformer.DEFAULT_CENTER * -position)
                view.pivotX = view.width.toFloat()
            } else
            //[1,0]
            {
                view.pivotX = view.width.toFloat() * BasePageTransformer.DEFAULT_CENTER * (1 - position)
                view.pivotX = 0f
            }

            // Scale the page down (between MIN_SCALE and 1)
        } else { // (1,+Infinity]
            // This page is way off-screen to the right.
            view.rotationY = 1 * mMaxRotate
            view.pivotX = 0f
        }
    }
}