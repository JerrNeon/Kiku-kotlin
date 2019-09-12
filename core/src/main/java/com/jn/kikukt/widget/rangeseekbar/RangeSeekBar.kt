package com.jn.kikukt.widget.rangeseekbar

import android.content.Context
import android.content.res.TypedArray
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Parcelable
import android.support.annotation.ColorRes
import android.support.v7.widget.AppCompatImageView
import android.util.AttributeSet
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import com.jn.kikukt.R
import com.jn.kikukt.common.utils.NumberUtils
import com.jn.kikukt.common.utils.dp2px
import com.jn.kikukt.common.utils.sp2px
import java.math.BigDecimal
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt
import kotlin.math.roundToLong

/**
 * Author：Stevie.Chen Time：2019/7/12
 * Class Comment：
 */
class RangeSeekBar<T : Number> : AppCompatImageView {

    /**
     * Default color of a [RangeSeekBar], #FF33B5E5. This is also known as "Ice Cream Sandwich" blue.
     */
    val ACTIVE_COLOR = Color.argb(0xFF, 0x33, 0xB5, 0xE5)
    /**
     * An invalid pointer id.
     */
    val INVALID_POINTER_ID = 255

    // Localized constants from MotionEvent for compatibility
    // with API < 8 "Froyo".
    val ACTION_POINTER_INDEX_MASK = 0x0000ff00
    val ACTION_POINTER_INDEX_SHIFT = 8

    val DEFAULT_MINIMUM = 0
    val DEFAULT_MAXIMUM = 100
    val DEFAULT_STEP = 1
    val HEIGHT_IN_DP = 30f
    val TEXT_LATERAL_PADDING_IN_DP = 3f

    private val INITIAL_PADDING_IN_DP = 8f
    private val DEFAULT_TEXT_SIZE_IN_DP = 12f//顶部的字体大小
    private val DEFAULT_TEXT_DISTANCE_TO_BUTTON_IN_DP = 4f//离底部的距离
    private val DEFAULT_TEXT_DISTANCE_TO_TOP_IN_DP = 4f//离顶部的距离

    private val LINE_HEIGHT_IN_DP = 1f
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val shadowPaint = Paint()

    private var thumbImage: Bitmap? = null
    private var thumbPressedImage: Bitmap? = null
    private var thumbDisabledImage: Bitmap? = null

    private var thumbHalfWidth: Float = 0f
    private var thumbHalfHeight: Float = 0f

    private var padding: Float = 0f
    private var absoluteMinValue: T? = null
    private var absoluteMaxValue: T? = null
    private var absoluteStepValue: T? = null
    private var numberType: NumberType? = null
    private var absoluteMinValuePrim: Double = 0.toDouble()
    private var absoluteMaxValuePrim: Double = 0.toDouble()
    private var absoluteStepValuePrim: Double = 0.toDouble()
    private var normalizedMinValue = 0.0f
    private var normalizedMaxValue = 1.0f
    private var minDeltaForDefault = 0.0
    private var pressedThumb: Thumb? = null
    private var notifyWhileDragging = false
    private var listener: OnRangeSeekBarChangeListener<T>? = null
    private var mRateListener: OnRangeRateSeekBarChangeListener? = null

    private var downMotionX: Float = 0.toFloat()

    private var activePointerId = INVALID_POINTER_ID

    private var scaledTouchSlop: Int = 0

    private var isDragging: Boolean = false

    private var textOffset: Int = 0
    private var textSize: Int = 0
    private var distanceToTop: Int = 0
    private var rect: RectF? = null

    private var singleThumb: Boolean = false
    private var alwaysActive: Boolean = false
    private var showLabels: Boolean = false
    private var showTextAboveThumbs: Boolean = false
    private var internalPad: Float = 0f
    private var activeColor: Int = 0
    private var defaultColor: Int = 0
    private var textAboveThumbsColor: Int = 0

    private var thumbShadow: Boolean = false
    private var thumbShadowXOffset: Float? = 0f
    private var thumbShadowYOffset: Float? = 0f
    private var thumbShadowBlur: Float? = 0f
    private var thumbShadowPath: Path? = null
    private val translatedThumbShadowPath = Path()
    private val thumbShadowMatrix = Matrix()

    private var activateOnDefaultValues: Boolean = false

    companion object {
        internal fun drawableToBitmap(drawable: Drawable): Bitmap {
            if (drawable is BitmapDrawable) {
                return drawable.bitmap
            }

            // We ask for the bounds if they have been set as they would be most
            // correct, then we check we are  > 0
            val width = if (!drawable.bounds.isEmpty)
                drawable.bounds.width()
            else
                drawable.intrinsicWidth

            val height = if (!drawable.bounds.isEmpty)
                drawable.bounds.height()
            else
                drawable.intrinsicHeight

            // Now we check we are > 0
            val bitmap = Bitmap.createBitmap(
                if (width <= 0) 1 else width, if (height <= 0) 1 else height,
                Bitmap.Config.ARGB_8888
            )
            val canvas = Canvas(bitmap)
            drawable.setBounds(0, 0, canvas.width, canvas.height)
            drawable.draw(canvas)

            return bitmap
        }
    }

