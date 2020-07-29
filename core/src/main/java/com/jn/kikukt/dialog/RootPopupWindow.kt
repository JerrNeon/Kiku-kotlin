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
abstract class RootPopupWindow : View.OnClickListener {

    protected var mActivity: Activity? = null
    protected var mFragment: Fragment? = null
    protected var mContext: Context? = null
    protected var mView: View? = null
    protected var mPopupWindow: PopupWindow? = null
    protected var mWindow: Window? = null

    abstract val layoutResourceId: Int//LayoutResourceId
    abstract val width: Int//popupWindow width
    abstract val height: Int//popupWindow height

    constructor(activity: Activity) {
        this.mActivity = activity
        this.mContext = activity.applicationContext
        initView()
        setWindowAttributes()
        initData()
        setListener()
    }

    constructor(fragment: Fragment) {
        this.mFragment = fragment
        this.mActivity = fragment.activity
        this.mContext = fragment.requireActivity().applicationContext
        initView()
        setWindowAttributes()
        initData()
        setListener()
    }

    protected fun initView() {
        mView = LayoutInflater.from(mContext).inflate(layoutResourceId, null, false)
        mPopupWindow = PopupWindow(mView).apply {
            width = if (width == 0)
                mContext?.getScreenWidth() ?: ViewGroup.LayoutParams.WRAP_CONTENT
            else
                width
            height = if (height == 0)
                ViewGroup.LayoutParams.WRAP_CONTENT
            else
                height
            isOutsideTouchable = false
        }
    }

    open fun setWindowAttributes() {
        mWindow = mActivity!!.window
        mWindow?.run {
            val lp = attributes
            lp.alpha = 0.5f
            addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
            attributes = lp
        }
    }

    open fun initData() {

    }

    open fun setListener() {

    }

    override fun onClick(view: View) {

    }

}