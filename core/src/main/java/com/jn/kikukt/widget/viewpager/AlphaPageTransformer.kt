package com.jn.kikukt.widget.viewpager

import android.annotation.TargetApi
import android.os.Build
import androidx.viewpager.widget.ViewPager
import android.view.View

/**
 * Author：Stevie.Chen Time：2019/7/12
 * Class Comment：
 */
class AlphaPageTransformer : BasePageTransformer {

    private var mMinAlpha = DEFAULT_MIN_ALPHA

    companion object {
        private const val DEFAULT_MIN_ALPHA = 0.5f
    }

    constructor()

    constructor(minAlpha: Float) : this(minAlpha, NonPageTransformer.INSTANCE)

    constructor(pageTransformer: ViewPager.PageTransformer) : this(DEFAULT_MIN_ALPHA, pageTransformer)

    constructor(minAlpha: Float, pageTransformer: ViewPager.PageTransformer) {
        mMinAlpha = minAlpha
        mPageTransformer = pageTransformer
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public override fun pageTransform(view: View, position: Float) {
        view.scaleX = 0.999f//hack

        if (position < -1) { // [-Infinity,-1)
            view.alpha = mMinAlpha
        } else if (position <= 1) { // [-1,1]

            if (position < 0)
            //[0，-1]
            {           //[1,min]
                val factor = mMinAlpha + (1 - mMinAlpha) * (1 + position)
                view.alpha = factor
            } else
            //[1，0]
            {
                //[min,1]
                val factor = mMinAlpha + (1 - mMinAlpha) * (1 - position)
                view.alpha = factor
            }
        } else { // (1,+Infinity]
            view.alpha = mMinAlpha
        }
    }
}