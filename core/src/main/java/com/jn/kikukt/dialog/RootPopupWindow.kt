package com.jn.kikukt.dialog

import android.app.Activity
import android.content.Context
import android.support.annotation.LayoutRes
import android.support.v4.app.Fragment
import android.view.*
import android.widget.PopupWindow
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

    /**
     * LayoutResourceId
     *
     * @return
     */
    @LayoutRes
    protected abstract fun getLayoutResourceId(): Int

    /**
     * get popupWindow width
     *
     * @return
     */
    protected abstract fun getWidth(): Int

    /**
     * get popupWindow height
     *
     * @return
     */
    protected abstract fun getHeight(): Int

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
        this.mContext = fragment.activity!!.applicationContext
        initView()
        setWindowAttributes()
        initData()
        setListener()
    }

    protected fun initView() {
        mView = LayoutInflater.from(mContext).inflate(getLayoutResourceId(), null, false)
        mPopupWindow = PopupWindow(mView)
        if (getWidth() == 0)
            mPopupWindow!!.width = mContext?.getScreenWidth() ?: ViewGroup.LayoutParams.WRAP_CONTENT
        else
            mPopupWindow!!.width = getWidth()
        if (getHeight() == 0)
            mPopupWindow!!.height = ViewGroup.LayoutParams.WRAP_CONTENT
        else
            mPopupWindow!!.height = getHeight()
        mPopupWindow!!.isOutsideTouchable = false
    }

    protected fun setWindowAttributes() {
        mWindow = mActivity!!.window
        val lp = mWindow!!.attributes
        lp.alpha = 0.5f
        mWindow!!.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        mWindow!!.attributes = lp
    }

    protected fun initData() {

    }

    protected fun setListener() {

    }

    override fun onClick(view: View) {

    }

}