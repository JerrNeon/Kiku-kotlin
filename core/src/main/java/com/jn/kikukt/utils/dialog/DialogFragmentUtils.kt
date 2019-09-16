package com.jn.kikukt.utils.dialog

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.jn.kikukt.dialog.TokenInvalidDialogFragment
import java.lang.ref.WeakReference

/**
 * Author：Stevie.Chen Time：2019/7/15
 * Class Comment：对话框工具类
 */
object DialogFragmentUtils {

    private var mTokenInvalidDialogWR: WeakReference<TokenInvalidDialogFragment>? = null

    /**
     * 显示Token失效对话框
     *
     * @param context
     */
    fun showTokenValidDialog(context: Context) {
        if (mTokenInvalidDialogWR == null && context is AppCompatActivity) {
            mTokenInvalidDialogWR = WeakReference(TokenInvalidDialogFragment.newInstance())
            mTokenInvalidDialogWR?.get()?.show(
                context.supportFragmentManager, TokenInvalidDialogFragment::class.java.simpleName
            )
        }
    }

    /**
     * 释放Token失效对话框资源
     */
    fun onDestroyTokenValidDialog() {
        mTokenInvalidDialogWR?.clear()
    }
}