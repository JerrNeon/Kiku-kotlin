package com.jn.kikukt.dialog

import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import com.jn.kikukt.R
import com.jn.kikukt.common.utils.getScreenWidth
import isLocationServiceEnable
import requestLocationService

/**
 * Author：Stevie.Chen Time：2019/7/15
 * Class Comment：定位服务对话框
 */
class LocationServiceDialogFragment : RootDialogFragment(), View.OnClickListener {

    var onLocationServiceOpenSuccess: (() -> Unit)? = null
    var onLocationServiceOpenFailure: (() -> Unit)? = null

    companion object {
        fun newInstance(): LocationServiceDialogFragment = LocationServiceDialogFragment()
    }

    override val layoutResId: Int = R.layout.dialog_locationservice

    override val isCanceledOnTouchOutsideEnable: Boolean = true

    override val layoutParams: WindowManager.LayoutParams?
        get() = mWindow?.attributes?.apply {
            gravity = Gravity.CENTER//中间显示
            val screenWidth = context?.getScreenWidth()?.toFloat() ?: 0f
            width = (screenWidth * 0.9f).toInt()//宽度为屏幕90%
        }

    private var locationServiceBlock: (() -> Unit)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        locationServiceBlock = requestLocationService {
            if (context?.isLocationServiceEnable() == true) {
                onLocationServiceOpenSuccess?.invoke()
            } else {
                onLocationServiceOpenFailure?.invoke()
            }
        }
    }

    override fun initView(view: View) {
        view.findViewById<TextView>(R.id.tv_permissionCancel)
            .setOnClickListener(this@LocationServiceDialogFragment)
        view.findViewById<TextView>(R.id.tv_permissionSubmit)
            .setOnClickListener(this@LocationServiceDialogFragment)
    }

    override fun onClick(view: View) {
        if (view.id == R.id.tv_permissionCancel) {
            onLocationServiceOpenFailure?.invoke()
            this.dismiss()
        } else if (view.id == R.id.tv_permissionSubmit) {
            locationServiceBlock?.invoke()
            this.dismiss()
        }
    }
}