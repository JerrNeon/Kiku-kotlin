package com.jn.kikukt.dialog

import android.view.Gravity
import android.view.View
import android.view.WindowManager
import com.jn.kikukt.R
import com.jn.kikukt.utils.dialog.DialogFragmentUtils
import com.jn.kikukt.common.utils.getScreenWidth
import com.jn.kikukt.common.SPManage
import kotlinx.android.synthetic.main.dialog_tokeninvalid.view.*

/**
 * Author：Stevie.Chen Time：2019/7/15
 * Class Comment：Token失效或被顶号对话框
 */
class TokenInvalidDialogFragment : RootDialogFragment() {

    companion object {
        fun newInstance(): TokenInvalidDialogFragment = TokenInvalidDialogFragment()
    }

    override fun getLayoutResourceId(): Int {
        return R.layout.dialog_tokeninvalid
    }

    override fun getAnimationStyle(): Int {
        return 0
    }

    override fun getCanceledOnTouchOutsideEnable(): Boolean {
        return false
    }

    override fun getLayoutParams(): WindowManager.LayoutParams? {
        val params = mWindow!!.attributes
        params.gravity = Gravity.CENTER//居中显示
        params.width = (mContext.getScreenWidth() * 0.7).toInt()//宽度为全屏的70%
        return params
    }

    override fun initView() {
        super.initView()
        mView!!.tv_commit.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        super.onClick(view)
        if (view.id == R.id.tv_commit) {
            SPManage.instance.clearUserInfo()
            openLoginActivity()
            this.dismiss()
            DialogFragmentUtils.onDestroyTokenValidDialog()
        }
    }

    /**
     * 打开登录界面并更新用户信息
     *
     *
     * 更新用户可以用EventBus或BroadCastReceiver,是具体情况而定
     *
     */
    protected fun openLoginActivity() {

    }

    override fun onDestroyView() {
        DialogFragmentUtils.onDestroyTokenValidDialog()
        super.onDestroyView()
    }
}