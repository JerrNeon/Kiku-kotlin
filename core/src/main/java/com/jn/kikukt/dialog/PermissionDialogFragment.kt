package com.jn.kikukt.dialog

import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import com.jn.kikukt.R
import com.jn.kikukt.common.utils.getScreenWidth
import requestApplicationSettings

/**
 * Author：Stevie.Chen Time：2019/7/15
 * Class Comment：
 */
class PermissionDialogFragment : RootDialogFragment(), View.OnClickListener {

    companion object {
        private const val APP_NAME = "appName"
        private const val PERMISSION_NAME = "permissionName"

        fun newInstance(appName: String, permissionName: String?): PermissionDialogFragment {
            val fragment = PermissionDialogFragment()
            val bundle = Bundle()
            bundle.putString(APP_NAME, appName)
            bundle.putString(PERMISSION_NAME, permissionName)
            fragment.arguments = bundle
            return fragment
        }
    }

    override val layoutResId: Int = R.layout.dialog_permission

    override val isCanceledOnTouchOutsideEnable: Boolean = true

    override val layoutParams: WindowManager.LayoutParams?
        get() = mWindow?.attributes?.apply {
            gravity = Gravity.CENTER//中间显示
            val screenWidth = context?.getScreenWidth()?.toFloat() ?: 0f
            width = (screenWidth * 0.9f).toInt()//宽度为屏幕90%
        }

    private var applicationBlock: (() -> Unit)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        applicationBlock = requestApplicationSettings {
        }
    }

    override fun initView(view: View) {
        view.findViewById<TextView>(R.id.tv_permissionCancel)
            .setOnClickListener(this@PermissionDialogFragment)
        view.findViewById<TextView>(R.id.tv_permissionSubmit)
            .setOnClickListener(this@PermissionDialogFragment)
    }

    override fun initData() {
        val bundle = arguments
        val appName = bundle?.getString(APP_NAME, "")
        val permissionName = bundle?.getString(PERMISSION_NAME, "")
        view?.findViewById<TextView>(R.id.tv_permissionMessage)?.text =
            String.format(
                resources.getString(R.string.permission_content),
                appName,
                permissionName,
                appName
            )
    }

    override fun onClick(view: View) {
        if (view.id == R.id.tv_permissionCancel) {
            this.dismiss()
        } else if (view.id == R.id.tv_permissionSubmit) {
            this.dismiss()
            applicationBlock?.invoke()
        }
    }
}