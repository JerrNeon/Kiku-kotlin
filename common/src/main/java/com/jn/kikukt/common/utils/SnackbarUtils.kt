package com.jn.kikukt.common.utils

import androidx.annotation.ColorInt
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import com.google.android.material.snackbar.Snackbar
import java.lang.ref.WeakReference

/**
 * Author：Stevie.Chen Time：2019/7/15
 * Class Comment：
 */
object SnackbarUtils {

    private var snackbarWeakReference: WeakReference<Snackbar>? = null

    /**
     * 显示短时snackbar
     *
     * @param parent    父视图(CoordinatorLayout或者DecorView)
     * @param text      文本
     * @param textColor 文本颜色
     * @param bgColor   背景色
     */
    fun showShortSnackbar(parent: View, text: CharSequence, @ColorInt textColor: Int, @ColorInt bgColor: Int) {
        showSnackbar(parent, text, Snackbar.LENGTH_SHORT, textColor, bgColor, null, -1, null)
    }

    /**
     * 显示短时snackbar
     *
     * @param parent          父视图(CoordinatorLayout或者DecorView)
     * @param text            文本
     * @param textColor       文本颜色
     * @param bgColor         背景色
     * @param actionText      事件文本
     * @param actionTextColor 事件文本颜色
     * @param listener        监听器
     */
    fun showShortSnackbar(
        parent: View, text: CharSequence, @ColorInt textColor: Int, @ColorInt bgColor: Int,
        actionText: CharSequence, actionTextColor: Int, listener: View.OnClickListener
    ) {
        showSnackbar(
            parent, text, Snackbar.LENGTH_SHORT, textColor, bgColor,
            actionText, actionTextColor, listener
        )
    }

    /**
     * 显示长时snackbar
     *
     * @param parent    视图(CoordinatorLayout或者DecorView)
     * @param text      文本
     * @param textColor 文本颜色
     * @param bgColor   背景色
     */
    fun showLongSnackbar(parent: View, text: CharSequence, @ColorInt textColor: Int, @ColorInt bgColor: Int) {
        showSnackbar(parent, text, Snackbar.LENGTH_LONG, textColor, bgColor, null, -1, null)
    }

    /**
     * 显示长时snackbar
     *
     * @param parent          视图(CoordinatorLayout或者DecorView)
     * @param text            文本
     * @param textColor       文本颜色
     * @param bgColor         背景色
     * @param actionText      事件文本
     * @param actionTextColor 事件文本颜色
     * @param listener        监听器
     */
    fun showLongSnackbar(
        parent: View, text: CharSequence, @ColorInt textColor: Int, @ColorInt bgColor: Int,
        actionText: CharSequence, actionTextColor: Int, listener: View.OnClickListener
    ) {
        showSnackbar(
            parent, text, Snackbar.LENGTH_LONG, textColor, bgColor,
            actionText, actionTextColor, listener
        )
    }

    /**
     * 显示自定义时长snackbar
     *
     * @param parent    父视图(CoordinatorLayout或者DecorView)
     * @param text      文本
     * @param textColor 文本颜色
     * @param bgColor   背景色
     */
    fun showIndefiniteSnackbar(parent: View, text: CharSequence, @ColorInt textColor: Int, @ColorInt bgColor: Int) {
        showSnackbar(parent, text, Snackbar.LENGTH_INDEFINITE, textColor, bgColor, null, -1, null)
    }

    /**
     * 显示自定义时长snackbar
     *
     * @param parent          父视图(CoordinatorLayout或者DecorView)
     * @param text            文本
     * @param textColor       文本颜色
     * @param bgColor         背景色
     * @param actionText      事件文本
     * @param actionTextColor 事件文本颜色
     * @param listener        监听器
     */
    fun showIndefiniteSnackbar(
        parent: View, text: CharSequence, @ColorInt textColor: Int, @ColorInt bgColor: Int,
        actionText: CharSequence, actionTextColor: Int, listener: View.OnClickListener
    ) {
        showSnackbar(
            parent, text, Snackbar.LENGTH_INDEFINITE, textColor, bgColor,
            actionText, actionTextColor, listener
        )
    }

    /**
     * 设置snackbar文字和背景颜色
     *
     * @param parent          父视图(CoordinatorLayout或者DecorView)
     * @param text            文本
     * @param duration        显示时长
     * @param textColor       文本颜色
     * @param bgColor         背景色
     * @param actionText      事件文本
     * @param actionTextColor 事件文本颜色
     * @param listener        监听器
     */
    private fun showSnackbar(
        parent: View, text: CharSequence,
        duration: Int,
        @ColorInt textColor: Int, @ColorInt bgColor: Int,
        actionText: CharSequence?, actionTextColor: Int,
        listener: View.OnClickListener?
    ) {
        val spannableString = SpannableString(text)
        val colorSpan = ForegroundColorSpan(textColor)
        spannableString.setSpan(colorSpan, 0, spannableString.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        snackbarWeakReference = WeakReference(Snackbar.make(parent, spannableString, duration))
        val snackbar = snackbarWeakReference!!.get()
        val view = snackbar!!.view
        view.setBackgroundColor(bgColor)
        if (actionText != null && actionText.length > 0 && listener != null) {
            snackbar.setActionTextColor(actionTextColor)
            snackbar.setAction(actionText, listener)
        }
        snackbar.show()
    }

    /**
     * 为snackbar添加布局
     *
     * 在show...Snackbar之后调用
     *
     * @param layoutId 布局文件
     * @param index    位置(the position at which to add the child or -1 to add last)
     */
    fun addView(layoutId: Int, index: Int) {
        val snackbar = snackbarWeakReference!!.get()
        if (snackbar != null) {
            val view = snackbar.view
            val layout = view as Snackbar.SnackbarLayout
            val child = LayoutInflater.from(view.getContext()).inflate(layoutId, null)
            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            params.gravity = Gravity.CENTER_VERTICAL
            layout.addView(child, index, params)
        }
    }

    /**
     * 取消snackbar显示
     */
    fun dismissSnackbar() {
        if (snackbarWeakReference != null && snackbarWeakReference!!.get() != null) {
            snackbarWeakReference!!.get()!!.dismiss()
            snackbarWeakReference = null
        }
    }
}