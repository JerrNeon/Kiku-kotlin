package com.jn.kikukt.common

import android.annotation.TargetApi
import android.app.Activity
import android.os.Build
import android.view.View
import android.view.ViewTreeObserver
import android.view.WindowManager
import com.jn.kikukt.common.utils.getContentViewInvisibleHeight

/**
 * Author：Stevie.Chen Time：2019/7/15
 * Class Comment：软键盘
 */
class SoftKeyBoardManager {

    private var sContentViewInvisibleHeightPre: Int = 0//窗口可视区域大小
    private var onSoftInputChangedListener: OnSoftInputChangedListener? = null
    private var onGlobalLayoutListener: ViewTreeObserver.OnGlobalLayoutListener? = null

    companion object {
        val instance: SoftKeyBoardManager by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            SoftKeyBoardManager()
        }
    }

    /**
     * Register soft input changed listener.
     *
     * @param activity The activity.
     * @param listener The soft input changed listener.
     */
    fun registerSoftInputChangedListener(
        activity: Activity,
        listener: OnSoftInputChangedListener
    ) {
        val flags = activity.window.attributes.flags
        if (flags and WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS != 0) {
            activity.window.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        }
        val contentView = activity.findViewById<View>(android.R.id.content)
        sContentViewInvisibleHeightPre = activity.getContentViewInvisibleHeight()
        onSoftInputChangedListener = listener
        onGlobalLayoutListener = ViewTreeObserver.OnGlobalLayoutListener {
            if (onSoftInputChangedListener != null) {
                val height = activity.getContentViewInvisibleHeight()
                if (sContentViewInvisibleHeightPre != height) {
                    onSoftInputChangedListener?.onSoftInputChanged(height)
                    sContentViewInvisibleHeightPre = height
                }
            }
        }
        contentView.viewTreeObserver.addOnGlobalLayoutListener(onGlobalLayoutListener)
    }

    /**
     * unRegister soft input changed listener.
     *
     * @param activity The activity.
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    fun unregisterSoftInputChangedListener(activity: Activity) {
        val contentView = activity.findViewById<View>(android.R.id.content)
        contentView.viewTreeObserver.removeOnGlobalLayoutListener(onGlobalLayoutListener)
        onSoftInputChangedListener = null
        onGlobalLayoutListener = null
    }

    interface OnSoftInputChangedListener {
        fun onSoftInputChanged(height: Int)
    }
}