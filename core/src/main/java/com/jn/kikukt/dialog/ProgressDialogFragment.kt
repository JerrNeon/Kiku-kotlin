package com.jn.kikukt.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.jn.kikukt.R
import com.jn.kikukt.common.leak.WeakHandler
import com.jn.kikukt.widget.imageview.SpinBlackView

/**
 * Author：Stevie.Chen Time：2019/7/15
 * Class Comment：加载对话框
 */
class ProgressDialogFragment : DialogFragment() {

    private var spinBlackProgressView: SpinBlackView? = null//图标资源View
    private var startedShowing: Boolean = false
    private var mStartMillisecond: Long = 0
    private var mStopMillisecond: Long = 0

    companion object {
        private const val DELAY_MILLISECOND = 450
        private const val SHOW_MIN_MILLISECOND = 300
        private const val KEY_TYPE = "type"
        const val TYPE_BLACK = 0x01
        const val TYPE_WHITE = 0x02

        fun newInstance(type: Int = TYPE_BLACK): ProgressDialogFragment {
            return ProgressDialogFragment().apply {
                val bundle = Bundle()
                bundle.putInt(KEY_TYPE, type)
                arguments = bundle
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val type = arguments?.getInt(KEY_TYPE) ?: TYPE_BLACK
        val view = LayoutInflater.from(context).inflate(
            if (type == TYPE_BLACK) R.layout.dialog_progress else R.layout.dialog_progress_white,
            container,
            false
        )
        spinBlackProgressView = view.findViewById(R.id.sbv_progressDialog)
        return view
    }

    override fun onStart() {
        super.onStart()
        dialog?.let {
            val window = it.window
            if (window != null) {
                val lp = window.attributes
                lp.width = WindowManager.LayoutParams.WRAP_CONTENT
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT
                //去掉背景色
                lp.dimAmount = 0f
                lp.flags = lp.flags or WindowManager.LayoutParams.FLAG_DIM_BEHIND
                window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                window.attributes = lp
                it.setCancelable(true)//点击外部区域隐藏对话框
            }
        }
    }

    override fun show(fm: FragmentManager, tag: String?) {
        mStartMillisecond = System.currentTimeMillis()
        startedShowing = false
        mStopMillisecond = java.lang.Long.MAX_VALUE

        val handler = WeakHandler(this)
        handler.postDelayed({
            if (mStopMillisecond > System.currentTimeMillis())
                showDialogAfterDelay(fm, tag)
        }, DELAY_MILLISECOND.toLong())
    }

    private fun showDialogAfterDelay(fm: FragmentManager, tag: String?) {
        startedShowing = true
        val ft = fm.beginTransaction()
        ft.add(this, tag)
        ft.commitAllowingStateLoss()
    }

    fun cancel() {
        mStopMillisecond = System.currentTimeMillis()

        if (startedShowing) {
            if (spinBlackProgressView != null) {
                cancelWhenShowing()
            } else {
                cancelWhenNotShowing()
            }
        }
    }

    private fun cancelWhenShowing() {
        if (mStopMillisecond < mStartMillisecond + DELAY_MILLISECOND.toLong() + SHOW_MIN_MILLISECOND.toLong()) {
            val handler = WeakHandler(this)
            handler.postDelayed({ dismissAllowingStateLoss() }, SHOW_MIN_MILLISECOND.toLong())
        } else {
            dismissAllowingStateLoss()
        }
    }

    private fun cancelWhenNotShowing() {
        val handler = WeakHandler(this)
        handler.postDelayed({ dismissAllowingStateLoss() }, DELAY_MILLISECOND.toLong())
    }
}