    constructor(context: Context?) : super(context) {
        init(context, null)
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context, attrs)
    }

    private fun extractNumericValueFromAttributes(a: TypedArray, attribute: Int, defaultValue: Int): T {
        val tv = a.peekValue(attribute) ?: return Integer.valueOf(defaultValue) as T

        val type = tv.type
        return if (type == TypedValue.TYPE_FLOAT) {
            java.lang.Float.valueOf(a.getFloat(attribute, defaultValue.toFloat())) as T
        } else {
            Integer.valueOf(a.getInteger(attribute, defaultValue)) as T
        }
    }

    private fun init(context: Context?, attrs: AttributeSet?) {
        val barHeight: Float?
        val thumbNormal = R.drawable.ic_kiku_seekbar
        val thumbPressed = R.drawable.ic_kiku_seekbar
        val thumbDisabled = R.drawable.ic_kiku_seekbar
        val thumbShadowColor: Int
        val defaultShadowColor = Color.argb(75, 0, 0, 0)
        val defaultShadowYOffset = context?.dp2px(2f)
        val defaultShadowXOffset = context?.dp2px(0f)
        val defaultShadowBlur = context?.dp2px(2f)

        if (attrs == null) {
            setRangeToDefaultValues()
            internalPad = context?.dp2px(INITIAL_PADDING_IN_DP) ?: 0f
            barHeight = context?.dp2px(LINE_HEIGHT_IN_DP)
            activeColor = ACTIVE_COLOR
            defaultColor = Color.GRAY
            alwaysActive = false
            showTextAboveThumbs = true
            textAboveThumbsColor = Color.WHITE
            thumbShadowColor = defaultShadowColor
            thumbShadowXOffset = defaultShadowXOffset
            thumbShadowYOffset = defaultShadowYOffset
            thumbShadowBlur = defaultShadowBlur
            activateOnDefaultValues = false
        } else {
            val a = getContext().obtainStyledAttributes(attrs, R.styleable.RangeSeekBar, 0, 0)
            try {
                setRangeValues(
                    extractNumericValueFromAttributes(a, R.styleable.RangeSeekBar_absoluteMinValue, DEFAULT_MINIMUM),
                    extractNumericValueFromAttributes(a, R.styleable.RangeSeekBar_absoluteMaxValue, DEFAULT_MAXIMUM),
                    extractNumericValueFromAttributes(a, R.styleable.RangeSeekBar_step, DEFAULT_STEP)
                )
                showTextAboveThumbs = a.getBoolean(R.styleable.RangeSeekBar_valuesAboveThumbs, true)
                textAboveThumbsColor = a.getColor(R.styleable.RangeSeekBar_textAboveThumbsColor, Color.WHITE)
                singleThumb = a.getBoolean(R.styleable.RangeSeekBar_singleThumb, false)
                showLabels = a.getBoolean(R.styleable.RangeSeekBar_showLabels, true)
                internalPad =
                    a.getDimensionPixelSize(R.styleable.RangeSeekBar_internalPadding, INITIAL_PADDING_IN_DP.toInt())
                        .toFloat()
                barHeight =
                    a.getDimensionPixelSize(R.styleable.RangeSeekBar_barHeight, LINE_HEIGHT_IN_DP.toInt()).toFloat()
                activeColor = a.getColor(R.styleable.RangeSeekBar_activeColor, ACTIVE_COLOR)
                defaultColor = a.getColor(R.styleable.RangeSeekBar_defaultColor, Color.GRAY)
                alwaysActive = a.getBoolean(R.styleable.RangeSeekBar_alwaysActive, false)

                val normalDrawable = a.getDrawable(R.styleable.RangeSeekBar_thumbNormal)
                if (normalDrawable != null) {
                    thumbImage = drawableToBitmap(normalDrawable)
                }
                val disabledDrawable = a.getDrawable(R.styleable.RangeSeekBar_thumbDisabled)
                if (disabledDrawable != null) {
                    thumbDisabledImage = drawableToBitmap(disabledDrawable)
                }
                val pressedDrawable = a.getDrawable(R.styleable.RangeSeekBar_thumbPressed)
                if (pressedDrawable != null) {
                    thumbPressedImage = drawableToBitmap(pressedDrawable)
                }
                thumbShadow = a.getBoolean(R.styleable.RangeSeekBar_thumbShadow, false)
                thumbShadowColor = a.getColor(R.styleable.RangeSeekBar_thumbShadowColor, defaultShadowColor)
                thumbShadowXOffset =
                    a.getDimensionPixelSize(
                        R.styleable.RangeSeekBar_thumbShadowXOffset,
                        defaultShadowXOffset?.toInt() ?: 0
                    ).toFloat()
                thumbShadowYOffset =
                    a.getDimensionPixelSize(
                        R.styleable.RangeSeekBar_thumbShadowYOffset,
                        defaultShadowYOffset?.toInt() ?: 0
                    ).toFloat()
                thumbShadowBlur =
                    a.getDimensionPixelSize(R.styleable.RangeSeekBar_thumbShadowBlur, defaultShadowBlur?.toInt() ?: 0)
                        .toFloat()

                activateOnDefaultValues = a.getBoolean(R.styleable.RangeSeekBar_activateOnDefaultValues, false)
            } finally {
                a.recycle()
            }
        }

        if (thumbImage == null) {
            thumbImage = BitmapFactory.decodeResource(resources, thumbNormal)
        }
        if (thumbPressedImage == null) {
            thumbPressedImage = BitmapFactory.decodeResource(resources, thumbPressed)
        }
        if (thumbDisabledImage == null) {
            thumbDisabledImage = BitmapFactory.decodeResource(resources, thumbDisabled)
        }

        thumbHalfWidth = 0.5f * thumbImage!!.width
        thumbHalfHeight = 0.5f * thumbImage!!.height

        setValuePrimAndNumberType()

        textSize = context?.sp2px(DEFAULT_TEXT_SIZE_IN_DP)?.toInt() ?: 0
        distanceToTop = context?.dp2px(DEFAULT_TEXT_DISTANCE_TO_TOP_IN_DP)?.toInt() ?: 0
        textOffset = if (!showTextAboveThumbs)
            0
        else
            this.textSize
        +
        (context?.dp2px(DEFAULT_TEXT_DISTANCE_TO_BUTTON_IN_DP)?.toInt() ?: 0)
        +
        this.distanceToTop

        rect = RectF(
            padding,
            textOffset + thumbHalfHeight - (barHeight?.div(2) ?: 0f),
            width - padding,
            textOffset.toFloat() + thumbHalfHeight + (barHeight?.div(2) ?: 0f)
        )

        // make RangeSeekBar focusable. This solves focus handling issues in case EditText widgets are being used along with the RangeSeekBar within ScrollViews.
        isFocusable = true
        isFocusableInTouchMode = true
        scaledTouchSlop = ViewConfiguration.get(getContext()).scaledTouchSlop

        if (thumbShadow) {
            // We need to remove hardware acceleration in order to blur the shadow
            setLayerType(View.LAYER_TYPE_SOFTWARE, null)
            shadowPaint.color = thumbShadowColor
            shadowPaint.maskFilter = BlurMaskFilter(thumbShadowBlur ?: 0f, BlurMaskFilter.Blur.NORMAL)
            thumbShadowPath = Path()
            thumbShadowPath!!.addCircle(
                0f,
                0f,
                thumbHalfHeight,
                Path.Direction.CW
            )
        }
    }

    fun setRangeValues(minValue: T, maxValue: T) {
        this.absoluteMinValue = minValue
        this.absoluteMaxValue = maxValue
        setValuePrimAndNumberType()
    }

    fun setRangeValues(minValue: T, maxValue: T, step: T) {
        this.absoluteStepValue = step
        setRangeValues(minValue, maxValue)
    }

    fun setTextAboveThumbsColor(textAboveThumbsColor: Int) {
        this.textAboveThumbsColor = textAboveThumbsColor
        invalidate()
    }

    fun setTextAboveThumbsColorResource(@ColorRes resId: Int) {
        setTextAboveThumbsColor(resources.getColor(resId))
    }

    private// only used to set default values when initialised from XML without any values specified
    fun setRangeToDefaultValues() {
        this.absoluteMinValue = DEFAULT_MINIMUM as T
        this.absoluteMaxValue = DEFAULT_MAXIMUM as T
        this.absoluteStepValue = DEFAULT_STEP as T
        setValuePrimAndNumberType()
    }

    private fun setValuePrimAndNumberType() {
        absoluteMinValuePrim = absoluteMinValue?.toDouble() ?: 0.toDouble()
        absoluteMaxValuePrim = absoluteMaxValue?.toDouble() ?: 0.toDouble()
        absoluteStepValuePrim = absoluteStepValue?.toDouble() ?: 0.toDouble()
        numberType = NumberType.fromNumber(absoluteMinValue ?: 0)
    }

    fun resetSelectedValues() {
        setSelectedMinValue(absoluteMinValue!!)
        setSelectedMaxValue(absoluteMaxValue!!)
    }

    fun isNotifyWhileDragging(): Boolean {
        return notifyWhileDragging
    }

    /**
     * Should the widget notify the listener callback while the user is still dragging a thumb? Default is false.
     */
    fun setNotifyWhileDragging(flag: Boolean) {
        this.notifyWhileDragging = flag
    }

    /**
     * Returns the absolute minimum value of the range that has been set at construction time.
     *
     * @return The absolute minimum value of the range.
     */
    fun getAbsoluteMinValue(): T? {
        return absoluteMinValue
    }

    /**
     * Returns the absolute maximum value of the range that has been set at construction time.
     *
     * @return The absolute maximum value of the range.
     */
    fun getAbsoluteMaxValue(): T? {
        return absoluteMaxValue
    }

    /**
     * Round off value using the [.absoluteStepValue]
     *
     * @param value to be rounded off
     * @return rounded off value
     */
    private fun roundOffValueToStep(value: T): T? {
        val d = (value.toDouble() / absoluteStepValuePrim).roundToLong() * absoluteStepValuePrim
        return numberType?.toNumber(max(absoluteMinValuePrim, min(absoluteMaxValuePrim, d))) as T
    }

    /**
     * Returns the currently selected min value.
     *
     * @return The currently selected min value.
     */
    fun getSelectedMinValue(): T? {
        return roundOffValueToStep(normalizedToValue(normalizedMinValue.toDouble()))
    }

    fun isDragging(): Boolean {
        return isDragging
    }

    /**
     * Sets the currently selected minimum value. The widget will be invalidated and redrawn.
     *
     * @param value The Number value to set the minimum value to. Will be clamped to given absolute minimum/maximum range.
     */
    fun setSelectedMinValue(value: T) {
        // in case absoluteMinValue == absoluteMaxValue, avoid division by zero when normalizing.
        if (0.0 == (absoluteMaxValuePrim - absoluteMinValuePrim)) {
            setNormalizedMinValue(0.0)
        } else {
            setNormalizedMinValue(valueToNormalized(value))
        }
    }

    /**
     * Returns the currently selected max value.
     *
     * @return The currently selected max value.
     */
    fun getSelectedMaxValue(): T? {
        return roundOffValueToStep(normalizedToValue(normalizedMaxValue.toDouble()))
    }

    /**
     * Sets the currently selected maximum value. The widget will be invalidated and redrawn.
     *
     * @param value The Number value to set the maximum value to. Will be clamped to given absolute minimum/maximum range.
     */
    fun setSelectedMaxValue(value: T) {
        // in case absoluteMinValue == absoluteMaxValue, avoid division by zero when normalizing.
        if (0.0 == (absoluteMaxValuePrim - absoluteMinValuePrim)) {
            setNormalizedMaxValue(1.0)
        } else {
            setNormalizedMaxValue(valueToNormalized(value))
        }
    }

    /**
     * Registers given listener callback to notify about changed selected values.
     *
     * @param listener The listener to notify about changed selected values.
     */
    fun setOnRangeSeekBarChangeListener(listener: OnRangeSeekBarChangeListener<T>) {
        this.listener = listener
    }

    /**
     * Set the path that defines the shadow of the thumb. This path should be defined assuming
     * that the center of the shadow is at the top left corner (0,0) of the canvas. The
     * [.drawThumbShadow] method will place the shadow appropriately.
     *
     * @param thumbShadowPath The path defining the thumb shadow
     */
    fun setThumbShadowPath(thumbShadowPath: Path) {
        this.thumbShadowPath = thumbShadowPath
    }

    /**
     * Handles thumb selection and movement. Notifies listener callback on certain events.
     */
    override fun onTouchEvent(event: MotionEvent): Boolean {

        if (!isEnabled) {
            return false
        }

        val pointerIndex: Int

        val action = event.action
        when (action and MotionEvent.ACTION_MASK) {

            MotionEvent.ACTION_DOWN -> {
                // Remember where the motion event started
                activePointerId = event.getPointerId(event.pointerCount - 1)
                pointerIndex = event.findPointerIndex(activePointerId)
                downMotionX = event.getX(pointerIndex)

                pressedThumb = evalPressedThumb(downMotionX)

                // Only handle thumb presses.
                if (pressedThumb == null) {
                    return super.onTouchEvent(event)
                }

                isPressed = true
                invalidate()
                onStartTrackingTouch()
                trackTouchEvent(event)
                attemptClaimDrag()
            }
            MotionEvent.ACTION_MOVE -> if (pressedThumb != null) {

                if (isDragging) {
                    trackTouchEvent(event)
                } else {
                    // Scroll to follow the motion event
                    pointerIndex = event.findPointerIndex(activePointerId)
                    val x = event.getX(pointerIndex)

                    if (Math.abs(x - downMotionX) > scaledTouchSlop) {
                        isPressed = true
                        invalidate()
                        onStartTrackingTouch()
                        trackTouchEvent(event)
                        attemptClaimDrag()
                    }
                }

                if (notifyWhileDragging && listener != null) {
                    listener!!.onRangeSeekBarValuesChanged(this, getSelectedMinValue()!!, getSelectedMaxValue()!!)
                }
                if (notifyWhileDragging && mRateListener != null) {
                    val minValue = calculateRate(valueToString(getSelectedMinValue()!!))
                    val maxValue = calculateRate(valueToString(getSelectedMaxValue()!!))
                    mRateListener!!.onRangeSeekBarValuesChanged(this, minValue.roundToLong(), maxValue.roundToLong())
                }
            }
            MotionEvent.ACTION_UP -> {
                if (isDragging) {
                    trackTouchEvent(event)
                    onStopTrackingTouch()
                    setPressed(false)
                } else {
                    // Touch up when we never crossed the touch slop threshold
                    // should be interpreted as a tap-seek to that location.
                    onStartTrackingTouch()
                    trackTouchEvent(event)
                    onStopTrackingTouch()
                }

                pressedThumb = null
                invalidate()
                if (listener != null) {
                    listener!!.onRangeSeekBarValuesChanged(this, getSelectedMinValue()!!, getSelectedMaxValue()!!)
                }
                if (mRateListener != null) {
                    val minValue = calculateRate(valueToString(getSelectedMinValue()!!))
                    val maxValue = calculateRate(valueToString(getSelectedMaxValue()!!))
                    mRateListener!!.onRangeSeekBarValuesChanged(this, Math.round(minValue), Math.round(maxValue))
                }
            }
            MotionEvent.ACTION_POINTER_DOWN -> {
                val index = event.pointerCount - 1
                // final int index = ev.getActionIndex();
                downMotionX = event.getX(index)
                activePointerId = event.getPointerId(index)
                invalidate()
            }
            MotionEvent.ACTION_POINTER_UP -> {
                onSecondaryPointerUp(event)
                invalidate()
            }
            MotionEvent.ACTION_CANCEL -> {
                if (isDragging) {
                    onStopTrackingTouch()
                    isPressed = false
                }
                invalidate() // see above explanation
            }
        }
        return true
    }

    private fun onSecondaryPointerUp(ev: MotionEvent) {
        val pointerIndex = (ev.action and ACTION_POINTER_INDEX_MASK) shr ACTION_POINTER_INDEX_SHIFT

        val pointerId = ev.getPointerId(pointerIndex)
        if (pointerId == activePointerId) {
            // This was our active pointer going up. Choose
            // a new active pointer and adjust accordingly.
            // TODO: Make this decision more intelligent.
            val newPointerIndex = if (pointerIndex == 0) 1 else 0
            downMotionX = ev.getX(newPointerIndex)
            activePointerId = ev.getPointerId(newPointerIndex)
        }
    }

    private fun trackTouchEvent(event: MotionEvent) {
        val pointerIndex = event.findPointerIndex(activePointerId)
        val x = event.getX(pointerIndex)

        if (Thumb.MIN == pressedThumb && !singleThumb) {
            setNormalizedMinValue(screenToNormalized(x))
        } else if (Thumb.MAX == pressedThumb) {
            setNormalizedMaxValue(screenToNormalized(x))
        }
    }

    /**
     * Tries to claim the user's drag motion, and requests disallowing any ancestors from stealing events in the drag.
     */
    private fun attemptClaimDrag() {
        if (parent != null) {
            parent.requestDisallowInterceptTouchEvent(true)
        }
    }

    /**
     * This is called when the user has started touching this widget.
     */
    internal fun onStartTrackingTouch() {
        isDragging = true
    }

    /**
     * This is called when the user either releases his touch or the touch is canceled.
     */
    internal fun onStopTrackingTouch() {
        isDragging = false
    }

    /**
     * Ensures correct size of the widget.
     */
    @Synchronized
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var width = 200
        if (MeasureSpec.UNSPECIFIED != MeasureSpec.getMode(widthMeasureSpec)) {
            width = MeasureSpec.getSize(widthMeasureSpec)
        }

        var height = (thumbImage!!.height
                + (if (!showTextAboveThumbs) 0 else context.dp2px(HEIGHT_IN_DP).toInt())
                + (if (thumbShadow) thumbShadowYOffset?.plus(thumbShadowBlur!!)?.toInt() ?: 0 else 0))
        if (MeasureSpec.UNSPECIFIED != MeasureSpec.getMode(heightMeasureSpec)) {
            height = Math.min(height, MeasureSpec.getSize(heightMeasureSpec))
        }
        setMeasuredDimension(width, height)
    }

    /**
     * Draws the widget on the given canvas.
     */
    @Synchronized
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        paint.textSize = textSize.toFloat()
        paint.style = Paint.Style.FILL
        paint.color = defaultColor
        paint.isAntiAlias = true
        var minMaxLabelSize = 0f

        if (showLabels) {
            // draw min and max labels
            val minLabel = "min"
            val maxLabel = "max"
            minMaxLabelSize = max(paint.measureText(minLabel), paint.measureText(maxLabel))
            val minMaxHeight = textOffset.toFloat() + thumbHalfHeight + (textSize / 3).toFloat()
            canvas.drawText(minLabel, 0f, minMaxHeight, paint)
            canvas.drawText(maxLabel, width - minMaxLabelSize, minMaxHeight, paint)
        }
        padding = internalPad + minMaxLabelSize + thumbHalfWidth

        // draw seek bar background line
        rect!!.left = padding
        rect!!.right = width - padding
        canvas.drawRect(rect!!, paint)

        val selectedValuesAreDefault =
            (normalizedMinValue <= minDeltaForDefault && normalizedMaxValue >= 1 - minDeltaForDefault)

        val colorToUseForButtonsAndHighlightedLine =
            if (!alwaysActive && !activateOnDefaultValues && selectedValuesAreDefault)
                defaultColor
            else
            // default values
                activeColor   // non default, filter is active

        // draw seek bar active range line
        rect!!.left = normalizedToScreen(normalizedMinValue.toDouble())
        rect!!.right = normalizedToScreen(normalizedMaxValue.toDouble())

        paint.color = colorToUseForButtonsAndHighlightedLine
        canvas.drawRect(rect!!, paint)

        // draw minimum thumb (& shadow if requested) if not a single thumb control
        if (!singleThumb) {
            if (thumbShadow) {
                drawThumbShadow(normalizedToScreen(normalizedMinValue.toDouble()), canvas)
            }
            drawThumb(
                normalizedToScreen(normalizedMinValue.toDouble()), Thumb.MIN == pressedThumb, canvas,
                selectedValuesAreDefault
            )
        }

        // draw maximum thumb & shadow (if necessary)
        if (thumbShadow) {
            drawThumbShadow(normalizedToScreen(normalizedMaxValue.toDouble()), canvas)
        }
        drawThumb(
            normalizedToScreen(normalizedMaxValue.toDouble()), Thumb.MAX == pressedThumb, canvas,
            selectedValuesAreDefault
        )

        // draw the text if sliders have moved from default edges
        if (showTextAboveThumbs && (activateOnDefaultValues || !selectedValuesAreDefault)) {
            paint.textSize = textSize.toFloat()
            paint.color = textAboveThumbsColor

            val minValue = calculateRate(valueToString(getSelectedMinValue()!!))
            val maxValue = calculateRate(valueToString(getSelectedMaxValue()!!))
            var minText = ""
            var maxText = ""
            if (minValue == 600000.0)
                minText = "无限"
            else
                minText = "¥" + NumberUtils.formatPriceWithComma(Math.round(minValue))
            if (maxValue == 600000.0)
                maxText = "无限"
            else
                maxText = "¥" + NumberUtils.formatPriceWithComma(Math.round(maxValue))
            val minTextWidth = paint.measureText(minText)
            val maxTextWidth = paint.measureText(maxText)
            // keep the position so that the labels don't get cut off
            var minPosition = Math.max(0f, normalizedToScreen(normalizedMinValue.toDouble()) - minTextWidth * 0.5f)
            var maxPosition =
                Math.min(width - maxTextWidth, normalizedToScreen(normalizedMaxValue.toDouble()) - maxTextWidth * 0.5f)

            if (!singleThumb) {
                // check if the labels overlap, or are too close to each other
                val spacing = context.dp2px(TEXT_LATERAL_PADDING_IN_DP)
                val overlap = minPosition + minTextWidth - maxPosition + spacing
                if (overlap > 0f) {
                    // we could move them the same ("overlap * 0.5f")
                    // but we rather move more the one which is farther from the ends, as it has more space
                    minPosition -= (overlap * normalizedMinValue / (normalizedMinValue + 1 - normalizedMaxValue)).toFloat()
                    maxPosition += (overlap * (1 - normalizedMaxValue) / (normalizedMinValue + 1 - normalizedMaxValue)).toFloat()
                }
                canvas.drawText(
                    minText,
                    minPosition,
                    (distanceToTop + textSize).toFloat(),
                    paint
                )

            }

            canvas.drawText(
                maxText,
                maxPosition,
                (distanceToTop + textSize).toFloat(),
                paint
            )
        }

    }

    protected fun valueToString(value: T): String {
        return (value).toString()
    }

    /**
     * Overridden to save instance state when device orientation changes. This method is called automatically if you assign an id to the RangeSeekBar widget using the [.setId] method. Other members of this class than the normalized min and max values don't need to be saved.
     */
    override fun onSaveInstanceState(): Parcelable? {
        val bundle = Bundle()
        bundle.putParcelable("SUPER", super.onSaveInstanceState())
        bundle.putDouble("MIN", normalizedMinValue.toDouble())
        bundle.putDouble("MAX", normalizedMaxValue.toDouble())
        return bundle
    }

    /**
     * Overridden to restore instance state when device orientation changes. This method is called automatically if you assign an id to the RangeSeekBar widget using the [.setId] method.
     */
    override fun onRestoreInstanceState(parcel: Parcelable) {
        val bundle = parcel as Bundle
        super.onRestoreInstanceState(bundle.getParcelable("SUPER"))
        normalizedMinValue = bundle.getDouble("MIN").toFloat()
        normalizedMaxValue = bundle.getDouble("MAX").toFloat()
    }

    /**
     * Draws the "normal" resp. "pressed" thumb image on specified x-coordinate.
     *
     * @param screenCoord The x-coordinate in screen space where to draw the image.
     * @param pressed     Is the thumb currently in "pressed" state?
     * @param canvas      The canvas to draw upon.
     */
    private fun drawThumb(screenCoord: Float, pressed: Boolean, canvas: Canvas, areSelectedValuesDefault: Boolean) {
        val buttonToDraw: Bitmap?
        if (!activateOnDefaultValues && areSelectedValuesDefault) {
            buttonToDraw = thumbDisabledImage
        } else {
            buttonToDraw = if (pressed) thumbPressedImage else thumbImage
        }

        canvas.drawBitmap(
            buttonToDraw!!, screenCoord - thumbHalfWidth,
            textOffset.toFloat(),
            paint
        )
    }

    /**
     * Draws a drop shadow beneath the slider thumb.
     *
     * @param screenCoord the x-coordinate of the slider thumb
     * @param canvas      the canvas on which to draw the shadow
     */
    private fun drawThumbShadow(screenCoord: Float, canvas: Canvas) {
        thumbShadowMatrix.setTranslate(
            screenCoord + thumbShadowXOffset!!,
            textOffset.toFloat() + thumbHalfHeight + (thumbShadowYOffset ?: 0f)
        )
        translatedThumbShadowPath.set(thumbShadowPath!!)
        translatedThumbShadowPath.transform(thumbShadowMatrix)
        canvas.drawPath(translatedThumbShadowPath, shadowPaint)
    }

    /**
     * Decides which (if any) thumb is touched by the given x-coordinate.
     *
     * @param touchX The x-coordinate of a touch event in screen space.
     * @return The pressed thumb or null if none has been touched.
     */
    private fun evalPressedThumb(touchX: Float): Thumb? {
        var result: Thumb? = null
        val minThumbPressed = isInThumbRange(touchX, normalizedMinValue.toDouble())
        val maxThumbPressed = isInThumbRange(touchX, normalizedMaxValue.toDouble())
        if (minThumbPressed && maxThumbPressed) {
            // if both thumbs are pressed (they lie on top of each other), choose the one with more room to drag. this avoids "stalling" the thumbs in a corner, not being able to drag them apart anymore.
            result = if ((touchX / width > 0.5f)) Thumb.MIN else Thumb.MAX
        } else if (minThumbPressed) {
            result = Thumb.MIN
        } else if (maxThumbPressed) {
            result = Thumb.MAX
        }
        return result
    }

    /**
     * Decides if given x-coordinate in screen space needs to be interpreted as "within" the normalized thumb x-coordinate.
     *
     * @param touchX               The x-coordinate in screen space to check.
     * @param normalizedThumbValue The normalized x-coordinate of the thumb to check.
     * @return true if x-coordinate is in thumb range, false otherwise.
     */
    private fun isInThumbRange(touchX: Float, normalizedThumbValue: Double): Boolean {
        return Math.abs(touchX - normalizedToScreen(normalizedThumbValue)) <= thumbHalfWidth
    }

    /**
     * Sets normalized min value to value so that 0 <= value <= normalized max value <= 1. The View will get invalidated when calling this method.
     *
     * @param value The new normalized min value to set.
     */
    private fun setNormalizedMinValue(value: Double) {
        normalizedMinValue = max(0.0f, min(1.0f, min(value.toFloat(), normalizedMaxValue)))
        invalidate()
    }

    /**
     * Sets normalized max value to value so that 0 <= normalized min value <= value <= 1. The View will get invalidated when calling this method.
     *
     * @param value The new normalized max value to set.
     */
    private fun setNormalizedMaxValue(value: Double) {
        normalizedMaxValue = max(0.0f, min(1.0f, max(value.toFloat(), normalizedMinValue)))
        invalidate()
    }

    /**
     * Converts a normalized value to a Number object in the value space between absolute minimum and maximum.
     */
    protected fun normalizedToValue(normalized: Double): T {
        val v = absoluteMinValuePrim + normalized * (absoluteMaxValuePrim - absoluteMinValuePrim)
        // TODO parameterize this rounding to allow variable decimal points
        return numberType?.toNumber((v * 100).roundToInt() / 100.0) as T
    }

    /**
     * Converts the given Number value to a normalized double.
     *
     * @param value The Number value to normalize.
     * @return The normalized double.
     */
    protected fun valueToNormalized(value: T): Double {
        return if (0.0 == absoluteMaxValuePrim - absoluteMinValuePrim) {
            // prevent division by zero, simply return 0.
            0.0
        } else (value.toDouble() - absoluteMinValuePrim) / (absoluteMaxValuePrim - absoluteMinValuePrim)
    }

    /**
     * Converts a normalized value into screen space.
     *
     * @param normalizedCoord The normalized value to convert.
     * @return The converted value in screen space.
     */
    private fun normalizedToScreen(normalizedCoord: Double): Float {
        return (padding + normalizedCoord * (width - 2 * padding)).toFloat()
    }

    /**
     * Converts screen space x-coordinates into normalized values.
     *
     * @param screenCoord The x-coordinate in screen space to convert.
     * @return The normalized value.
     */
    private fun screenToNormalized(screenCoord: Float): Double {
        val width = width
        if (width <= 2 * padding) {
            // prevent division by zero, simply return 0.
            return 0.0
        } else {
            val result = ((screenCoord - padding) / (width - 2 * padding)).toDouble()
            return Math.min(1.0, Math.max(0.0, result))
        }
    }

    /**
     * Thumb constants (min and max).
     */
    private enum class Thumb {
        MIN, MAX
    }

    /**
     * Utility enumeration used to convert between Numbers and doubles.
     *
     * @author Stephan Tittel (stephan.tittel@kom.tu-darmstadt.de)
     */
    protected enum class NumberType {
        LONG, DOUBLE, INTEGER, FLOAT, SHORT, BYTE, BIG_DECIMAL;

        fun toNumber(value: Double): Number {
            when (this) {
                LONG -> return value.toLong()
                DOUBLE -> return value
                INTEGER -> return value.toInt()
                FLOAT -> return value.toFloat()
                SHORT -> return value.toShort()
                BYTE -> return value.toByte()
                BIG_DECIMAL -> return BigDecimal.valueOf(value)
            }
            throw InstantiationError("can't convert $this to a Number object")
        }

        companion object {

            @Throws(IllegalArgumentException::class)
            fun <E : Number> fromNumber(value: E): NumberType {
                if (value is Long) {
                    return LONG
                }
                if (value is Double) {
                    return DOUBLE
                }
                if (value is Int) {
                    return INTEGER
                }
                if (value is Float) {
                    return FLOAT
                }
                if (value is Short) {
                    return SHORT
                }
                if (value is Byte) {
                    return BYTE
                }
                if (value is BigDecimal) {
                    return BIG_DECIMAL
                }
                throw IllegalArgumentException("Number class '" + value.javaClass.name + "' is not supported")
            }
        }
    }

    /**
     * Callback listener interface to notify about changed range values.
     *
     * @param <T> The Number type the RangeSeekBar has been declared with.
     * @author Stephan Tittel (stephan.tittel@kom.tu-darmstadt.de)
    </T> */
    interface OnRangeSeekBarChangeListener<T : Number> {
        fun onRangeSeekBarValuesChanged(bar: RangeSeekBar<T>, minValue: T, maxValue: T)
    }

    fun setOnRangeRateSeekBarChangeListener(listener: OnRangeRateSeekBarChangeListener) {
        this.mRateListener = listener
    }

    interface OnRangeRateSeekBarChangeListener {
        fun onRangeSeekBarValuesChanged(bar: RangeSeekBar<*>, minValue: Long, maxValue: Long)
    }

    /**
     * 数值转换
     *
     *
     * 一般用于显示数值和实际数值不一样的情况
     *
     *
     * @param str
     * @return
     */
    fun calculateRate(str: String): Double {
        var value = java.lang.Double.valueOf(str)
        when {
            value <= 200000 -> value /= 10
            value <= 220000 -> value / 9.7
            value <= 240000 -> value / 9.5
            value <= 250000 -> value / 9.3
            value <= 260000 -> value / 9
            value <= 270000 -> value / 8.8
            value <= 280000 -> value / 8.5
            value <= 300000 -> value / 8
            value <= 320000 -> value / 7.8
            value <= 340000 -> value / 7.5
            value <= 370000 -> value / 7.3
            value <= 380000 -> value / 7.2
            value <= 400000 -> value / 7
            value <= 410000 -> value / 6.8
            value <= 420000 -> value / 6.2
            value <= 430000 -> value / 6
            value <= 435000 -> value / 5.8
            value <= 440000 -> value / 5.5
            value <= 445000 -> value / 5.3
            value <= 450000 -> value / 5.1
            value <= 452000 -> value / 5
            value <= 453000 -> value / 4.53
            value <= 455000 -> value / 4.5
            value <= 460000 -> value / 4
            value <= 465000 -> value / 3.5
            value <= 468000 -> value / 3.2
            value <= 470000 -> value / 3
            value <= 473000 -> value / 2.8
            value <= 475000 -> value / 2.5
            value <= 478000 -> value / 2.2
            value <= 480000 -> value / 2
            value <= 482000 -> value / 1.8
            value <= 483000 -> value / 1.61
            value <= 485000 -> value / 1.5
            value <= 488000 -> value / 1.3
            value <= 490000 -> value / 1.2
            value <= 495000 -> value / 1.1
            value <= 498000 -> value / 1
        }
        return value
    }
}