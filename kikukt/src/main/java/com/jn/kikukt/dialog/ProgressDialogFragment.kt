package com.jn.kikukt.dialog

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.support.annotation.DrawableRes
import android.support.v4.app.DialogFragment
import android.support.v4.app.FragmentManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.jn.kikukt.R
import com.jn.kikukt.widget.imageview.SpinBlackView

/**
 * Author：Stevie.Chen Time：2019/7/15
 * Class Comment：加载对话框
 */
class ProgressDialogFragment : DialogFragment() {

    private var mSpinBlackProgressView: SpinBlackView? = null
    private var startedShowing: Boolean = false
    private var mStartMillisecond: Long = 0
    private var mStopMillisecond: Long = 0

    companion object {
        private const val DELAY_MILLISECOND = 450
        private const val SHOW_MIN_MILLISECOND = 300

        fun newInstance(): ProgressDialogFragment = ProgressDialogFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_progress, null, false)
        mSpinBlackProgressView = view.findViewById(R.id.sbv_progressDialog)
        return view
    }

    @SuppressLint("ResourceType")
    override fun onStart() {
        super.onStart()
        if (dialog.window != null) {
            val window = dialog.window
            val lp = window!!.attributes
            lp.width = WindowManager.LayoutParams.WRAP_CONTENT
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT
            //去掉背景色
            lp.dimAmount = 0f
            lp.flags = lp.flags or WindowManager.LayoutParams.FLAG_DIM_BEHIND
            window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            window.attributes = lp
            dialog.setCancelable(true)//点击外部区域隐藏对话框
        }
    }

    override fun show(fm: FragmentManager, tag: String) {
        mStartMillisecond = System.currentTimeMillis()
        startedShowing = false
        mStopMillisecond = java.lang.Long.MAX_VALUE

        val handler = Handler()
        handler.postDelayed({
            if (mStopMillisecond > System.currentTimeMillis())
                showDialogAfterDelay(fm, tag)
        }, DELAY_MILLISECOND.toLong())
    }

    private fun showDialogAfterDelay(fm: FragmentManager, tag: String) {
        startedShowing = true
        val ft = fm.beginTransaction()
        ft.add(this, tag)
        ft.commitAllowingStateLoss()
    }

    fun cancel() {
        mStopMillisecond = System.currentTimeMillis()

        if (startedShowing) {
            if (mSpinBlackProgressView != null) {
                cancelWhenShowing()
            } else {
                cancelWhenNotShowing()
            }
        }
    }

    private fun cancelWhenShowing() {
        if (mStopMillisecond < mStartMillisecond + DELAY_MILLISECOND.toLong() + SHOW_MIN_MILLISECOND.toLong()) {
            val handler = Handler()
            handler.postDelayed({ dismissAllowingStateLoss() }, SHOW_MIN_MILLISECOND.toLong())
        } else {
            dismissAllowingStateLoss()
        }
    }

    private fun cancelWhenNotShowing() {
        val handler = Handler()
        handler.postDelayed({ dismissAllowingStateLoss() }, DELAY_MILLISECOND.toLong())
    }

    /**
     * 设置加载图标
     *
     * @param resId
     */
    fun setProgressImageResource(@DrawableRes resId: Int): ProgressDialogFragment {
        if (mSpinBlackProgressView != null)
            mSpinBlackProgressView!!.setImageResource(resId)
        return this
    }
}