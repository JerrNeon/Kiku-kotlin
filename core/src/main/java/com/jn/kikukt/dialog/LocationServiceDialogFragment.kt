package com.jn.kikukt.dialog

import android.content.Intent
import android.os.Build
import androidx.fragment.app.FragmentManager
import android.view.Gravity
import android.view.View
import android.view.WindowManager
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

    private var mLocationServiceListener: ILocationServiceListener? = null

    companion object {
        private const val REQUESTCODE_LOCATIONSERVICE = 1

        fun newInstance(): LocationServiceDialogFragment = LocationServiceDialogFragment()
    }

    override fun getLayoutResourceId(): Int {
        return R.layout.dialog_locationservice
    }

    override fun getAnimationStyle(): Int {
        return 0
    }

    override fun getCanceledOnTouchOutsideEnable(): Boolean {
        return true
    }

    override fun getLayoutParams(): WindowManager.LayoutParams? {
        val params = mWindow!!.attributes
        params.gravity = Gravity.CENTER//中间显示
        params.width =
            (mContext.getScreenWidth() * 0.9).toInt()//宽度为屏幕90%
        return params
    }

    override fun initView() {
        mView!!.tv_permissionCancel.setOnClickListener(this)
        mView!!.tv_permissionSubmit.setOnClickListener(this)
    }

    fun show(manager: FragmentManager, tag: String, listener: ILocationServiceListener) {
        super.show(manager, tag)
        mLocationServiceListener = listener
    }

    override fun onClick(view: View) {
        if (view.id == R.id.tv_permissionCancel) {
            if (mLocationServiceListener != null)
                mLocationServiceListener!!.onLocationServiceOpenFailure()
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
                if (mLocationServiceListener != null)
                    mLocationServiceListener!!.onLocationServiceOpenSuccess()
            } else {
                if (mLocationServiceListener != null)
                    mLocationServiceListener!!.onLocationServiceOpenFailure()
            }
            this.dismiss()
        }
    }

    /**
     * 定位服务监听器
     */
    interface ILocationServiceListener {
        fun onLocationServiceOpenSuccess()

        fun onLocationServiceOpenFailure()
    }
}