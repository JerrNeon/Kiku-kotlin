package com.jn.kikukt.widget.index

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.jn.kikukt.common.utils.sp2px

/**
 * Author：Stevie.Chen Time：2019/7/12
 * Class Comment：快速索引
 */
class QuickIndexBar : View {

    //以26个字母作为索引
    private var indexs = arrayOf(
        "盛时",
        "A",
        "B",
        "C",
        "D",
        "E",
        "F",
        "G",
        "H",
        "I",
        "J",
        "K",
        "L",
        "M",
        "N",
        "O",
        "P",
        "Q",
        "R",
        "S",
        "T",
        "U",
        "V",
        "W",
        "X",
        "Y",
        "Z"
    )

    private var mContext: Context? = null
    private var paint: Paint? = null
    private val bounds = Rect()
    private var cellWidth: Int = 0
    private var cellHeight: Int = 0

    constructor(context: Context?) : super(context) {
        init(context)
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context)
    }

    fun init(context: Context?) {
        mContext = context
        paint = Paint()
        paint?.textSize = mContext?.sp2px(11f)!!
        paint?.color = Color.argb(255, 33, 33, 33)
        paint?.isAntiAlias = true
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (cellHeight == 0) cellHeight = measuredHeight / indexs.size
        if (cellWidth == 0) cellWidth = measuredWidth

        //分别画26个字母
        for (i in indexs.indices) {
            if (i == lastIndex) {               //正在touch的位置 需要更加人性化的交互
                paint?.textSize = mContext?.sp2px(16f)!!
                paint?.color = Color.argb(255, 33, 33, 33)
            } else {
                paint?.textSize = mContext?.sp2px(11f)!!
                paint?.color = Color.argb(255, 33, 33, 33)
            }

            //先测量用此画笔画字母的大小，用一个矩形把它包裹起来，这样方便计算字母的高度
            paint?.getTextBounds(indexs[i], 0, indexs[i].length, bounds)

            //计算画每个字母的起始坐标
            val x = cellWidth / 2 - (paint?.measureText(indexs[i])?.div(2) ?: 0f)
            val y = (cellHeight / 2 + bounds.height() / 2 + i * cellHeight).toFloat()

            canvas.drawText(indexs[i], x, y, paint!!)
        }
    }

    private var lastIndex = -1

    override fun onTouchEvent(event: MotionEvent): Boolean {
        //val x = event.x.toInt()
        //        if (x >= 0 && x <= cellWidth) {  //只对quickindexbar的触摸事件有效
        //            return false;
        //        }

        val y = event.y.toInt()
        when (event.action) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> {
                if (!isSameIndex(y / cellHeight)) {
                    //安全检查
                    if (y >= 0 && y / cellHeight < indexs.size) {
                        val word = indexs[y / cellHeight]
                        lastIndex = y / cellHeight
                        if (mIndexChangedListener != null) {
                            mIndexChangedListener!!.indexChanged(word)
                        }
                    }
                }
            }
            MotionEvent.ACTION_UP -> {
                lastIndex = -1
                performClick()
            }
            else -> {
            }
        }
        //重新调用onDraw
        invalidate()

        //自行处理触摸事件，不向上传递
        return true
    }

    override fun performClick(): Boolean {
        super.performClick()
        return true
    }

    /**
     * 当前的索引位置是否和上一个相等
     */
    private fun isSameIndex(currIndex: Int): Boolean {
        return lastIndex == currIndex
    }

    private var mIndexChangedListener: IndexChangedListener? = null

    fun setIndexChangedListener(indexChangedListener: IndexChangedListener) {
        mIndexChangedListener = indexChangedListener
    }

    interface IndexChangedListener {
        fun indexChanged(word: String)
    }
}