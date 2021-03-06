package com.jn.kikukt.widget.recyclerview.layoutmanager

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

/**
 * Author：Stevie.Chen Time：2019/7/12
 * Class Comment：RecyclerView嵌套时用到
 */
class NoStaggeredGridLayoutManager : StaggeredGridLayoutManager {

    private var mwidth = 0
    private var mheight = 0

    private val mMeasuredDimension = IntArray(2)

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(
        context,
        attrs,
        defStyleAttr,
        defStyleRes
    )

    constructor(spanCount: Int, orientation: Int) : super(spanCount, orientation)

    fun getMwidth(): Int {
        return mwidth
    }

    fun setMwidth(mwidth: Int) {
        this.mwidth = mwidth
    }

    fun getMheight(): Int {
        return mheight
    }

    fun setMheight(mheight: Int) {
        this.mheight = mheight
    }

    override fun onMeasure(
        recycler: RecyclerView.Recycler,
        state: RecyclerView.State,
        widthSpec: Int,
        heightSpec: Int
    ) {
        val widthMode = View.MeasureSpec.getMode(widthSpec)
        val heightMode = View.MeasureSpec.getMode(heightSpec)
        val widthSize = View.MeasureSpec.getSize(widthSpec)
        val heightSize = View.MeasureSpec.getSize(heightSpec)

        var width = 0
        var height = 0
        val count = itemCount
        val span = spanCount
        for (i in 0 until count) {
            measureScrapChild(
                recycler, i,
                View.MeasureSpec.makeMeasureSpec(i, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(i, View.MeasureSpec.UNSPECIFIED),
                mMeasuredDimension
            )

            if (orientation == HORIZONTAL) {
                if (i % span == 0) {
                    width += mMeasuredDimension[0]
                }
                if (i == 0) {
                    height = mMeasuredDimension[1]
                }
            } else {
                if (i % span == 0) {
                    height += mMeasuredDimension[1]
                }
                if (i == 0) {
                    width = mMeasuredDimension[0]
                }
            }
        }

        if (widthMode == View.MeasureSpec.EXACTLY) {
            width = widthSize
        }

        if (heightMode == View.MeasureSpec.EXACTLY) {
            height = heightSize
        }
        setMheight(height)
        setMwidth(width)
        setMeasuredDimension(width, height)
    }

    private fun measureScrapChild(
        recycler: RecyclerView.Recycler?, position: Int, widthSpec: Int,
        heightSpec: Int, measuredDimension: IntArray
    ) {
        if (position < itemCount) {
            try {
                val view = recycler?.getViewForPosition(0)//fix 动态添加时报IndexOutOfBoundsException
                if (view != null) {
                    val p = view.layoutParams as RecyclerView.LayoutParams
                    val childWidthSpec = ViewGroup.getChildMeasureSpec(
                        widthSpec,
                        paddingLeft + paddingRight, p.width
                    )
                    val childHeightSpec = ViewGroup.getChildMeasureSpec(
                        heightSpec,
                        paddingTop + paddingBottom, p.height
                    )
                    view.measure(childWidthSpec, childHeightSpec)
                    measuredDimension[0] = view.measuredWidth + p.leftMargin + p.rightMargin
                    measuredDimension[1] = view.measuredHeight + p.bottomMargin + p.topMargin
                    recycler.recycleView(view)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }

    /**
     * RecyclerView在刷新数据的时候崩溃的bug
     *
     * @param recycler
     * @param state
     */
    override fun onLayoutChildren(recycler: RecyclerView.Recycler?, state: RecyclerView.State) {
        try {
            super.onLayoutChildren(recycler, state)
        } catch (e: IndexOutOfBoundsException) {
            e.printStackTrace()
        }

    }

    /**
     * Disable predictive animations. There is a bug in RecyclerView which causes views that
     * are being reloaded to pull invalid ViewHolders from the internal recycler stack if the
     * adapter size has decreased since the ViewHolder was recycled.
     */
    override fun supportsPredictiveItemAnimations(): Boolean {
        return false
    }

    override fun canScrollVertically(): Boolean {
        return false//解决滑动不流畅问题
    }
}