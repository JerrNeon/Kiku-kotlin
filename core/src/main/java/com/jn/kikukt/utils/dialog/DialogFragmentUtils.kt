package com.jn.kikukt.utils.dialog

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.jn.kikukt.common.leak.Weak
import com.jn.kikukt.dialog.TokenInvalidDialogFragment

/**
 * Author：Stevie.Chen Time：2019/7/15
 * Class Comment：对话框工具类
 */
object DialogFragmentUtils {

    private var tokenInvalidDialog: TokenInvalidDialogFragment? by Weak()

    /**
     * 显示Token失效对话框
     */
    fun showTokenValidDialog(activity: AppCompatActivity) {
        if (tokenInvalidDialog == null) {
            tokenInvalidDialog = TokenInvalidDialogFragment.newInstance()
            activity.lifecycle.addObserver(object : LifecycleEventObserver {
                override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                    if (Lifecycle.Event.ON_DESTROY == event) {
                        tokenInvalidDialog = null
                    }
                }
            })
        }
        tokenInvalidDialog?.show(
            activity.supportFragmentManager, TokenInvalidDialogFragment::class.java.simpleName
        )
    }
}