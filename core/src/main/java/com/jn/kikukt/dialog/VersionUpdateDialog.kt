package com.jn.kikukt.dialog

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import com.jn.kikukt.R
import com.jn.kikukt.common.utils.getScreenWidth
import com.jn.kikukt.entiy.VersionUpdateVO
import com.jn.kikukt.service.VersionUpdateService

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

    override val layoutResourceId: Int = R.layout.dialog_versionupdate

    override val layoutParams: WindowManager.LayoutParams? = mWindow?.attributes?.apply {
        gravity = Gravity.CENTER//居中显示
        width = (mContext.getScreenWidth() * 0.8).toInt()//宽度为屏幕宽度的80%
        height = (mContext.getScreenWidth() * 0.8).toInt()//高度为屏幕宽度的80%
    }

    override fun initView() {
        super.initView()
        mView?.run {
            mTvVersionUpdateContent = findViewById(R.id.tv_versionUpdateContent)
            mTvVersionUpdateCancel = findViewById(R.id.tv_versionUpdateCancel)
            mTvVersionUpdateSubmit = findViewById(R.id.tv_versionUpdateSubmit)
            mTvVersionUpdateCancel?.setOnClickListener(this@VersionUpdateDialog)
            mTvVersionUpdateSubmit?.setOnClickListener(this@VersionUpdateDialog)
        }
    }

    override fun initData() {
        val bundle = arguments
        mVersionUpdateVO = bundle?.getParcelable(VersionUpdateVO::class.java.simpleName)
        mVersionUpdateVO?.run {
            if (forceUpdate == 0)
                mTvVersionUpdateCancel?.visibility = View.VISIBLE
            else {
                mTvVersionUpdateCancel?.visibility = View.GONE
                setCanceledOnBackPress()
            }
            mTvVersionUpdateContent?.text = content
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
            mVersionUpdateVO?.let {
                val intent = Intent(mContext, VersionUpdateService::class.java)
                intent.putExtra(VersionUpdateVO::class.java.simpleName, it)
                mActivity.startService(intent)
            }
            mOnClickListener?.onClick(view)
        }
    }
}