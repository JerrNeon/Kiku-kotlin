package com.jn.kikukt.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Context.TELEPHONY_SERVICE
import android.content.Intent
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.provider.Settings
import android.support.v4.app.Fragment
import android.telephony.TelephonyManager
import java.io.File

/**
 * Author：Stevie.Chen Time：2019/7/15
 * Class Comment：打开系统相关服务管理类
 */

/**
 * 打开照相机(Activity)
 *
 * @param cameraImgPath 拍照存储路径
 * @param requestCode   请求码
 */
fun Activity.openCamera(cameraImgPath: String, requestCode: Int) {
    try {
        val file = File(cameraImgPath)
        file.deleteOnExit()
        val photoIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        //判断是否是AndroidN以及更高的版本
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
        //添加这一句表示对目标应用临时授权该Uri所代表的文件
            photoIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        val picUri = UriUtils.getUri(applicationContext, cameraImgPath)
        photoIntent.putExtra(MediaStore.EXTRA_OUTPUT, picUri)
        startActivityForResult(photoIntent, requestCode)
    } catch (e: Exception) {
        e.printStackTrace()
    }

}

/**
 * 打开照相机(Fragment)
 *
 * @param cameraImgPath 拍照存储路径
 * @param requestCode   请求码
 */
fun Fragment.openCamera(cameraImgPath: String, requestCode: Int) {
    try {
        val file = File(cameraImgPath)
        file.deleteOnExit()
        val photoIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        //判断是否是AndroidN以及更高的版本
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
        //添加这一句表示对目标应用临时授权该Uri所代表的文件
            photoIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        val picUri = UriUtils.getUri(activity!!.applicationContext, cameraImgPath)
        photoIntent.putExtra(MediaStore.EXTRA_OUTPUT, picUri)
        startActivityForResult(photoIntent, requestCode)
    } catch (e: Exception) {
        e.printStackTrace()
    }

}

/**
 * 打开相册(Activity)
 *
 * @param requestCode 请求码
 */
fun Activity.openAlbum(requestCode: Int) {
    try {
        val iconIntent = Intent(Intent.ACTION_GET_CONTENT)
        iconIntent.addCategory(Intent.CATEGORY_OPENABLE)
        iconIntent.type = "image/*"
        val mimeTypes = arrayOf("image/jpg", "image/jpeg", "image/png")
        iconIntent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
        startActivityForResult(iconIntent, requestCode)
    } catch (e: Exception) {
        e.printStackTrace()
    }

}

/**
 * 打开相册(Fragment)
 *
 * @param requestCode 请求码
 */
fun Fragment.openAlbum(requestCode: Int) {
    try {
        val iconIntent = Intent(Intent.ACTION_GET_CONTENT)
        iconIntent.addCategory(Intent.CATEGORY_OPENABLE)
        iconIntent.type = "image/*"
        val mimeTypes = arrayOf("image/jpg", "image/jpeg", "image/png")
        iconIntent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
        startActivityForResult(iconIntent, requestCode)
    } catch (e: Exception) {
        e.printStackTrace()
    }

}

/**
 * 裁剪图片
 *
 * @param imgPath     要裁剪图片路径
 * @param targetPath  裁剪后路径
 * @param requestCode 请求码
 */
fun Activity.cropImage(imgPath: String, targetPath: String, requestCode: Int) {
    val file = File(targetPath)
    file.deleteOnExit()
    val intent = Intent("com.android.camera.action.CROP")
    val imgUri = UriUtils.getUri(applicationContext, imgPath)
    val targetUri = Uri.fromFile(File(targetPath))
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        intent.putExtra("noFaceDetection", true)//去除默认的人脸识别，否则和剪裁匡重叠
    }
    intent.setDataAndType(imgUri, "image/*")
    intent.putExtra("crop", "true")
    intent.putExtra("aspectX", 1)
    intent.putExtra("aspectY", 1)
    intent.putExtra("outputX", 150)
    intent.putExtra("outputY", 150)
    intent.putExtra("return-data", true)
    intent.putExtra("outputFormat", "PNG")// 返回格式
    intent.putExtra(MediaStore.EXTRA_OUTPUT, targetUri)//裁剪后的路径
    startActivityForResult(intent, requestCode)
}

/**
 * 裁剪图片
 *
 * @param imgPath     裁剪路径
 * @param requestCode 请求码
 */
