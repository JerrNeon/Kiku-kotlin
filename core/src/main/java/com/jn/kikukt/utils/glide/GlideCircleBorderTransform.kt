package com.jn.kikukt.utils.glide

import android.content.res.Resources
import android.graphics.*
import com.bumptech.glide.load.Key
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation

import java.io.UnsupportedEncodingException
import java.security.MessageDigest
import kotlin.math.min

class GlideCircleBorderTransform(borderWidth: Int, borderColor: Int) : BitmapTransformation() {
    private val mBorderPaint: Paint?
    private val mBorderWidth: Float = Resources.getSystem().displayMetrics.density * borderWidth

    init {
        mBorderPaint = Paint()
        mBorderPaint.isDither = true
        mBorderPaint.isAntiAlias = true
        mBorderPaint.color = borderColor
        mBorderPaint.style = Paint.Style.STROKE
        mBorderPaint.strokeWidth = mBorderWidth
    }

    override fun transform(pool: BitmapPool, toTransform: Bitmap, outWidth: Int, outHeight: Int): Bitmap? {
        return circleCrop(pool, toTransform)
    }

    private fun circleCrop(pool: BitmapPool, source: Bitmap?): Bitmap? {
        if (source == null) return null

        val size = (min(source.width, source.height) - mBorderWidth / 2).toInt()
        val x = (source.width - size) / 2
        val y = (source.height - size) / 2
        val squared = Bitmap.createBitmap(source, x, y, size, size)
        var result: Bitmap? = pool.get(size, size, Bitmap.Config.ARGB_8888)
        if (result == null) {
            result = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
        }
        val canvas = Canvas(result!!)
        val paint = Paint()
        paint.shader = BitmapShader(squared, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        paint.isAntiAlias = true
        val r = size / 2f
        canvas.drawCircle(r, r, r, paint)
        if (mBorderPaint != null) {
            val borderRadius = r - mBorderWidth / 2
            canvas.drawCircle(r, r, borderRadius, mBorderPaint)
        }
        return result
    }


    override fun equals(other: Any?): Boolean {
        return other is GlideCircleBorderTransform
    }

    override fun hashCode(): Int {
        return ID.hashCode()
    }

    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
        try {
            messageDigest.update(ID.toByteArray(charset(Key.STRING_CHARSET_NAME)))
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }

    }

    companion object {

        private const val ID = "com.jn.kikukt.utils.glide.GlideCircleBorderTransform"
    }


}
