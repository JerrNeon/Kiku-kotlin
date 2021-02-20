package com.jn.kikukt.dialog

import android.text.style.ClickableSpan
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.jn.kikukt.R
import com.jn.kikukt.common.utils.SpanUtils
import kotlin.system.exitProcess

/**
 * Author：Stevie.Chen Time：2020/9/23
 * Class Comment：用户协议和隐私政策提示框
 */
class ProtocolDialogFragment : RootDialogFragment() {

    override val layoutResId: Int
        get() = R.layout.dialog_protocol
    override val isCanceledOnTouchOutsideEnable: Boolean
        get() = true
    override val animationStyle: Int
        get() = R.style.center_in_out
    override val layoutParams: WindowManager.LayoutParams?
        get() = dialog?.window?.attributes?.apply {
            gravity = Gravity.CENTER
            width = WindowManager.LayoutParams.WRAP_CONTENT
            height = WindowManager.LayoutParams.WRAP_CONTENT
        }

    private var mTimeExit: Long = 0 //Back press time
    var onUserProtocolBlock: (() -> Unit)? = null//用户协议
    var onPrivacyPolicyBlock: (() -> Unit)? = null//隐私政策
    var onPositiveBlock: (() -> Unit)? = null

    companion object {
        private const val TIME_INTERVAL: Long = 2000 //two times Interval

        fun newInstance() = ProtocolDialogFragment()
    }

    override fun initView(view: View) {
        super.initView()
        setCanceledOnBackPress()

        context?.let { context ->
            val tvContent = view.findViewById<TextView>(R.id.tv_protocolContent)
            val color = ContextCompat.getColor(context, R.color.c_333333)
            tvContent.setLinkTextColor(ContextCompat.getColor(context, R.color.c_FA415E))
            SpanUtils.with(tvContent)?.apply {
                append(getString(R.string.dialog_protocolContent1))
                setFontSize(14, true)
                setForegroundColor(color)
                append(getString(R.string.app_name))
                setFontSize(14, true)
                setForegroundColor(color)
                appendLine(getString(R.string.dialog_protocolContent2))
                setFontSize(14, true)
                setForegroundColor(color)
                append(getString(R.string.dialog_protocolContent3))
                setFontSize(14, true)
                setForegroundColor(color)
                append(getString(R.string.dialog_protocolContent4))
                setFontSize(14, true)
                setForegroundColor(color)
                setBold()
                setUnderline()
                append(getString(R.string.dialog_protocolContent5))
                setFontSize(14, true)
                setForegroundColor(ContextCompat.getColor(context, R.color.c_FA415E))
                setBold()
                setUnderline()
                setClickSpan(object : ClickableSpan() {
                    override fun onClick(widget: View) {
                        //用户协议
                        onUserProtocolBlock?.invoke()
                    }
                })
                append(getString(R.string.dialog_protocolContent6))
                setFontSize(14, true)
                setForegroundColor(color)
                setBold()
                setUnderline()
                append(getString(R.string.dialog_protocolContent7))
                setFontSize(14, true)
                setForegroundColor(ContextCompat.getColor(context, R.color.c_FA415E))
                setBold()
                setUnderline()
                setClickSpan(object : ClickableSpan() {
                    override fun onClick(widget: View) {
                        //隐私政策
                        onPrivacyPolicyBlock?.invoke()
                    }
                })
                appendLine(getString(R.string.dialog_protocolContent8))
                setFontSize(14, true)
                setForegroundColor(color)
                setBold()
                setUnderline()
                append(getString(R.string.dialog_protocolContent9))
                setFontSize(14, true)
                setForegroundColor(color)
                create()
            }
        }

        view.findViewById<TextView>(R.id.tv_protocolNegative)?.setOnClickListener {
            if (System.currentTimeMillis() - mTimeExit > TIME_INTERVAL) {
                Toast.makeText(
                    context,
                    resources.getString(R.string.app_exitNoticeMessage),
                    Toast.LENGTH_SHORT
                ).show()
                mTimeExit = System.currentTimeMillis()
            } else {
                dismissAllowingStateLoss()
                exitProcess(0)
            }
        }
        view.findViewById<TextView>(R.id.tv_protocolPositive)
            ?.setOnClickListener {
                dismissAllowingStateLoss()
                onPositiveBlock?.invoke()
            }
    }

}