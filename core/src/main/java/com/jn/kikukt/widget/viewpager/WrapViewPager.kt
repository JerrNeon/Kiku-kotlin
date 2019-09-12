package com.jn.kikukt.widget.viewpager

import android.content.Context
import android.support.v4.view.ViewPager
import android.util.AttributeSet

/**
 * Author：Stevie.Chen Time：2019/7/12
 * Class Comment：解决ViewPager设置WrapContent无效的问题
 */
class WrapViewPager : ViewPager {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var height = 0
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED))
            val h = child.measuredHeight
            if (h > height) height = h
        }
        val heightMeasureSpecs = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY)
        super.onMeasure(widthMeasureSpec, heightMeasureSpecs)
    }
}