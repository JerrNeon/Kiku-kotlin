package com.jn.kikukt.utils

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import com.jn.kikukt.R
import com.jn.kikukt.annonation.*
import com.jn.kikukt.common.utils.checkLocationSereviceOPenInM
import com.jn.kikukt.dialog.LocationServiceDialogFragment
import com.jn.kikukt.dialog.PermissionDialogFragment
import com.tbruyelle.rxpermissions2.Permission
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.functions.Consumer

/**
 * Author：Stevie.Chen Time：2019/7/11
 * Class Comment：RxPermissions权限管理
 */
object RxPermissionsManager {

    private val CAMERA_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)//相机权限
    private val WRITE_EXTERNAL_STORAGE_PERMISSIONS = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)//存储空间(相册)权限
    private val CALL_PHONES_PERMISSIONS =
        arrayOf(Manifest.permission.CALL_PHONE, Manifest.permission.READ_PHONE_STATE)//电话权限
    private val LOCATIONS_PERMISSIONS =
        arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)//定位权限
    private val CAMERA_WRITE_EXTERNAL_STORAGE_PERMISSIONS =
        arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)//拍照&存储空间权限

    @SuppressLint("CheckResult")
    fun requestPermission(
        activity: Activity,
        rxPermissions: RxPermissions?,
        @PermissionType permissionType: Int,
        appName: String,
        consumer: Consumer<Boolean>?
    ) {
        var permissions: Array<String> = emptyArray()
        var permissionName: String? = null
        when (permissionType) {
            CAMERA -> {
                permissions = CAMERA_PERMISSIONS
                permissionName = activity.resources.getString(R.string.permission_camera)
            }
            WRITE_EXTERNAL_STORAGE -> {
                permissions =
                    WRITE_EXTERNAL_STORAGE_PERMISSIONS
                permissionName = activity.resources.getString(R.string.permission_storage)
            }
            CALL_PHONE -> {
                permissions = CALL_PHONES_PERMISSIONS
                permissionName = activity.resources.getString(R.string.permission_phone)
            }
            LOCATION -> {
                permissions = LOCATIONS_PERMISSIONS
                permissionName = activity.resources.getString(R.string.permission_location)
            }
            CAMERA_WRITE_EXTERNAL_STORAGE -> {
                permissions =
                    CAMERA_WRITE_EXTERNAL_STORAGE_PERMISSIONS
                permissionName = activity.resources.getString(R.string.permission_camera_storage)
            }
            else -> {
            }
        }
        val finalPermissionName = permissionName
        requestEachCombined(rxPermissions, permissions,
            Consumer { permission ->
                try {
                    when {
                        permission.granted -> {
                            if (permissionType == LOCATION) {//所有权限都同意
                                if (activity.applicationContext.checkLocationSereviceOPenInM()) {//6.0以上GPS开启成功
                                    consumer?.accept(true)//权限申请成功
                                } else {
                                    showLocationServiceDialog(
                                        activity,
                                        consumer
                                    )
                                }
                            } else {
                                consumer!!.accept(true)//权限申请成功
                            }
                        }
                        permission.shouldShowRequestPermissionRationale -> {//至少拒绝了一个权限并没有勾选不再提示
                            consumer!!.accept(false)
                        }
                        else -> {//至少拒绝了一个权限并勾选了不再提示
                            consumer!!.accept(false)
                            showPermissionDialog(
                                activity,
                                appName,
                                finalPermissionName
                            )
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            })
    }

    @SuppressLint("CheckResult")
    fun requestEachCombined(
        rxPermissions: RxPermissions?,
        permissions: Array<String>,
        consumer: Consumer<Permission>?
    ) {
        if (rxPermissions == null)
            throw NullPointerException("RxPermissions not init")
        rxPermissions
            .requestEachCombined(*permissions)
            .subscribe { permission ->
                consumer?.accept(permission)
                when {
                    permission.granted -> {//所有权限都同意

                    }
                    permission.shouldShowRequestPermissionRationale -> {//至少拒绝了一个权限并没有勾选不再提示

                    }
                    else -> {//至少拒绝了一个权限并勾选了不再提示

                    }
                }
            }
    }

    /**
     * 请求所有权限
     *
     * @param rxPermissions RxPermissions
     * @param consumer      Consumer
     * @param permissions   所有权限
     *
     *
     * 一般用于启动页
     *
     */
    @SuppressLint("CheckResult")
    fun requestAllPermissions(rxPermissions: RxPermissions?, consumer: Consumer<Boolean>?, permissions: Array<String>) {
        if (rxPermissions == null)
            throw NullPointerException("RxPermissions not init")
        rxPermissions
            .request(*permissions)
            .subscribe { aBoolean ->
                try {
                    consumer?.accept(aBoolean)
                } catch (e: Exception) {
                    e.printStackTrace()
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
     * @param consumer
     */
    private fun showLocationServiceDialog(activity: Activity, consumer: Consumer<Boolean>?) {
        if (activity is AppCompatActivity) {
            LocationServiceDialogFragment.newInstance()
                .show(
                    activity.supportFragmentManager,
                    "",
                    object : LocationServiceDialogFragment.ILocationServiceListener {
                        override fun onLocationServiceOpenSuccess() {
                            try {
                                consumer!!.accept(true)
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }

                        override fun onLocationServiceOpenFailure() {
                            try {
                                consumer!!.accept(false)
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                    })
        }
    }
}