fun Fragment.cropImage(imgPath: String, targetPath: String, requestCode: Int) {
    val file = File(targetPath)
    file.deleteOnExit()
    val intent = Intent("com.android.camera.action.CROP")
    val imgUri = UriUtils.getUri(activity!!.applicationContext, imgPath)
    val targetUri = Uri.fromFile(File(targetPath))
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        intent.putExtra("noFaceDetection", true)//去除默认的人脸识别，否则和剪裁匡重叠
    }
    intent.setDataAndType(imgUri, "image/*")
    intent.putExtra("crop", "true")
    intent.putExtra("aspectX", 1)
    intent.putExtra("aspectY", 1)
    intent.putExtra("outputX", 150)
    intent.putExtra("outputY", 150)
    intent.putExtra("return-data", true)
    intent.putExtra("outputFormat", "PNG")// 返回格式
    intent.putExtra(MediaStore.EXTRA_OUTPUT, targetUri)//裁剪后的路径
    startActivityForResult(intent, requestCode)
}

/**
 * 拨打电话
 */
fun Activity.callPhone(phoneNumber: String) {
    try {
        val intent = Intent(Intent.ACTION_CALL)
        intent.data = Uri.parse("tel:$phoneNumber")
        startActivity(intent)
    } catch (e: Exception) {
        e.printStackTrace()
    }

}

/**
 * 拨打电话
 *
 * @param mContext fragment
 */
fun Fragment.callPhone(phoneNumber: String) {
    try {
        val intent = Intent(Intent.ACTION_CALL)
        intent.data = Uri.parse("tel:$phoneNumber")
        startActivity(intent)
    } catch (e: Exception) {
        e.printStackTrace()
    }

}

/**
 * 打开此app系统设置界面
 */
fun Context.openApplicationSetting() {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
    val uri = Uri.fromParts("package", packageName, null)
    intent.data = uri
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    startActivity(intent)
}

/**
 * 是否有SIM卡
 * 使用这个方法需要申请 Manifest.permission.READ_PHONE_STATE 这个权限
 */
@SuppressLint("MissingPermission", "HardwareIds")
fun Context.exitSimCard(): Boolean {
    try {
        val tm = getSystemService(TELEPHONY_SERVICE) as TelephonyManager
        val simSer = tm.simSerialNumber
        if (simSer == null || simSer == "") {
            return false
        }
    } catch (e: Exception) {
        e.printStackTrace()
        return false
    }

    return true
}

/**
 * 安装
 */
fun Context.install(apkPath: String) {
    // 核心是下面几句代码
    val intent = Intent(Intent.ACTION_VIEW)
    //判断是否是AndroidN以及更高的版本
    val apkUri = UriUtils.getUri(applicationContext, apkPath)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        //区别于 FLAG_GRANT_READ_URI_PERMISSION 跟 FLAG_GRANT_WRITE_URI_PERMISSION， URI权限会持久存在即使重启，直到明确的用 revokeUriPermission(Uri, int) 撤销。
        // 这个flag只提供可能持久授权。但是接收的应用必须调用ContentResolver的takePersistableUriPermission(Uri, int)方法实现
        intent.flags =
            Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION or Intent.FLAG_ACTIVITY_NEW_TASK
    } else {
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    }
    intent.setDataAndType(apkUri, "application/vnd.android.package-archive")
    startActivity(intent)
}

/**
 * 安装
 */
fun Context.getInstallIntent(apkPath: String): Intent {
    // 核心是下面几句代码
    val intent = Intent(Intent.ACTION_VIEW)
    //判断是否是AndroidN以及更高的版本
    val apkUri = UriUtils.getUri(applicationContext, apkPath)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        //区别于 FLAG_GRANT_READ_URI_PERMISSION 跟 FLAG_GRANT_WRITE_URI_PERMISSION， URI权限会持久存在即使重启，直到明确的用 revokeUriPermission(Uri, int) 撤销。
        // 这个flag只提供可能持久授权。但是接收的应用必须调用ContentResolver的takePersistableUriPermission(Uri, int)方法实现
        intent.flags =
            Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION or Intent.FLAG_ACTIVITY_NEW_TASK
    } else {
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    }
    intent.setDataAndType(apkUri, "application/vnd.android.package-archive")
    return intent
}

/**
 * 卸载
 */
fun Context.uninstall(packageName: String) {
    val packageURI = Uri.parse("package:$packageName")
    val uninstallIntent = Intent(Intent.ACTION_DELETE, packageURI)
    startActivity(uninstallIntent)
}

/**
 * 判断是否开启定位服务
 */
fun Context.checkLocationServiceOPen(): Boolean {
    val locManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
    return locManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
}

/**
 * 6.0系统判断是否开启定位服务
 */
fun Context.checkLocationSereviceOPenInM(): Boolean {
    //6.0系统并且定位服务未开启
    return !(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !checkLocationServiceOPen())
}

/**
 * 打开定位服务界面
 */
fun Activity.openLocationService(requestCode: Int) {
    val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
    startActivityForResult(intent, requestCode) // 设置完成后返回到原来的界面
}

/**
 * 打开定位服务界面
 */
fun Fragment.openLocationService(requestCode: Int) {
    val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
    startActivityForResult(intent, requestCode) // 设置完成后返回到原来的界面
}