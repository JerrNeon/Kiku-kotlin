package com.jn.kikukt.dialog

import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.jn.kikukt.common.api.IBaseView

/**
 * Author：Stevie.Chen Time：2019/7/15
 * Class Comment：
 */
abstract class RootBottomSheetDialogFragment : BottomSheetDialogFragment(),
    DialogInterface.OnKeyListener, IBaseView, View.OnClickListener {

    override lateinit var mAppCompatActivity: AppCompatActivity
    override lateinit var mContext: Context
    override var mProgressDialog: ProgressDialogFragment? = null
    protected lateinit var mFragment: Fragment
    protected var mWindow: Window? = null
    protected var mView: View? = null

    @CallSuper
    override fun onStart() {
        super.onStart()
        dialog?.let {
            mWindow = it.window
            if (layoutParams == null) {
                val params = mWindow?.attributes
                params?.gravity = Gravity.BOTTOM//底部显示
                params?.width = WindowManager.LayoutParams.MATCH_PARENT//宽度为全屏
                params?.height = WindowManager.LayoutParams.WRAP_CONTENT//宽度为全屏
                mWindow?.attributes = params
            } else
                mWindow?.attributes = layoutParams
            mWindow?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))//设置半透明背景
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        dialog?.run {
            requestWindowFeature(Window.FEATURE_NO_TITLE)//去掉默认标题
            setCanceledOnTouchOutside(!isCanceledOnTouchOutsideEnable)//点击边际是否可消失
            if (animationStyle != 0)
                window?.attributes?.windowAnimations = animationStyle
        }
        mView = inflater.inflate(layoutResourceId, container, false)
        if (activity is AppCompatActivity)
            mAppCompatActivity = activity as AppCompatActivity
        mFragment = this
        initView()
        initData()
        return mView
    }

    override fun onKey(
        dialogInterface: DialogInterface,
        keyCode: Int,
        keyEvent: KeyEvent
    ): Boolean {
        return keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_SEARCH//不执行父类点击事件
    }

    override fun show(manager: FragmentManager, tag: String?) {
        try {
            super.show(manager, tag)
        } catch (e: Exception) {
            //
        }

    }

    abstract val layoutResourceId: Int//布局资源
    //动画
    open val animationStyle: Int
        get() = 0
    open val isCanceledOnTouchOutsideEnable: Boolean = false//点击边际是否可消失(false可消失)
    abstract val layoutParams: WindowManager.LayoutParams?//对话框布局参数

    //对话框是否正在显示
    val isShowing: Boolean
        get() {
            return if (dialog != null) dialog?.isShowing ?: false else false
        }

    /**
     * 点击物理按键让对话框不消失
     */
    fun setCanceledOnBackPress() {
        dialog?.setOnKeyListener(this)
    }

    override fun onClick(view: View) {

    }
}