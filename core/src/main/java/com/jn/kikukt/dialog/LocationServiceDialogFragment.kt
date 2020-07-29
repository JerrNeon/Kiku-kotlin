package com.jn.kikukt.dialog

import android.content.Intent
import android.os.Build
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import androidx.fragment.app.FragmentManager
import com.jn.kikukt.R
import com.jn.kikukt.common.utils.checkLocationServiceOPen
import com.jn.kikukt.common.utils.getScreenWidth
import com.jn.kikukt.common.utils.openLocationService
import kotlinx.android.synthetic.main.dialog_locationservice.view.*

/**
 * Author：Stevie.Chen Time：2019/7/15
 * Class Comment：定位服务对话框
 */
class LocationServiceDialogFragment : RootDialogFragment() {

    private var onLocationServiceOpenSuccess: (() -> Unit)? = null
    private var onLocationServiceOpenFailure: (() -> Unit)? = null

    companion object {
        private const val REQUESTCODE_LOCATIONSERVICE = 1

        fun newInstance(): LocationServiceDialogFragment = LocationServiceDialogFragment()
    }

    override val layoutResourceId: Int = R.layout.dialog_locationservice

    override val isCanceledOnTouchOutsideEnable: Boolean = true

    override val layoutParams: WindowManager.LayoutParams? = mWindow?.attributes?.apply {
        gravity = Gravity.CENTER//中间显示
        width =
            (mContext.getScreenWidth() * 0.9).toInt()//宽度为屏幕90%
    }

    override fun initView() {
        mView?.run {
            tv_permissionCancel.setOnClickListener(this@LocationServiceDialogFragment)
            tv_permissionSubmit.setOnClickListener(this@LocationServiceDialogFragment)
        }
    }

    fun show(
        manager: FragmentManager,
        tag: String,
        onLocationServiceOpenSuccess: () -> Unit,
        onLocationServiceOpenFailure: () -> Unit
    ) {
        super.show(manager, tag)
        this.onLocationServiceOpenSuccess = onLocationServiceOpenSuccess
        this.onLocationServiceOpenFailure = onLocationServiceOpenFailure
    }

    override fun onClick(view: View) {
        if (view.id == R.id.tv_permissionCancel) {
            onLocationServiceOpenFailure?.invoke()
            this.dismiss()
        } else if (view.id == R.id.tv_permissionSubmit) {
            mFragment.openLocationService(REQUESTCODE_LOCATIONSERVICE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //回调再次判断是否开启定位服务
        if (requestCode == REQUESTCODE_LOCATIONSERVICE && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (mContext.checkLocationServiceOPen()) {
                onLocationServiceOpenSuccess?.invoke()
            } else {
                onLocationServiceOpenFailure?.invoke()
            }
            this.dismiss()
        }
    }
}