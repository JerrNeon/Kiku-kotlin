package com.jn.kikukt.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.jn.kikukt.R
import com.jn.kikukt.dialog.LocationServiceDialogFragment
import com.jn.kikukt.dialog.PermissionDialogFragment
import isLocationServiceEnable
import requestMultiplePermissions

/**
 * Author：Stevie.Chen Time：2019/7/11
 * Class Comment：Permissions权限管理
 */
object PermissionsManager {

    private val CAMERA_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)//相机权限
    private val WRITE_EXTERNAL_STORAGE_PERMISSIONS =
        arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)//存储空间(相册)权限
    private val CALL_PHONES_PERMISSIONS =
        arrayOf(Manifest.permission.CALL_PHONE, Manifest.permission.READ_PHONE_STATE)//电话权限
    private val LOCATIONS_PERMISSIONS =
        arrayOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        )//定位权限
    private val CAMERA_WRITE_EXTERNAL_STORAGE_PERMISSIONS =
        arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)//拍照&存储空间权限

    enum class PermissionType {
        PERMISSION_CAMERA,//相机
        PERMISSION_WRITE_EXTERNAL_STORAGE,//存储空间(打开相册)
        PERMISSION_CALL_PHONE,//电话
        PERMISSION_LOCATION,//定位
        PERMISSION_CAMERA_WRITE_EXTERNAL_STORAGE,//相机&存储空间(打开相册)
    }

    private fun getPermissions(permissionType: PermissionType) = when (permissionType) {
        PermissionType.PERMISSION_CAMERA -> {
            CAMERA_PERMISSIONS
        }
        PermissionType.PERMISSION_WRITE_EXTERNAL_STORAGE -> {
            WRITE_EXTERNAL_STORAGE_PERMISSIONS
        }
        PermissionType.PERMISSION_CALL_PHONE -> {
            CALL_PHONES_PERMISSIONS
        }
        PermissionType.PERMISSION_LOCATION -> {
            LOCATIONS_PERMISSIONS
        }
        PermissionType.PERMISSION_CAMERA_WRITE_EXTERNAL_STORAGE -> {
            CAMERA_WRITE_EXTERNAL_STORAGE_PERMISSIONS
        }
    }

    private fun getPermissionName(permissionType: PermissionType, context: Context) =
        when (permissionType) {
            PermissionType.PERMISSION_CAMERA -> {
                context.getString(R.string.permission_camera)
            }
            PermissionType.PERMISSION_WRITE_EXTERNAL_STORAGE -> {
                context.getString(R.string.permission_storage)
            }
            PermissionType.PERMISSION_CALL_PHONE -> {
                context.getString(R.string.permission_phone)
            }
            PermissionType.PERMISSION_LOCATION -> {
                context.getString(R.string.permission_location)
            }
            PermissionType.PERMISSION_CAMERA_WRITE_EXTERNAL_STORAGE -> {
                context.getString(R.string.permission_camera_storage)
            }
        }

    fun requestPermission(
        activity: AppCompatActivity,
        permissionType: PermissionType,
        result: ((granted: Boolean) -> Unit)? = null
    ): () -> Unit {
        val permissions = getPermissions(permissionType)
        val permissionName = getPermissionName(permissionType, activity.applicationContext)
        val context = activity.applicationContext
        return activity.requestMultiplePermissions(permissions) {
            when (it) {
                0 -> {//权限申请成功
                    if (permissionType == PermissionType.PERMISSION_LOCATION) {//所有权限都同意
                        if (context.isLocationServiceEnable()) {//6.0以上GPS开启成功
                            result?.invoke(true)//权限申请成功
                        } else {
                            showLocationServiceDialog(activity, result)
                        }
                    } else {
                        result?.invoke(true)//权限申请成功
                    }
                }
                1 -> {//至少拒绝了一个权限并没有勾选不再提示
                    result?.invoke(false)
                }
                2 -> {//至少拒绝了一个权限并勾选了不再提示
                    result?.invoke(false)
                    showPermissionDialog(
                        activity,
                        context.getString(R.string.app_name),
                        permissionName
                    )
                }
            }
        }
    }

    fun requestPermission(
        fragment: Fragment,
        permissionType: PermissionType,
        result: ((granted: Boolean) -> Unit)? = null
    ): () -> Unit {
        val permissions = getPermissions(permissionType)
        val permissionName = fragment.context?.let { getPermissionName(permissionType, it) } ?: ""
        val context = fragment.context
        val activity = fragment.activity
        return fragment.requestMultiplePermissions(permissions) {
            when (it) {
                0 -> {//权限申请成功
                    if (permissionType == PermissionType.PERMISSION_LOCATION) {//所有权限都同意
                        if (context?.isLocationServiceEnable() == true) {//6.0以上GPS开启成功
                            result?.invoke(true)//权限申请成功
                        } else {
                            showLocationServiceDialog(activity, result)
                        }
                    } else {
                        result?.invoke(true)//权限申请成功
                    }
                }
                1 -> {//至少拒绝了一个权限并没有勾选不再提示
                    result?.invoke(false)
                }
                2 -> {//至少拒绝了一个权限并勾选了不再提示
                    result?.invoke(false)
                    showPermissionDialog(
                        activity,
                        context?.getString(R.string.app_name) ?: "",
                        permissionName
                    )
                }
            }
        }
    }

    /**
     * 显示权限拒绝并勾选不再提示对话框
     *
     * @param activity       Activity
     * @param appName        App名称
     * @param permissionName 权限名称
     */
    private fun showPermissionDialog(
        activity: Activity?,
        appName: String,
        permissionName: String?
    ) {
        (activity as? AppCompatActivity)?.let {
            PermissionDialogFragment.newInstance(appName, permissionName)
                .show(it.supportFragmentManager, "PermissionDialogFragment")
        }
    }

    /**
     * 显示定位服务未开启对话框
     *
     * @param activity
     * @param result
     */
    private fun showLocationServiceDialog(
        activity: Activity?,
        result: ((granted: Boolean) -> Unit)? = null
    ) {
        (activity as? AppCompatActivity)?.let {
            LocationServiceDialogFragment.newInstance()
                .apply {
                    onLocationServiceOpenSuccess = {
                        result?.invoke(true)
                    }
                    onLocationServiceOpenFailure = {
                        result?.invoke(false)
                    }
                }
                .show(it.supportFragmentManager, "LocationServiceDialogFragment")
        }
    }
}