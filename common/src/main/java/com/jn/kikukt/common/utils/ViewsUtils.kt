package com.jn.kikukt.common.utils

import android.content.Context
import android.support.design.widget.TabLayout
import android.support.v4.widget.NestedScrollView
import android.support.v7.widget.*
import android.view.View
import android.view.ViewTreeObserver
import android.widget.LinearLayout
import java.lang.reflect.Field
import kotlin.math.min

/**
 * Author：Stevie.Chen Time：2019/7/12
 * Class Comment：
 */
class ViewsUtils {

    companion object {
        // 两次点击按钮之间的点击间隔不能少于1000毫秒
        private const val MIN_CLICK_DELAY_TIME = 1000
        private var lastClickTime: Long = 0

        fun isFastClick(): Boolean {
            var flag = false
            val curClickTime = System.currentTimeMillis()
            if (curClickTime - lastClickTime >= MIN_CLICK_DELAY_TIME) {
                flag = true
            }
            lastClickTime = curClickTime
            return flag
        }

        fun resetClickTime() {
            lastClickTime = 0
        }
    }
}

/**
 * 判断RecyclerView最后一个child是否完全显示出来
 *
 * @return true完全显示出来，否则false
 */
fun RecyclerView.isLastItemVisible(): Boolean {
    val adapter = adapter ?: return true

    var firstVisiblePosition = 0
    var lastVisiblePosition = 0
    val layoutManager = layoutManager
    when (layoutManager) {
        is LinearLayoutManager -> {
            firstVisiblePosition = layoutManager.findFirstVisibleItemPosition()
            lastVisiblePosition = layoutManager.findLastVisibleItemPosition()
        }
        is GridLayoutManager -> {
            firstVisiblePosition = layoutManager.findFirstVisibleItemPosition()
            lastVisiblePosition = layoutManager.findLastVisibleItemPosition()
        }
        is StaggeredGridLayoutManager -> {
            val firstVisiblePositions =
                layoutManager.findFirstVisibleItemPositions(IntArray(layoutManager.spanCount))
            val lastVisiblePositions =
                layoutManager.findLastVisibleItemPositions(IntArray(layoutManager.spanCount))
            firstVisiblePosition = firstVisiblePositions[0]
            lastVisiblePosition = lastVisiblePositions[0]
            for (firstPosition in firstVisiblePositions) {
                if (firstPosition < firstVisiblePosition)
                    firstVisiblePosition = firstPosition
            }
            for (lastPosition in lastVisiblePositions) {
                if (lastPosition > lastVisiblePosition)
                    lastVisiblePosition = lastPosition
            }
        }

        /**
         * This check should really just be: lastVisiblePosition == lastItemPosition, but ListView
         * internally uses a FooterView which messes the positions up. For me we'll just subtract
         * one to account for it and rely on the inner condition which checks getBottom().
         */
    }
    val lastItemPosition = adapter.itemCount - 1

    /**
     * This check should really just be: lastVisiblePosition == lastItemPosition, but ListView
     * internally uses a FooterView which messes the positions up. For me we'll just subtract
     * one to account for it and rely on the inner condition which checks getBottom().
     */
    if (lastVisiblePosition >= lastItemPosition - 1) {
        val childIndex = lastVisiblePosition - firstVisiblePosition
        val childCount = adapter.itemCount
        val index = min(childIndex, childCount - 1)
        val lastVisibleChild = getChildAt(index)
        if (lastVisibleChild != null) {
            return lastVisibleChild.bottom <= bottom
        }
    }
    return false
}

/**
 * 判断RecyclerView最后一个child是否滑动到底部
 */
fun RecyclerView.isSlideToBottom(): Boolean {
    return computeVerticalScrollExtent() + computeVerticalScrollOffset() >= computeVerticalScrollRange()
}

/**
 * 判断RecyclerView最后一个child是否滑动到底部
 */
fun RecyclerView.isSlideBottom(): Boolean {
    return !canScrollVertically(1)
}

/**
 * 判断RecyclerView最后一个child是否滑动到顶部
 */
