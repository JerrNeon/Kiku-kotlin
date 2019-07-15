package com.jn.kikukt.widget

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import com.jn.kikukt.utils.ContextUtils
import com.jn.kikukt.utils.dp2px
import com.jn.kikukt.utils.getScreenWidth
import com.jn.kikukt.utils.sp2px
import java.util.*

/**
 * Author：Stevie.Chen Time：2019/7/15
 * Class Comment：图形验证码
 */
class GraphicCode {

    companion object {
        private val CHARS = charArrayOf(
            '0',
            '1',
            '2',
            '3',
            '4',
            '5',
            '6',
            '7',
            '8',
            '9',
            'a',
            'b',
            'c',
            'd',
            'e',
            'f',
            'g',
            'h',
            'i',
            'j',
            'k',
            'l',
            'm',
            'n',
            'o',
            'p',
            'q',
            'r',
            's',
            't',
            'u',
            'v',
            'w',
            'x',
            'y',
            'z',
            'A',
            'B',
            'C',
            'D',
            'E',
            'F',
            'G',
            'H',
            'I',
            'J',
            'K',
            'L',
            'M',
            'N',
            'O',
            'P',
            'Q',
            'R',
            'S',
            'T',
            'U',
            'V',
            'W',
            'X',
            'Y',
            'Z'
        )


        private const val DEFAULT_CODE_LENGTH = 4// 验证码的长度 这里是4位
        private const val DEFAULT_FONT_SIZE = 14// 字体大小
        private const val DEFAULT_LINE_NUMBER = 3// 多少条干扰线
        private const val BASE_PADDING_LEFT = 10 // 左边距
        private const val RANGE_PADDING_LEFT = 10// 左边距范围值
        private const val BASE_PADDING_TOP = 10// 上边距
        private const val RANGE_PADDING_TOP = 10// 上边距范围值
        private const val DEFAULT_WIDTH = 60// 默认宽度.图片的总宽
        private const val DEFAULT_HEIGHT = 22// 默认高度.图片的总高
        //private final int DEFAULT_COLOR = 0xdf;// 默认背景颜色值
        private const val DEFAULT_COLOR = 0xff// 默认背景颜色值

        private const val width = DEFAULT_WIDTH
        private const val height = DEFAULT_HEIGHT

        private const val base_padding_left = BASE_PADDING_LEFT
        private const val range_padding_left = RANGE_PADDING_LEFT
        private const val base_padding_top = BASE_PADDING_TOP
        private const val range_padding_top = RANGE_PADDING_TOP

        private const val codeLength = DEFAULT_CODE_LENGTH
        private const val line_number = DEFAULT_LINE_NUMBER
        private const val font_size = DEFAULT_FONT_SIZE
    }

    private var code: String? = null// 保存生成的验证码
    private var padding_left: Int = 0
    private var padding_top: Int = 0
    private val random = Random()

    private fun createBitmap(): Bitmap {
        val context = ContextUtils.getContext()
        padding_left = 0
        val bp = Bitmap.createBitmap(
            (context.getScreenWidth() * 0.21).toInt(),
            context.dp2px(height.toFloat()).toInt(),
            Bitmap.Config.ARGB_8888
        )
        val c = Canvas(bp)
        code = createCode()
        c.drawColor(Color.rgb(DEFAULT_COLOR, DEFAULT_COLOR, DEFAULT_COLOR))
        val paint = Paint()
        paint.textSize = context.sp2px(font_size.toFloat())
        for (i in 0 until code!!.length) {
            randomTextStyle(paint)
            randomPadding()
            c.drawText(
                code!![i] + "",
                context.dp2px(padding_left.toFloat()),
                context.dp2px(padding_top.toFloat()),
                paint
            )
        }
        for (i in 0 until line_number) {
            drawLine(c, paint)
        }
        //c.save(Canvas.ALL_SAVE_FLAG);// 保存
        c.save()// 保存
        c.restore()//
        return bp
    }

    fun getCode(): String {
        return code!!.toLowerCase()
    }

    fun getBitmap(): Bitmap {
        return createBitmap()
    }

    private fun createCode(): String {
        val buffer = StringBuilder()
        for (i in 0 until codeLength) {
            buffer.append(CHARS[random.nextInt(CHARS.size)])
        }
        return buffer.toString()
    }

    private fun drawLine(canvas: Canvas, paint: Paint) {
        val color = randomColor()
        val startX = random.nextInt(width)
        val startY = random.nextInt(height)
        val stopX = random.nextInt(width)
        val stopY = random.nextInt(height)
        paint.strokeWidth = 1f
        paint.color = color
        canvas.drawLine(startX.toFloat(), startY.toFloat(), stopX.toFloat(), stopY.toFloat(), paint)
    }

    private fun randomColor(): Int {
        return randomColor(1)
    }

    private fun randomColor(rate: Int): Int {
        val red = random.nextInt(256) / rate
        val green = random.nextInt(256) / rate
        val blue = random.nextInt(256) / rate
        return Color.rgb(red, green, blue)
    }

    private fun randomTextStyle(paint: Paint) {
        val color = randomColor()
        paint.color = color
        paint.isFakeBoldText = random.nextBoolean() // true为粗体，false为非粗体
        var skewX = (random.nextInt(11) / 10).toFloat()
        skewX = if (random.nextBoolean()) skewX else -skewX
        paint.textSkewX = skewX // float类型参数，负数表示右斜，整数左斜
    }

    private fun randomPadding() {
        padding_left += base_padding_left + random.nextInt(range_padding_left)
        padding_top = base_padding_top + random.nextInt(range_padding_top)
    }

}