package com.jn.kikukt.dialog

import android.app.Activity
import android.content.Context
import android.view.*
import android.widget.PopupWindow
import androidx.fragment.app.Fragment
import com.jn.kikukt.common.utils.getScreenWidth

/**
 * Author：Stevie.Chen Time：2019/7/15
 * Class Comment：
 */
abstract class RootPopupWindow {

    protected var mActivity: Activity? = null
    protected var mFragment: Fragment? = null
    protected var mContext: Context? = null
    protected var mView: View? = null
    protected var mPopupWindow: PopupWindow? = null
    protected var mWindow: Window? = null

    open val width: Int = 0//popupWindow width
    open val height: Int = 0//popupWindow height
    open val animationStyle: Int = 0//动画

    abstract val layoutResId: Int//LayoutResId

    constructor(activity: Activity) {
        this.mActivity = activity
        this.mContext = activity.applicationContext
        initView()
    }

    constructor(fragment: Fragment) {
        this.mFragment = fragment
        this.mActivity = fragment.activity
        this.mContext = fragment.context
        initView()
    }

    protected fun initView() {
        mView = LayoutInflater.from(mContext).inflate(layoutResId, null, false)
        mPopupWindow = PopupWindow(mView).apply {
            width = if (this@RootPopupWindow.width == 0)
                mContext?.getScreenWidth() ?: ViewGroup.LayoutParams.WRAP_CONTENT
            else
                this@RootPopupWindow.width
            height = if (this@RootPopupWindow.height == 0)
                ViewGroup.LayoutParams.WRAP_CONTENT
            else
                this@RootPopupWindow.height
            isFocusable = true//响应返回键(点击返回键消失)
            isOutsideTouchable = true//点击边际是否可消失(true可消失)
            if (this@RootPopupWindow.animationStyle != 0)
                animationStyle = this@RootPopupWindow.animationStyle
        }
    }

    open fun setWindowAttributes() {
        mWindow = mActivity?.window?.apply {
            val lp = attributes
            lp.alpha = 0.5f
            addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
            attributes = lp
        }
    }

    open fun initData() {}

    open fun setListener() {}

    val isShowing
        get() = mPopupWindow?.isShowing ?: false

    fun dismiss() {
        mPopupWindow?.dismiss()
    }

    fun setOnDismissListener(onDismissListener: PopupWindow.OnDismissListener) {
        mPopupWindow?.setOnDismissListener(onDismissListener)
    }

    open fun showAsDropDown(anchor: View) {
        mPopupWindow?.showAsDropDown(anchor)
    }

    open fun showAsDropDown(anchor: View, xoff: Int, yoff: Int) {
        mPopupWindow?.showAsDropDown(anchor, xoff, yoff)
    }

    open fun showAsDropDown(anchor: View, xoff: Int, yoff: Int, gravity: Int) {
        mPopupWindow?.showAsDropDown(anchor, xoff, yoff, gravity)
    }

    open fun showAtLocation(anchor: View, gravity: Int, x: Int, y: Int) {
        mPopupWindow?.showAtLocation(anchor, gravity, x, y)
    }
}