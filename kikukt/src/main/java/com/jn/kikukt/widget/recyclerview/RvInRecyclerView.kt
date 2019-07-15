package com.jn.kikukt.widget.recyclerview

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.MotionEvent

/**
 * Author：Stevie.Chen Time：2019/7/12
 * Class Comment：RecyclerView嵌套-设置子项RecyclerView嵌套不拦截触摸事件
 */
class RvInRecyclerView : RecyclerView {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle)

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        return false
    }
}