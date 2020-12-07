package com.jn.kikukt.dialog

import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import com.jn.kikukt.R
import com.jn.kikukt.common.utils.getScreenWidth

/**
 * Author：Stevie.Chen Time：2019/7/15
 * Class Comment：Token失效或被顶号对话框
 */
open class TokenInvalidDialogFragment : RootDialogFragment(), View.OnClickListener {

    companion object {
        fun newInstance(): TokenInvalidDialogFragment = TokenInvalidDialogFragment()
    }

    override val layoutResId: Int = R.layout.dialog_tokeninvalid

    override val layoutParams: WindowManager.LayoutParams?
        get() = mWindow?.attributes?.apply {
            gravity = Gravity.CENTER//居中显示
            val screenWidth = context?.getScreenWidth()?.toFloat() ?: 0f
            width = (screenWidth * 0.7f).toInt()//宽度为全屏的70%
        }

    override fun initView(view: View) {
        super.initView()
        view.findViewById<TextView>(R.id.tv_commit).setOnClickListener(this)
    }

    override fun onClick(view: View) {
        if (view.id == R.id.tv_commit) {
            openLoginActivity()
            this.dismiss()
        }
    }

    /**
     * 打开登录界面并更新用户信息
     *
     * 更新用户可以用EventBus或BroadCastReceiver,是具体情况而定
     */
    open fun openLoginActivity() {}
}