fun RecyclerView.isSlideTop(): Boolean {
    return !canScrollVertically(-1)
}

/**
 * 取消刷新时动画(解决RecyclerView局部刷新时闪烁)
 */
fun RecyclerView.cancelAnimations() {
    (itemAnimator as? DefaultItemAnimator)?.supportsChangeAnimations = false
}

/**
 * NestedScrollView是否滑动到底部
 */
fun NestedScrollView.isSlideToBottom(scrollY: Int): Boolean {
    return scrollY == getChildAt(0).measuredHeight - measuredHeight
}

/**
 * 获取View的宽高
 * <p>
 * onCreate() 中 View.getWidth 和 View.getHeight 无法获得一个 view 的高度和宽度，
 * 这是因为 View 组件布局要在 onResume() 回调后完成。
 * 所以现在需要使用 getViewTreeObserver().addOnGlobalLayoutListener() 来获得宽度或者高度。
 * 这是获得一个 view 的宽度和高度的方法之一。
 * 但是需要注意的是 OnGlobalLayoutListener 可能会被多次触发，因此在得到了高度之后，要将OnGlobalLayoutListener 注销掉。
 * </p>
 *
 */
fun View.addOnGlobalLayoutListener(onGlobalLayoutListener: ViewTreeObserver.OnGlobalLayoutListener?) {
    viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            onGlobalLayoutListener?.onGlobalLayout()
            viewTreeObserver.removeOnGlobalLayoutListener(this)
        }
    })
}

/**
 * 获取控件距离屏幕顶部的距离(不包含状态栏)
 */
fun View.getTopMargin(): Int {
    var topMargin = 0
    try {
        val location = IntArray(2)
        getLocationOnScreen(location)//获取在整个屏幕内的绝对坐标
        topMargin = location[1] - context.getStatusHeight()//Y坐标减去状态高度就是距离屏幕顶部的高度
    } catch (e: NullPointerException) {
        e.printStackTrace()
    } catch (e: Exception) {
        e.printStackTrace()
    }

    return topMargin
}

/**
 * 获取控件的宽高(设置为View.GONE状态的也可以获取到)
 */
fun View.getUnDisplayViewSize(): IntArray {
    val size = IntArray(2)
    try {
        val width = View.MeasureSpec.makeMeasureSpec(
            0,
            View.MeasureSpec.UNSPECIFIED
        )
        val height = View.MeasureSpec.makeMeasureSpec(
            0,
            View.MeasureSpec.UNSPECIFIED
        )
        measure(width, height)
        size[0] = measuredWidth
        size[1] = measuredHeight
        return size
    } catch (e: NullPointerException) {
        e.printStackTrace()
        size[0] = 0
        size[1] = 0
    } catch (e: Exception) {
        e.printStackTrace()
        size[0] = 0
        size[1] = 0
    }
    return size
}

/**
 * 设置TabLayout子项左右margin值
 * @param marginLeft  左margin
 * @param marginRight 右margin
 */
fun TabLayout.setTabLayoutIndicatorMargin(context: Context, marginLeft: Int, marginRight: Int) {
    val tabLayoutClass = javaClass
    var tabStrip: Field? = null
    try {
        tabStrip = tabLayoutClass.getDeclaredField("mTabStrip")
        tabStrip!!.isAccessible = true
    } catch (e: NoSuchFieldException) {
        e.printStackTrace()
    }

    var layout: LinearLayout? = null
    try {
        if (tabStrip != null) {
            layout = tabStrip.get(this) as LinearLayout
        }
        for (i in 0 until layout!!.childCount) {
            val child = layout.getChildAt(i)
            child.setPadding(0, 0, 0, 0)
            val params = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1f)
            params.marginStart = context.dp2px(marginLeft.toFloat()).toInt()
            params.marginEnd = context.dp2px(marginRight.toFloat()).toInt()
            child.layoutParams = params
            child.invalidate()
        }
    } catch (e: NullPointerException) {
        e.printStackTrace()
    } catch (e: IllegalAccessException) {
        e.printStackTrace()
    }
}