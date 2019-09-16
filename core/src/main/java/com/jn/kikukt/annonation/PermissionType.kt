package com.jn.kikukt.annonation

import androidx.annotation.IntDef

/**
 * Author：Stevie.Chen Time：2019/7/10
 * Class Comment：
 */
const val CAMERA = 1//相机
const val WRITE_EXTERNAL_STORAGE = 2//存储空间(打开相册)
const val CALL_PHONE = 3//电话
const val LOCATION = 4//定位
const val CAMERA_WRITE_EXTERNAL_STORAGE = 5//相机&存储空间(打开相册)

/**
 * 权限类型
 */
@IntDef(CAMERA, WRITE_EXTERNAL_STORAGE, CALL_PHONE, LOCATION, CAMERA_WRITE_EXTERNAL_STORAGE)
@Target(AnnotationTarget.FIELD, AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.SOURCE)
annotation class PermissionType