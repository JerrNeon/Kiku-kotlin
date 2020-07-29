package com.jn.kikukt.utils

import android.Manifest
import android.app.Activity
import androidx.annotation.IntDef
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.jn.kikukt.R
import com.jn.kikukt.common.utils.checkLocationSereviceOPenInM
import com.jn.kikukt.dialog.LocationServiceDialogFragment
import com.jn.kikukt.dialog.PermissionDialogFragment
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.disposables.Disposable

/**
 * Author：Stevie.Chen Time：2019/7/11
 * Class Comment：RxPermissions权限管理
 */
private val Activity.rxPermissions
    get() = RxPermissions(this)

const val PERMISSION_CAMERA = 1//相机
const val PERMISSION_WRITE_EXTERNAL_STORAGE = 2//存储空间(打开相册)
const val PERMISSION_CALL_PHONE = 3//电话
const val PERMISSION_LOCATION = 4//定位
const val PERMISSION_CAMERA_WRITE_EXTERNAL_STORAGE = 5//相机&存储空间(打开相册)

/**
 * 权限类型
 */
@IntDef(
    PERMISSION_CAMERA,
    PERMISSION_WRITE_EXTERNAL_STORAGE,
    PERMISSION_CALL_PHONE,
    PERMISSION_LOCATION,
    PERMISSION_CAMERA_WRITE_EXTERNAL_STORAGE
)
@Target(AnnotationTarget.FIELD, AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.SOURCE)
annotation class PermissionType

object RxPermissionsManager {

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

    fun requestPermission(
        activity: Activity,
        @PermissionType permissionType: Int,
        result: ((granted: Boolean) -> Unit)? = null
    ) {
        val rxPermissions = activity.rxPermissions
        var permissions: Array<String> = emptyArray()
        var permissionName: String? = null
        when (permissionType) {
            PERMISSION_CAMERA -> {
                permissions = CAMERA_PERMISSIONS
                permissionName = activity.resources.getString(R.string.permission_camera)
            }
            PERMISSION_WRITE_EXTERNAL_STORAGE -> {
                permissions =
                    WRITE_EXTERNAL_STORAGE_PERMISSIONS
                permissionName = activity.resources.getString(R.string.permission_storage)
            }
            PERMISSION_CALL_PHONE -> {
                permissions = CALL_PHONES_PERMISSIONS
                permissionName = activity.resources.getString(R.string.permission_phone)
            }
            PERMISSION_LOCATION -> {
                permissions = LOCATIONS_PERMISSIONS
                permissionName = activity.resources.getString(R.string.permission_location)
            }
            PERMISSION_CAMERA_WRITE_EXTERNAL_STORAGE -> {
                permissions =
                    CAMERA_WRITE_EXTERNAL_STORAGE_PERMISSIONS
                permissionName = activity.resources.getString(R.string.permission_camera_storage)
            }
            else -> {
            }
        }
        val disposable = rxPermissions
            .requestEachCombined(*permissions)
            .subscribe { permission ->
                try {
                    when {
                        permission.granted -> {
                            if (permissionType == PERMISSION_LOCATION) {//所有权限都同意
                                if (activity.applicationContext.checkLocationSereviceOPenInM()) {//6.0以上GPS开启成功
                                    result?.invoke(true)//权限申请成功
                                } else {
                                    showLocationServiceDialog(
                                        activity,
                                        result
                                    )
                                }
                            } else {
                                result?.invoke(true)//权限申请成功
                            }
                        }
                        permission.shouldShowRequestPermissionRationale -> {//至少拒绝了一个权限并没有勾选不再提示
                            result?.invoke(false)
                        }
                        else -> {//至少拒绝了一个权限并勾选了不再提示
                            result?.invoke(false)
                            showPermissionDialog(
                                activity,
                                activity.getString(R.string.app_name),
                                permissionName
                            )
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        dispose(activity, disposable)
    }

    /**
     * 请求所有权限
     *
     * @param permissions   所有权限
     * @param result      Consumer
     *
     * 一般用于启动页
     */
    fun requestPermission(
        activity: Activity,
        permissions: Array<String>,
        result: ((granted: Boolean) -> Unit)? = null
    ) {
        val disposable = activity.rxPermissions
            .request(*permissions)
            .subscribe { aBoolean ->
                try {
                    result?.invoke(aBoolean)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        dispose(activity, disposable)
    }

    private fun dispose(activity: Activity, disposable: Disposable) {
        if (activity is AppCompatActivity) {
            activity.lifecycle.addObserver(object : LifecycleEventObserver {
                override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                    if (Lifecycle.Event.ON_DESTROY == event && !disposable.isDisposed) {
                        disposable.dispose()
                    }
                }
            })
        }
    }

    /**
     * 显示权限拒绝并勾选不再提示对话框
     *
     * @param activity       Activity
     * @param appName        App名称
     * @param permissionName 权限名称
     */
    private fun showPermissionDialog(activity: Activity, appName: String, permissionName: String?) {
        if (activity is AppCompatActivity) {
            PermissionDialogFragment.newInstance(appName, permissionName)
                .show(activity.supportFragmentManager, "")
        }
    }

    /**
     * 显示定位服务未开启对话框
     *
     * @param activity
     * @param result
     */
    private fun showLocationServiceDialog(
        activity: Activity,
        result: ((granted: Boolean) -> Unit)? = null
    ) {
        if (activity is AppCompatActivity) {
            LocationServiceDialogFragment.newInstance()
                .show(
                    activity.supportFragmentManager,
                    "",
                    {
                        result?.invoke(true)
                    }, {
                        result?.invoke(false)
                    })
        }
    }
}