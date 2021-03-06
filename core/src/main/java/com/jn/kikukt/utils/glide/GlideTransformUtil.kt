package com.jn.kikukt.utils.glide

import android.content.Context
import android.graphics.Bitmap
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.jn.kikukt.common.utils.getIntDip
import jp.wasabeef.glide.transformations.BlurTransformation
import jp.wasabeef.glide.transformations.RoundedCornersTransformation

object GlideTransformUtil {

    /**
     * 加载'圆角'高斯模糊图片
     * GlideCircleBorderTransform
     * @param radius  圆角半径
     * @param blurred 设置模糊度(在0.0到25.0之间)，默认”25"
     */
    fun withRadiusBlurred(
        context: Context,
        radius: Int,
        blurred: Int
    ): MultiTransformation<Bitmap> {
        return MultiTransformation(
            BlurTransformation(blurred),
            CenterCrop(),
            RoundedCornersTransformation(
                context.getIntDip(radius.toFloat()),
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
    fun withRadius(context: Context, radius: Int): MultiTransformation<Bitmap> {
        return MultiTransformation(
            CenterCrop(),
            RoundedCornersTransformation(
                context.getIntDip(radius.toFloat()),
                0,
                RoundedCornersTransformation.CornerType.ALL
            )
        )
    }

    /**
     * 加载圆形图片带外框，颜色
     *
     * @param borderColor 外圈圆颜色
     * @param borderWith  外圈圆宽度
     */
    fun withCircleBorder(
        borderWith: Int,
        borderColor: Int
    ): MultiTransformation<Bitmap> {
        return MultiTransformation(GlideCircleBorderTransform(borderWith, borderColor))
    }

    /**
     * 加载'圆角'图片
     *
     * @param radius     圆角半径
     * @param cornerType 圆角位置
     */
    fun withRadiusAndCornerType(
        context: Context,
        radius: Int,
        cornerType: RoundedCornersTransformation.CornerType
    ): MultiTransformation<Bitmap> {
        return MultiTransformation(
            CenterCrop(),
            RoundedCornersTransformation(context.getIntDip(radius.toFloat()), 0, cornerType)
        )
    }

    /**
     * 加载'圆角'图片
     *
     * @param radius     圆角半径
     * @param cornerType 圆角位置
     */
    fun withRadiusAndCornerTypeNatural(
        context: Context,
        radius: Int,
        cornerType: RoundedCornersTransformation.CornerType
    ): MultiTransformation<Bitmap> {
        return MultiTransformation(
            RoundedCornersTransformation(
                context.getIntDip(radius.toFloat()), 0, cornerType
            )
        )
    }
}
