package com.jn.kikukt.widget.ratingbar

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View.OnClickListener
import android.widget.ImageView
import android.widget.LinearLayout
import com.jn.kikukt.R
import java.math.BigDecimal
import kotlin.math.roundToInt

/**
 * Author：Stevie.Chen Time：2020/7/28 18:10
 * Class Comment：自定义RatingBar
 */
class RatingBar(context: Context, attrs: AttributeSet?) :
    LinearLayout(context, attrs) {
    /**
     * 是否可点击
     */
    private var mClickable: Boolean

    /**
     * 星星总数
     */
    private val starCount: Int

    /**
     * 星星的点击事件
     */
    private var onRatingChangeListener: OnRatingChangeListener? = null

    /**
     * 每个星星的大小
     */
    private var starImageSize: Float

    /**
     * 每个星星的间距
     */
    private val starPadding: Float

    /**
     * 星星的显示数量，支持小数点
     */
    private var starStep: Float

    /**
     * 空白的默认星星图片
     */
    private var starEmptyDrawable: Drawable?

    /**
     * 选中后的星星填充图片
     */
    private var starFillDrawable: Drawable?

    /**
     * 半颗星的图片
     */
    private var starHalfDrawable: Drawable?

    /**
     * 每次点击星星所增加的量是整个还是半个
     */
    private var stepSize: StepSize

    /**
     * 设置半星的图片资源文件
     */
    fun setStarHalfDrawable(starHalfDrawable: Drawable?) {
        this.starHalfDrawable = starHalfDrawable
    }

    /**
     * 设置满星的图片资源文件
     */
    fun setStarFillDrawable(starFillDrawable: Drawable?) {
        this.starFillDrawable = starFillDrawable
    }

    /**
     * 设置空白和默认的图片资源文件
     */
    fun setStarEmptyDrawable(starEmptyDrawable: Drawable?) {
        this.starEmptyDrawable = starEmptyDrawable
    }

    /**
     * 设置星星是否可以点击操作
     */
    override fun setClickable(clickable: Boolean) {
        mClickable = clickable
    }

    /**
     * 设置星星点击事件
     */
    fun setOnRatingChangeListener(onRatingChangeListener: OnRatingChangeListener?) {
        this.onRatingChangeListener = onRatingChangeListener
    }

    /**
     * 设置星星的大小
     */
    fun setStarImageSize(starImageSize: Float) {
        this.starImageSize = starImageSize
    }

    fun setStepSize(stepSize: StepSize) {
        this.stepSize = stepSize
    }

    /**
     * 设置每颗星星的参数
     */
    private val starImageView: ImageView
        get() {
            val imageView = ImageView(context)
            val layout = LayoutParams(
                starImageSize.roundToInt(), starImageSize.roundToInt()
            ) //设置每颗星星在线性布局的大小
            layout.setMargins(0, 0, starPadding.roundToInt(), 0) //设置每颗星星在线性布局的间距
            imageView.layoutParams = layout
            imageView.adjustViewBounds = true
            imageView.scaleType = ImageView.ScaleType.CENTER_CROP
            imageView.setImageDrawable(starEmptyDrawable)
            imageView.minimumWidth = 10
            imageView.maxHeight = 10
            return imageView
        }

    /**
     * 设置星星的个数
     */
    fun setStar(rating: Float) {
        onRatingChangeListener?.onRatingChange(rating)
        starStep = rating
        //浮点数的整数部分
        val fint = rating.toInt()
        val b1 = BigDecimal(rating.toString())
        val b2 = BigDecimal(fint.toString())
        //浮点数的小数部分
        val fPoint: Float = b1.subtract(b2).toFloat()

        //设置选中的星星
        for (i in 0 until fint) {
            (getChildAt(i) as ImageView).setImageDrawable(starFillDrawable)
        }
        //设置没有选中的星星
        for (i in fint until starCount) {
            (getChildAt(i) as ImageView).setImageDrawable(starEmptyDrawable)
        }
        //小数点默认增加半颗星
        if (fPoint > 0) {
            (getChildAt(fint) as ImageView).setImageDrawable(starHalfDrawable)
        }
    }

    /**
     * 操作星星的点击事件
     */
    interface OnRatingChangeListener {
        /**
         * 选中的星星的个数
         */
        fun onRatingChange(ratingCount: Float)
    }

    /**
     * 星星每次增加的方式整星还是半星，枚举类型
     * 类似于View.GONE
     */
    enum class StepSize(var step: Int) {
        Half(0), Full(1);

        companion object {
            fun fromStep(step: Int): StepSize {
                for (f in values()) {
                    if (f.step == step) {
                        return f
                    }
                }
                throw IllegalArgumentException()
            }
        }

    }

    /**
     * 构造函数
     * 获取xml中设置的资源文件
     */
    init {
        orientation = HORIZONTAL
        val mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.RatingBar)
        starImageSize = mTypedArray.getDimension(R.styleable.RatingBar_starImageSize, 20f)
        starPadding = mTypedArray.getDimension(R.styleable.RatingBar_starPadding, 10f)
        starStep = mTypedArray.getFloat(R.styleable.RatingBar_starStep, 1.0f)
        stepSize =
            StepSize.fromStep(mTypedArray.getInt(R.styleable.RatingBar_stepSize, 1))
        starCount = mTypedArray.getInteger(R.styleable.RatingBar_starCount, 5)
        starEmptyDrawable = mTypedArray.getDrawable(R.styleable.RatingBar_starEmpty)
        starFillDrawable = mTypedArray.getDrawable(R.styleable.RatingBar_starFill)
        starHalfDrawable = mTypedArray.getDrawable(R.styleable.RatingBar_starHalf)
        mClickable = mTypedArray.getBoolean(R.styleable.RatingBar_clickable, true)
        mTypedArray.recycle()
        for (i in 0 until starCount) {
            val imageView = starImageView
            imageView.setImageDrawable(starEmptyDrawable)
            imageView.setOnClickListener(
                OnClickListener { v ->
                    if (mClickable) {
                        //浮点数的整数部分
                        var fint = starStep.toInt()
                        val b1 =
                            BigDecimal(starStep.toString())
                        val b2 =
                            BigDecimal(fint.toString())
                        //浮点数的小数部分
                        val fPoint: Float = b1.subtract(b2).toFloat()
                        if (fPoint == 0f) {
                            fint -= 1
                        }
                        if (indexOfChild(v) > fint) {
                            setStar(indexOfChild(v) + 1.toFloat())
                        } else if (indexOfChild(v) == fint) {
                            if (stepSize == StepSize.Full) { //如果是满星 就不考虑半颗星了
                                return@OnClickListener
                            }
                            //点击之后默认每次先增加一颗星，再次点击变为半颗星
                            if (imageView.drawable.current
                                    .constantState == starHalfDrawable!!.constantState
                            ) {
                                setStar(indexOfChild(v) + 1.toFloat())
                            } else {
                                setStar(indexOfChild(v) + 0.5f)
                            }
                        } else {
                            setStar(indexOfChild(v) + 1f)
                        }
                    }
                }
            )
            addView(imageView)
        }
        setStar(starStep)
    }
}