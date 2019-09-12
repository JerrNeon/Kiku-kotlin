package com.jn.kikukt.dialog

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.annotation.CallSuper
import android.support.annotation.LayoutRes
import android.support.design.widget.BottomSheetDialogFragment
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v7.app.AppCompatActivity
import android.view.*
import com.jn.kikukt.common.api.IBaseView
import com.jn.kikukt.utils.BaseManager
import com.tbruyelle.rxpermissions2.RxPermissions

/**
 * Author：Stevie.Chen Time：2019/7/15
 * Class Comment：
 */
abstract class RootBottomSheetDialogFragment : BottomSheetDialogFragment(),
    DialogInterface.OnKeyListener, IBaseView, View.OnClickListener {

    override lateinit var mActivity: Activity
    override lateinit var mAppCompatActivity: AppCompatActivity
    override lateinit var mContext: Context
    override var mRxPermissions: RxPermissions? = null
    override var mProgressDialog: ProgressDialogFragment? = null
    override var mBaseManager: BaseManager? = null
    protected lateinit var mFragment: Fragment
    protected var mWindow: Window? = null
    protected var mView: View? = null

    @CallSuper
    override fun onStart() {
        super.onStart()
        mWindow = dialog.window
        if (getLayoutParams() == null) {
            val params = mWindow?.attributes
            params?.gravity = Gravity.BOTTOM//底部显示
            params?.width = WindowManager.LayoutParams.MATCH_PARENT//宽度为全屏
            params?.height = WindowManager.LayoutParams.WRAP_CONTENT//宽度为全屏
            mWindow?.attributes = params
        } else
            mWindow?.attributes = getLayoutParams()
        mWindow?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))//设置半透明背景
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)//去掉默认标题
        dialog.setCanceledOnTouchOutside(!getCanceledOnTouchOutsideEnable())//点击边际是否可消失
        if (getAnimationStyle() != 0)
            dialog.window!!.attributes.windowAnimations = getAnimationStyle()
        mView = inflater.inflate(getLayoutResourceId(), container, false)
        mActivity = activity as Activity
        if (activity is AppCompatActivity)
            mAppCompatActivity = activity as AppCompatActivity
        mContext = activity!!.applicationContext
        mFragment = this
        mBaseManager = BaseManager(this)
        lifecycle.addObserver(mBaseManager!!)
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

    override fun show(manager: FragmentManager, tag: String) {
        try {
            super.show(manager, tag)
        } catch (e: Exception) {
            //
        }

    }

    /**
     * 布局资源
     *
     * @return
     */
    @LayoutRes
    protected abstract fun getLayoutResourceId(): Int

    /**
     * 动画
     *
     * @return
     */
    protected abstract fun getAnimationStyle(): Int

    /**
     * 点击边际是否可消失
     *
     * @return false可消失
     */
    open fun getCanceledOnTouchOutsideEnable(): Boolean = false

    /**
     * 对话框布局参数
     *
     * @return
     */
    protected abstract fun getLayoutParams(): WindowManager.LayoutParams?

    /**
     * 点击物理按键让对话框不消失
     */
    fun setCanceledOnBackPress() {
        dialog.setOnKeyListener(this)
    }

    /**
     * 对话框是否正在显示
     *
     * @return
     */
    fun isShowing(): Boolean {
        return if (dialog != null) dialog.isShowing else false
    }

    override fun onClick(view: View) {

    }
}