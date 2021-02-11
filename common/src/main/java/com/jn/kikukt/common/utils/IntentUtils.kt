package com.jn.kikukt.common.utils

import android.Manifest
import android.content.ContentValues
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Environment.DIRECTORY_PICTURES
import android.provider.MediaStore
import android.provider.Settings
import androidx.annotation.RequiresApi
import com.jn.kikukt.common.utils.file.FileUtils
import java.io.File


/**
 * Author：Stevie.Chen Time：2019/7/15
 * Class Comment：打开系统相关服务管理类
 */

object IntentUtils {

    /**
     * 打开照相机Intent
     *
     * @param cameraImgPath 拍照存储路径
     */
    fun getCameraIntent(cameraImgPath: String) = Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
        val file = File(cameraImgPath)
        file.deleteOnExit()
        //判断是否是AndroidN以及更高的版本
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //添加这一句表示对目标应用临时授权该Uri所代表的文件
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        putExtra(MediaStore.EXTRA_OUTPUT, UriUtils.getUri(cameraImgPath))
    }

    /**
     * 打开相册Intent
     */
    fun getAlbumIntent() = Intent(Intent.ACTION_GET_CONTENT).apply {
        addCategory(Intent.CATEGORY_OPENABLE)
        type = "image/*"
        putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("image/jpg", "image/jpeg", "image/png"))
    }

    /**
     * 打开裁剪Intent（拍照裁剪）
     */
    fun getCropIntent(
        srcPath: String,
        targetDirectoryName: String,
        targetFileName: String,
        resultBlock: ((uri: Uri?) -> Unit)? = null
    ) =
        Intent("com.android.camera.action.CROP").apply {
            val context = ContextUtils.context
            val srcUri = UriUtils.getUri(srcPath)
            val targetUri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                // 通过 MediaStore API 插入file 为了拿到系统裁剪要保存到的uri
                //（因为App没有权限不能访问公共存储空间，需要通过 MediaStore API来操作）
                val values = ContentValues().apply {
                    put(MediaStore.Images.Media.DISPLAY_NAME, targetFileName)
                    put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                    put(
                        MediaStore.MediaColumns.RELATIVE_PATH, DIRECTORY_PICTURES
                                + File.separator + targetDirectoryName
                    )
                    put(MediaStore.MediaColumns.IS_PENDING, false)
                }
                context.contentResolver
                    .insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
            } else {
                val targetFile = File(FileUtils.getImagePath(targetFileName))
                targetFile.deleteOnExit()
                Uri.fromFile(targetFile)
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
                putExtra("noFaceDetection", true)//去除默认的人脸识别，否则和剪裁匡重叠
            }
            setDataAndType(srcUri, "image/*")
            putExtra("crop", "true")
            putExtra("aspectX", 1)
            putExtra("aspectY", 1)
            putExtra("outputX", 150)
            putExtra("outputY", 150)
            putExtra("return-data", true)
            putExtra("outputFormat", "PNG")// 返回格式
            putExtra(MediaStore.EXTRA_OUTPUT, targetUri)//裁剪后的路径
            resultBlock?.invoke(targetUri)
        }

    /**
     * 打开裁剪Intent(相册裁剪)
     */
    fun getCropIntent(
        srcUri: Uri,
        targetDirectoryName: String,
        targetFileName: String,
        resultBlock: ((uri: Uri?) -> Unit)? = null
    ) =
        Intent("com.android.camera.action.CROP").apply {
            val context = ContextUtils.context
            val srcRealUri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val bm = BitmapFactory.decodeStream(context.contentResolver.openInputStream(srcUri))
                val filePath = FileUtils.saveBitmap(bm, System.currentTimeMillis().toString()) ?: ""
                UriUtils.getUri(filePath)
            } else srcUri
            val targetUri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                // 通过 MediaStore API 插入file 为了拿到系统裁剪要保存到的uri
                //（因为App没有权限不能访问公共存储空间，需要通过 MediaStore API来操作）
                val values = ContentValues().apply {
                    put(MediaStore.Images.Media.DISPLAY_NAME, targetFileName)
                    put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                    put(
                        MediaStore.MediaColumns.RELATIVE_PATH, DIRECTORY_PICTURES
                                + File.separator + targetDirectoryName
                    )
                    put(MediaStore.MediaColumns.IS_PENDING, false)
                }
                context.contentResolver
                    .insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
            } else {
                val targetFile = File(FileUtils.getImagePath(targetFileName))
                targetFile.deleteOnExit()
                Uri.fromFile(targetFile)
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
                putExtra("noFaceDetection", true)//去除默认的人脸识别，否则和剪裁匡重叠
            }
            if (srcRealUri != null) {
                setDataAndType(srcRealUri, "image/*")
                putExtra("crop", "true")
                putExtra("aspectX", 1)
                putExtra("aspectY", 1)
                putExtra("outputX", 150)
                putExtra("outputY", 150)
                putExtra("return-data", true)
                putExtra("outputFormat", "PNG")// 返回格式
                putExtra(MediaStore.EXTRA_OUTPUT, targetUri)//裁剪后的路径
                resultBlock?.invoke(targetUri)
            } else {
                resultBlock?.invoke(null)
            }
        }

    /**
     * 拨打电话Intent
     */
    fun getCallPhoneIntent(phoneNumber: String) = Intent(Intent.ACTION_CALL).apply {
        data = Uri.parse("tel:$phoneNumber")
    }

    /**
     * 安装Apk Intent
     * 需要在manifest中声明[Manifest.permission.REQUEST_INSTALL_PACKAGES]权限,否则安装时会一闪而过
     */
    fun getInstallIntent(apkPath: String) = Intent(Intent.ACTION_VIEW).apply {
        //判断是否是AndroidN以及更高的版本
        val apkUri = UriUtils.getUri(apkPath)
        flags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //区别于 FLAG_GRANT_READ_URI_PERMISSION 跟 FLAG_GRANT_WRITE_URI_PERMISSION， URI权限会持久存在即使重启，直到明确的用 revokeUriPermission(Uri, int) 撤销。
            // 这个flag只提供可能持久授权。但是接收的应用必须调用ContentResolver的takePersistableUriPermission(Uri, int)方法实现
            Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION or Intent.FLAG_ACTIVITY_NEW_TASK
        } else {
            Intent.FLAG_ACTIVITY_NEW_TASK
        }
        setDataAndType(apkUri, "application/vnd.android.package-archive")
    }


    /**
     * 卸载Apk Intent
     */
    fun getUninstallIntent(packageName: String) =
        Intent(Intent.ACTION_DELETE).apply {
            data = Uri.parse("package:$packageName")
        }

    /**
     * 管理未知来源应用的intent
     */
    @RequiresApi(Build.VERSION_CODES.O)
    fun getManageUnknownAppSourcesIntent() = Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES)

    /**
     * 管理系统弹框的intent
     */
    @RequiresApi(Build.VERSION_CODES.M)
    fun getManageOverlayPermissionIntent() = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
}