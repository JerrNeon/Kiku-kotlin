package com.jn.kikukt.utils.glide

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.widget.ImageView
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.bumptech.glide.request.target.DrawableImageViewTarget
import com.jn.kikukt.BuildConfig
import com.jn.kikukt.R
import com.jn.kikukt.utils.glide.GlideTransformUtil.withRadius

/**
 * Author：Stevie.Chen Time：2019/7/10
 * Class Comment：Glide图片Util
 */
object GlideUtil {
    /**
     * 恢复加载
     */
    fun resumeRequests(context: Any) {
        getRequestManager(context).resumeRequests()
    }

    /**
     * 停止加载
     */
    fun pauseRequests(context: Any) {
        getRequestManager(context).pauseRequests()
    }

    /**
     * 清除内存
     *
     * @param context
     */
    fun clearMemory(context: Context) {
        Glide.get(context).clearMemory()
    }

    /**
     * 交给 Glide 处理内存情况。
     *
     * @param context
     */
    fun trimMemory(context: Context, level: Int) {
        Glide.get(context).trimMemory(level)
    }

}

/**
 * @param placeholderResourceId
 * @param errorResourceId
 * @return
 *
 *
 * DiskCacheStrategy有五个常量：
 * DiskCacheStrategy.ALL 使用DATA和RESOURCE缓存远程数据，仅使用RESOURCE来缓存本地数据。
 * DiskCacheStrategy.NONE 不使用磁盘缓存
 * DiskCacheStrategy.DATA 在资源解码前就将原始数据写入磁盘缓存
 * DiskCacheStrategy.RESOURCE 在资源解码后将数据写入磁盘缓存，即经过缩放等转换后的图片资源。
 * DiskCacheStrategy.AUTOMATIC 根据原始图片数据和资源编码策略来自动选择磁盘缓存策略。
 * 默认的策略是DiskCacheStrategy.AUTOMATIC
 *
 */
@SuppressLint("CheckResult")
private fun getRequestOptions(
    placeholderResourceId: Int,
    errorResourceId: Int,
    isCache: Boolean
): RequestOptions {
    val requestOptions = RequestOptions()
        .placeholder(placeholderResourceId)
        .error(errorResourceId)
        .priority(Priority.HIGH)
    if (!isCache) {
        requestOptions.skipMemoryCache(true)
        requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE)
    }
    return requestOptions
}

/**
 * 获取图片请求管理对象
 *
 * @param context context资源对象
 * @return
 */
private fun getRequestManager(context: Any): RequestManager {
    return when (context) {
        is RequestManager -> context
        is Activity -> Glide.with(context)
        is Fragment -> Glide.with(context)
        is Context -> Glide.with(context)
        else -> throw IllegalArgumentException("context type is no correct")
    }
}

/*--------------------------------------------------------------------------------------------------------------------*/

fun Activity.requestManager() = Glide.with(this)
fun Fragment.requestManager() = Glide.with(this)

fun ImageView.loadImage(
    context: Any,
    url: String,
    placeholderResourceId: Int = R.drawable.ic_default_placeholder,
    errorResourceId: Int = 0,
    isCache: Boolean = true,
    isGif: Boolean = false,
    isCircle: Boolean = false,
    radius: Int? = null
) {
    if (BuildConfig.DEBUG && isCircle && (isGif || radius != null)) {
        error("Assertion failed")
    }
    val requestManager = getRequestManager(context)
    val requestOptions = getRequestOptions(placeholderResourceId, errorResourceId, isCache).apply {
        if (radius != null) {
            transform(withRadius(getContext(), radius))
        }
    }
    if (isCircle) {
        val requestBuilder: RequestBuilder<Bitmap> =
            requestManager.asBitmap().load(url)
        val circleBitmapImageViewTarget = object : BitmapImageViewTarget(this) {
            override fun setResource(resource: Bitmap?) {
                val circularBitmapDrawable =
                    RoundedBitmapDrawableFactory.create(resources, resource).apply {
                        isCircular = true
                    }
                this@loadImage.setImageDrawable(circularBitmapDrawable)
            }
        }
        requestBuilder.apply(requestOptions).into(circleBitmapImageViewTarget)
    } else {
        val requestBuilder: RequestBuilder<*> =
            if (isGif)
                requestManager.asGif().load(url)
            else
                requestManager.load(url)
        requestBuilder.apply(requestOptions).into(this)
    }
}

fun ImageView.loadImage(
    context: Any,
    url: String,
    placeholderResourceId: Int = R.drawable.ic_default_placeholder,
    errorResourceId: Int = 0,
    drawableImageViewTarget: DrawableImageViewTarget
) {
    val requestManager = getRequestManager(context)
    val requestOptions = getRequestOptions(placeholderResourceId, errorResourceId, false)
    requestManager.load(url).apply(requestOptions).into(drawableImageViewTarget)
}

fun ImageView.loadImage(
    context: Any,
    url: String,
    placeholderResourceId: Int = R.drawable.ic_default_placeholder,
    errorResourceId: Int = 0,
    bitmapImageViewTarget: BitmapImageViewTarget
) {
    val requestManager = getRequestManager(context)
    val requestOptions = getRequestOptions(placeholderResourceId, errorResourceId, false)
    requestManager.asBitmap().load(url).apply(requestOptions).into(bitmapImageViewTarget)
}