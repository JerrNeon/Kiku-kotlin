package com.jn.kikukt.common.utils

import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import java.lang.reflect.Field

/**
 * Author：Stevie.Chen Time：2019/7/15
 * Class Comment：
 */

/**
 * 打开软键盘
 */
fun Context.openKeyboard(mEditText: EditText) {
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.showSoftInput(mEditText, InputMethodManager.RESULT_SHOWN)
    imm.toggleSoftInput(
        InputMethodManager.SHOW_FORCED,
        InputMethodManager.HIDE_IMPLICIT_ONLY
    )
}

/**
 * 关闭软键盘
 */
fun Context.closeKeyboard(mEditText: EditText) {
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(mEditText.windowToken, 0)
}

/**
 * 关闭软件盘
 */
fun Activity.closeSoftKeyboard() {
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(window.decorView.windowToken, 0)
}

/**
 * 解决InputMethodManager内存泄漏的问题
 */
fun Context.fixInputMethodManagerLeak() {
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

    val arr = arrayOf("mCurRootView", "mServedView", "mNextServedView", "mLastSrvView")
    var f: Field? = null
    var objGet: Any? = null
    for (i in arr.indices) {
        val param = arr[i]
        try {
            f = imm.javaClass.getDeclaredField(param)
            if (!f.isAccessible) {
                f.isAccessible = true
            } // author: sodino mail:sodino@qq.com
            objGet = f.get(imm)
            if (objGet != null && objGet is View) {
                val vGet = objGet as View?
                if (vGet!!.context === this) { // 被InputMethodManager持有引用的context是想要目标销毁的
                    f.set(imm, null) // 置空，破坏掉path to gc节点
                } else {
                    // 不是想要目标销毁的，即为又进了另一层界面了，不要处理，避免影响原逻辑,也就不用继续for循环了
                    break
                }
            }
        } catch (t: Throwable) {
            t.printStackTrace()
        }

    }
}

/**
 * Return whether soft input is visible.
 *
 * The minimum height is 200
 * @return `true`: yes<br></br>`false`: no
 */
fun Activity.isSoftInputVisible(): Boolean {
    return isSoftInputVisible(200)
}

/**
 * Return whether soft input is visible.
 *
 * @param minHeightOfSoftInput The minimum height of soft input.
 * @return `true`: yes<br></br>`false`: no
 */
fun Activity.isSoftInputVisible(minHeightOfSoftInput: Int): Boolean {
    return getContentViewInvisibleHeight() >= minHeightOfSoftInput
}

/**
 * 获取窗口可视区域大小
 */
fun Activity.getContentViewInvisibleHeight(): Int {
    val contentView = findViewById<View>(android.R.id.content)
    val outRect = Rect()
    contentView.getWindowVisibleDisplayFrame(outRect)
    return contentView.bottom - outRect.bottom
}
