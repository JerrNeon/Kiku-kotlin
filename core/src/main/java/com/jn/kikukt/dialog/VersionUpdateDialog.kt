package com.jn.kikukt.dialog

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.FragmentManager
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import com.jn.kikukt.R
import com.jn.kikukt.entiy.VersionUpdateVO
import com.jn.kikukt.service.VersionUpdateService
import com.jn.kikukt.common.utils.getScreenWidth

/**
 * Author：Stevie.Chen Time：2019/7/15
 * Class Comment：版本更新对话框
 */
class VersionUpdateDialog : RootDialogFragment() {

    private var mTvVersionUpdateContent: TextView? = null//更新内容
    private var mTvVersionUpdateCancel: TextView? = null//取消
    private var mTvVersionUpdateSubmit: TextView? = null//确定

    private var mVersionUpdateVO: VersionUpdateVO? = null
    private var mOnClickListener: View.OnClickListener? = null

    companion object {
        fun newInstance(versionUpdateVO: VersionUpdateVO): VersionUpdateDialog {
            val dialog = VersionUpdateDialog()
            val bundle = Bundle()
            bundle.putParcelable(VersionUpdateVO::class.java.simpleName, versionUpdateVO)
            dialog.arguments = bundle
            return dialog
        }
    }

    override fun getLayoutResourceId(): Int {
        return R.layout.dialog_versionupdate
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
        params.width = (mContext.getScreenWidth() * 0.8).toInt()//宽度为屏幕宽度的80%
        params.height = (mContext.getScreenWidth() * 0.8).toInt()//高度为屏幕宽度的80%
        return params
    }

    override fun initView() {
        super.initView()
        mTvVersionUpdateContent = mView!!.findViewById(R.id.tv_versionUpdateContent)
        mTvVersionUpdateCancel = mView!!.findViewById(R.id.tv_versionUpdateCancel)
        mTvVersionUpdateSubmit = mView!!.findViewById(R.id.tv_versionUpdateSubmit)
        mTvVersionUpdateCancel!!.setOnClickListener(this)
        mTvVersionUpdateSubmit!!.setOnClickListener(this)
    }

    override fun initData() {
        val bundle = arguments
        mVersionUpdateVO = bundle?.getParcelable(VersionUpdateVO::class.java.simpleName)
        if (mVersionUpdateVO != null) {
            if (mVersionUpdateVO?.forceUpdate == 0)
                mTvVersionUpdateCancel!!.visibility = View.VISIBLE
            else {
                mTvVersionUpdateCancel!!.visibility = View.GONE
                setCanceledOnBackPress()
            }
            mTvVersionUpdateContent!!.text = mVersionUpdateVO!!.content
        }
    }

    fun show(manager: FragmentManager, tag: String, listener: View.OnClickListener) {
        mOnClickListener = listener
        super.show(manager, tag)
    }

    override fun onClick(view: View) {
        val viewId = view.id
        if (viewId == R.id.tv_versionUpdateCancel) {
            this.dismiss()
        } else if (viewId == R.id.tv_versionUpdateSubmit) {
            this.dismiss()
            if (mVersionUpdateVO != null) {
                val intent = Intent(mContext, VersionUpdateService::class.java)
                intent.putExtra(VersionUpdateVO::class.java.simpleName, mVersionUpdateVO)
                mActivity.startService(intent)
            }
            if (mOnClickListener != null)
                mOnClickListener!!.onClick(view)
        }
    }
}