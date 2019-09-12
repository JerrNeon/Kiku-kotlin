package com.jn.kikukt.utils.glide

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.support.v4.app.Fragment
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.jn.kikukt.common.utils.getIntDip
import jp.wasabeef.glide.transformations.BlurTransformation
import jp.wasabeef.glide.transformations.RoundedCornersTransformation

object GlideTransformUtil {

    private fun getContext(context: Any): Context? {
        return when (context) {
            is Activity -> context.applicationContext
            is Fragment -> context.context
            else -> context as? Context ?: throw IllegalArgumentException("context type is no correct")
        }
    }

    /**
     * 加载'圆角'高斯模糊图片
     * GlideCircleBorderTransform
     * @param radius  圆角半径
     * @param blurred 设置模糊度(在0.0到25.0之间)，默认”25"
     */
    fun withRadiusBlurred(context: Any, radius: Int, blurred: Int): MultiTransformation<Bitmap> {
        return MultiTransformation(
            BlurTransformation(blurred),
            CenterCrop(),
            RoundedCornersTransformation(
                getContext(context)?.getIntDip(radius.toFloat()) ?: 0,
                0,
                RoundedCornersTransformation.CornerType.ALL
            )
        )
    }

    /**
     * 加载高斯模糊图片
     *
     * @param blurred 设置模糊度(在0.0到25.0之间)，默认”25"
     * @param scale   图片缩放比例,默认“1”。
     */
    fun withBlurred(blurred: Int, scale: Int): MultiTransformation<Bitmap> {
        return MultiTransformation(BlurTransformation(blurred, scale))
    }

    /**
     * 加载'圆角'图片
     *
     * @param radius 圆角半径
     */
    fun withRadius(context: Any, radius: Int): MultiTransformation<Bitmap> {
        return MultiTransformation(
            CenterCrop(),
            RoundedCornersTransformation(
                getContext(context)?.getIntDip(radius.toFloat()) ?: 0,
                0,
                RoundedCornersTransformation.CornerType.ALL
            )
        )
    }

    /**
     * 加载圆形图片带外框，颜色
     *
     * @param context     上下文
     * @param borderColor 外圈圆颜色
     * @param borderWith  外圈圆宽度
     */
    fun withCircleBorder(context: Any, borderWith: Int, borderColor: Int): MultiTransformation<Bitmap> {
        return MultiTransformation(GlideCircleBorderTransform(borderWith, borderColor))
    }

    /**
     * 加载'圆角'图片
     *
     * @param radius     圆角半径
     * @param cornerType 圆角位置
     */
    fun withRadiusAndCornerType(
        context: Any,
        radius: Int,
        cornerType: RoundedCornersTransformation.CornerType
    ): MultiTransformation<Bitmap> {
        return MultiTransformation(
            CenterCrop(),
            RoundedCornersTransformation(getContext(context)?.getIntDip(radius.toFloat()) ?: 0, 0, cornerType)
        )
    }

    /**
     * 加载'圆角'图片
     *
     * @param radius     圆角半径
     * @param cornerType 圆角位置
     */
    fun withRadiusAndCornerType_Natural(
        context: Any,
        radius: Int,
        cornerType: RoundedCornersTransformation.CornerType
    ): MultiTransformation<Bitmap> {
        return MultiTransformation(
            RoundedCornersTransformation(
                getContext(context)?.getIntDip(radius.toFloat()) ?: 0, 0, cornerType
            )
        )
    }
}
