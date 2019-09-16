package com.jn.kikukt.widget.recyclerview.decoration

import android.content.Context
import android.graphics.Rect
import androidx.annotation.DimenRes
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import android.view.View

/**
 * Author：Stevie.Chen Time：2019/7/12
 * Class Comment：垂直滑动水平分割线
 */
class HorizontalDividerItemDecoration : FlexibleDividerDecoration {

    private var mMarginProvider: MarginProvider

    constructor(builder: Builder) : super(builder) {
        mMarginProvider = builder.mMarginProvider
    }

    override fun getDividerBound(position: Int, parent: RecyclerView, child: View): Rect {
        val bounds = Rect(0, 0, 0, 0)
        val transitionX = ViewCompat.getTranslationX(child).toInt()
        val transitionY = ViewCompat.getTranslationY(child).toInt()
        val params = child.layoutParams as RecyclerView.LayoutParams
        bounds.left = parent.paddingLeft +
                mMarginProvider.dividerLeftMargin(position, parent) + transitionX
        bounds.right = parent.width - parent.paddingRight -
                mMarginProvider.dividerRightMargin(position, parent) + transitionX

        val dividerSize = getDividerSize(position, parent)
        val isReverseLayout = isReverseLayout(parent)
        if (mDividerType == DividerType.DRAWABLE) {
            // set top and bottom position of divider
            if (isReverseLayout) {
                bounds.bottom = child.top - params.topMargin + transitionY
                bounds.top = bounds.bottom - dividerSize
            } else {
                bounds.top = child.bottom + params.bottomMargin + transitionY
                bounds.bottom = bounds.top + dividerSize
            }
        } else {
            // set center point of divider
            val halfSize = dividerSize / 2
            if (isReverseLayout) {
                bounds.top = child.top - params.topMargin - halfSize + transitionY
            } else {
                bounds.top = child.bottom + params.bottomMargin + halfSize + transitionY
            }
            bounds.bottom = bounds.top
        }

        if (mPositionInsideItem) {
            if (isReverseLayout) {
                bounds.top += dividerSize
                bounds.bottom += dividerSize
            } else {
                bounds.top -= dividerSize
                bounds.bottom -= dividerSize
            }
        }

        return bounds
    }

    override fun setItemOffsets(outRect: Rect, position: Int, parent: RecyclerView) {
        if (mPositionInsideItem) {
            outRect.set(0, 0, 0, 0)
            return
        }

        if (isReverseLayout(parent)) {
            outRect.set(0, getDividerSize(position, parent), 0, 0)
        } else {
            outRect.set(0, 0, 0, getDividerSize(position, parent))
        }
    }

    private fun getDividerSize(position: Int, parent: RecyclerView): Int {
        return when {
            mPaintProvider != null -> mPaintProvider!!.dividerPaint(position, parent).strokeWidth.toInt()
            mSizeProvider != null -> mSizeProvider!!.dividerSize(position, parent)
            mDrawableProvider != null -> {
                val drawable = mDrawableProvider!!.drawableProvider(position, parent)
                drawable.intrinsicHeight
            }
            else -> throw RuntimeException("failed to get size")
        }
    }

    /**
     * Interface for controlling divider margin
     */
    interface MarginProvider {

        /**
         * Returns left margin of divider.
         *
         * @param position Divider position (or group index for GridLayoutManager)
         * @param parent   RecyclerView
         * @return left margin
         */
        fun dividerLeftMargin(position: Int, parent: RecyclerView): Int

        /**
         * Returns right margin of divider.
         *
         * @param position Divider position (or group index for GridLayoutManager)
         * @param parent   RecyclerView
         * @return right margin
         */
        fun dividerRightMargin(position: Int, parent: RecyclerView): Int
    }

    class Builder(context: Context) : FlexibleDividerDecoration.Builder<Builder>(context) {

        var mMarginProvider: MarginProvider = object : MarginProvider {
            override fun dividerLeftMargin(position: Int, parent: RecyclerView): Int {
                return 0
            }

            override fun dividerRightMargin(position: Int, parent: RecyclerView): Int {
                return 0
            }
        }

        fun margin(leftMargin: Int, rightMargin: Int): Builder {
            return marginProvider(object : MarginProvider {
                override fun dividerLeftMargin(position: Int, parent: RecyclerView): Int {
                    return leftMargin
                }

                override fun dividerRightMargin(position: Int, parent: RecyclerView): Int {
                    return rightMargin
                }
            })
        }

        fun margin(horizontalMargin: Int): Builder {
            return margin(horizontalMargin, horizontalMargin)
        }

        fun marginResId(@DimenRes leftMarginId: Int, @DimenRes rightMarginId: Int): Builder {
            return margin(
                mResources?.getDimensionPixelSize(leftMarginId) ?: 0,
                mResources?.getDimensionPixelSize(rightMarginId) ?: 0
            )
        }

        fun marginResId(@DimenRes horizontalMarginId: Int): Builder {
            return marginResId(horizontalMarginId, horizontalMarginId)
        }

        fun marginProvider(provider: MarginProvider): Builder {
            mMarginProvider = provider
            return this
        }

        fun build(): HorizontalDividerItemDecoration {
            checkBuilderParams()
            return HorizontalDividerItemDecoration(this)
        }
    }
}