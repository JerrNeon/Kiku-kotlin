package com.jn.kikukt.common.utils

import android.content.ContentResolver
import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.storage.StorageManager
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import androidx.core.content.FileProvider
import com.jn.kikukt.common.utils.file.FileIOUtils
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import java.io.InputStream
import java.lang.reflect.Array
import java.lang.reflect.Method


/**
 * Author：Stevie.Chen Time：2019/7/15
 * Class Comment：获取uri, 兼容7.0
 */
object UriUtils {

    private val context: Context
        get() = ContextUtils.context
    private val packageName
        get() = ContextUtils.context.packageName

    /**
     * resPath The path of res.
     */
    fun res2Uri(resPath: String): Uri {
        return Uri.parse("android.resource://$packageName/$resPath")
    }

    /**
     * 获取Uri
     *
     * 适配android7.0
     *
     * @param filePath       文件路径
     * @param application_id 应用包名
     * @return
     */
    fun getUri(filePath: String, application_id: String = packageName): Uri? {
        try {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                FileProvider.getUriForFile(
                    context,
                    "$application_id.fileprovider",
                    File(filePath)
                )
            } else
                Uri.fromFile(File(filePath))
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    /**
     * Android Q获取相册图片可访问Uri
     */
    fun getImageContentUri(path: String?): Uri? {
        val cursor = context.contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            arrayOf(MediaStore.Images.Media._ID), MediaStore.Images.Media.DATA + "=? ",
            arrayOf(path), null
        )
        try {
            if (cursor != null && cursor.moveToFirst()) {
                val id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
                val baseUri = Uri.parse("content://media/external/images/media");
                return Uri.withAppendedPath(baseUri, "" + id);
            } else {
                // 如果图片不在手机的共享图片数据库，就先把它插入。
                return if (path?.isNotEmpty() == true && File(path).exists()) {
                    val values = ContentValues()
                    values.put(MediaStore.Images.Media.DATA, path);
                    context.contentResolver.insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        values
                    )
                } else {
                    null
                }
            }
        } catch (e: Exception) {
            return null
        } finally {
            cursor?.close()
        }
    }

    /**
     * 手机相册中返回的Uri格式可能不是以.jpg/.png结尾的格式
     * 获取手机中uri的真实路径(选取手机相册返回的uri统一经过此方法获得图片路径)
     *
     * @param uri     The Uri to query.
     */
    fun getPathFromUri(uri: Uri): String? {
        return uri2File(uri)?.absolutePath
    }

    /**
     * Uri to file.
     *
     * @param uri The uri.
     * @return file
     */
    fun uri2File(uri: Uri?): File? {
        if (uri == null) return null
        val file = uri2FileReal(uri)
        return file ?: copyUri2Cache(uri)
    }

    /**
     * Uri to file.
     *
     * @param uri The uri.
     * @return file
     */
    private fun uri2FileReal(uri: Uri): File? {
        Log.d("UriUtils", uri.toString())
        val authority = uri.authority
        val scheme = uri.scheme
        val path = uri.path
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && path != null) {
            val externals = arrayOf("/external/", "/external_path/")
            var file: File?
            for (external in externals) {
                if (path.startsWith(external)) {
                    file = File(
                        Environment.getExternalStorageDirectory().absolutePath
                            .toString() + path.replace(external, "/")
                    )
                    if (file.exists()) {
                        Log.d("UriUtils", "$uri -> $external")
                        return file
                    }
                }
            }
            file = null
            when {
                path.startsWith("/files_path/") -> {
                    file = File(
                        context.filesDir.absolutePath
                            .toString() + path.replace("/files_path/", "/")
                    )
                }
                path.startsWith("/cache_path/") -> {
                    file = File(
                        context.cacheDir.absolutePath
                            .toString() + path.replace("/cache_path/", "/")
                    )
                }
                path.startsWith("/external_files_path/") -> {
                    file = File(
                        context.getExternalFilesDir(null)?.absolutePath
                            .toString() + path.replace("/external_files_path/", "/")
                    )
                }
                path.startsWith("/external_cache_path/") -> {
                    file = File(
                        context.externalCacheDir?.absolutePath
                            .toString() + path.replace("/external_cache_path/", "/")
                    )
                }
            }
            if (file != null && file.exists()) {
                Log.d("UriUtils", "$uri -> $path")
                return file
            }
        }
        return if (ContentResolver.SCHEME_FILE == scheme) {
            if (path != null) return File(path)
            Log.d("UriUtils", "$uri parse failed. -> 0")
            null
        } // end 0
        else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT
            && DocumentsContract.isDocumentUri(context, uri)
        ) {
            if ("com.android.externalstorage.documents" == authority) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":".toRegex()).toTypedArray()
                val type = split[0]
                if ("primary".equals(type, ignoreCase = true)) {
                    return File(
                        Environment.getExternalStorageDirectory().toString() + "/" + split[1]
                    )
                } else {
                    // Below logic is how External Storage provider build URI for documents
                    // http://stackoverflow.com/questions/28605278/android-5-sd-card-label
                    val mStorageManager =
                        context.getSystemService(Context.STORAGE_SERVICE) as StorageManager
                    try {
                        val storageVolumeClazz = Class.forName("android.os.storage.StorageVolume")
                        val getVolumeList: Method =
                            mStorageManager.javaClass.getMethod("getVolumeList")
                        val getUuid: Method = storageVolumeClazz.getMethod("getUuid")
                        val getState: Method = storageVolumeClazz.getMethod("getState")
                        val getPath: Method = storageVolumeClazz.getMethod("getPath")
                        val isPrimary: Method = storageVolumeClazz.getMethod("isPrimary")
                        val isEmulated: Method = storageVolumeClazz.getMethod("isEmulated")
                        val result: Any? = getVolumeList.invoke(mStorageManager)
                        result?.let {
                            val length: Int = Array.getLength(result)
                            for (i in 0 until length) {
                                val storageVolumeElement: Any? = Array.get(result, i)
                                //String uuid = (String) getUuid.invoke(storageVolumeElement);
                                val mounted =
                                    (Environment.MEDIA_MOUNTED == getState.invoke(
                                        storageVolumeElement
                                    )
                                            || Environment.MEDIA_MOUNTED_READ_ONLY == getState.invoke(
                                        storageVolumeElement
                                    ))

                                //if the media is not mounted, we need not get the volume details
                                if (!mounted) continue

                                //Primary storage is already handled.
                                if ((isPrimary.invoke(storageVolumeElement) as? Boolean) == true
                                    && (isEmulated.invoke(storageVolumeElement) as? Boolean) == true
                                ) {
                                    continue
                                }
                                val uuid = getUuid.invoke(storageVolumeElement) as String
                                if (uuid == type) {
                                    return File(
                                        "${getPath.invoke(storageVolumeElement)}/${split[1]}"
                                    )
                                }
                            }
                        }
                    } catch (ex: java.lang.Exception) {
                        Log.d("UriUtils", "$uri parse failed. $ex -> 1_0")
                    }
                }
                Log.d("UriUtils", "$uri parse failed. -> 1_0")
                null
            } // end 1_0
            else if ("com.android.providers.downloads.documents" == authority) {
                var id = DocumentsContract.getDocumentId(uri)
                if (TextUtils.isEmpty(id)) {
                    Log.d("UriUtils", "$uri parse failed(id is null). -> 1_1")
                    return null
                }
                if (id.startsWith("raw:")) {
                    return File(id.substring(4))
                } else if (id.startsWith("msf:")) {
                    id = id.split(":".toRegex()).toTypedArray()[1]
                }
                var availableId: Long
                availableId = try {
                    id.toLong()
                } catch (e: java.lang.Exception) {
                    return null
                }
                val contentUriPrefixesToTry = arrayOf(
                    "content://downloads/public_downloads",
                    "content://downloads/all_downloads",
                    "content://downloads/my_downloads"
                )
                for (contentUriPrefix in contentUriPrefixesToTry) {
                    val contentUri =
                        ContentUris.withAppendedId(Uri.parse(contentUriPrefix), availableId)
                    try {
                        val file = getFileFromUri(contentUri, "1_1")
                        if (file != null) {
                            return file
                        }
                    } catch (ignore: java.lang.Exception) {
                    }
                }
                Log.d("UriUtils", "$uri parse failed. -> 1_1")
                null
            } // end 1_1
            else if ("com.android.providers.media.documents" == authority) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":".toRegex()).toTypedArray()
                val type = split[0]
                val contentUri: Uri
                contentUri = when (type) {
                    "image" -> {
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    }
                    "video" -> {
                        MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                    }
                    "audio" -> {
                        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                    }
                    else -> {
                        Log.d("UriUtils", "$uri parse failed. -> 1_2")
                        return null
                    }
                }
                val selection = "_id=?"
                val selectionArgs = arrayOf(split[1])
                getFileFromUri(contentUri, selection, selectionArgs, "1_2")
            } // end 1_2
            else if (ContentResolver.SCHEME_CONTENT == scheme) {
                getFileFromUri(uri, "1_3")
            } // end 1_3
            else {
                Log.d("UriUtils", "$uri parse failed. -> 1_4")
                null
            } // end 1_4
        } // end 1
        else if (ContentResolver.SCHEME_CONTENT == scheme) {
            getFileFromUri(uri, "2")
        } // end 2
        else {
            Log.d("UriUtils", "$uri parse failed. -> 3")
            null
        } // end 3
    }

    private fun getFileFromUri(uri: Uri, code: String): File? {
        return getFileFromUri(uri, null, null, code)
    }

    private fun getFileFromUri(
        uri: Uri,
        selection: String?,
        selectionArgs: kotlin.Array<String>?,
        code: String
    ): File? {
        if ("com.google.android.apps.photos.content" == uri.authority) {
            val lastPathSegment = uri.lastPathSegment
            if (lastPathSegment != null && !TextUtils.isEmpty(lastPathSegment)) {
                return File(lastPathSegment)
            }
        } else if ("com.tencent.mtt.fileprovider" == uri.authority) {
            val path = uri.path
            if (!TextUtils.isEmpty(path)) {
                val fileDir: File = Environment.getExternalStorageDirectory()
                return File(fileDir, path!!.substring("/QQBrowser".length, path.length))
            }
        } else if ("com.huawei.hidisk.fileprovider" == uri.authority) {
            val path = uri.path
            if (!TextUtils.isEmpty(path)) {
                return File(path!!.replace("/root", ""))
            }
        }
        val cursor: Cursor? = context.contentResolver.query(
            uri, arrayOf("_data"), selection, selectionArgs, null
        )
        if (cursor == null) {
            Log.d("UriUtils", "$uri parse failed(cursor is null). -> $code")
            return null
        }
        return try {
            if (cursor.moveToFirst()) {
                val columnIndex: Int = cursor.getColumnIndex("_data")
                if (columnIndex > -1) {
                    File(cursor.getString(columnIndex))
                } else {
                    Log.d(
                        "UriUtils",
                        "$uri parse failed(columnIndex: $columnIndex is wrong). -> $code"
                    )
                    null
                }
            } else {
                Log.d("UriUtils", "$uri parse failed(moveToFirst return false). -> $code")
                null
            }
        } catch (e: java.lang.Exception) {
            Log.d("UriUtils", "$uri parse failed. -> $code")
            null
        } finally {
            cursor.close()
        }
    }

    private fun copyUri2Cache(uri: Uri): File? {
        Log.d("UriUtils", "copyUri2Cache() called")
        var `is`: InputStream? = null
        return try {
            `is` = context.contentResolver.openInputStream(uri)
            val file = File(context.cacheDir, "" + System.currentTimeMillis())
            FileIOUtils.writeFileFromIS(file.absolutePath, `is`)
            file
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            null
        } finally {
            if (`is` != null) {
                try {
                    `is`.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }

    /**
     * uri to input stream.
     *
     * @param uri The uri.
     * @return the input stream
     */
    fun uri2Bytes(uri: Uri?): ByteArray? {
        if (uri == null) return null
        var `is`: InputStream? = null
        return try {
            `is` = context.contentResolver.openInputStream(uri)
            FileIOUtils.is2Bytes(`is`)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            Log.d("UriUtils", "uri to bytes failed.")
            null
        } finally {
            if (`is` != null) {
                try {
                    `is`.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }

    /**
     * 获取 文件MimeType
     *
     * @param uri     uri
     * @return String MimeType
     */
    fun getMimeType(uri: Uri?): String? {
        return if (uri == null) {
            null
        } else context.contentResolver.getType(uri)
    }

    /**
     * 获取 Uri(根据mimeType)
     *
     * @param mimeType mimeType
     * @return Uri
     */
    fun getContentUri(mimeType: String): Uri? {
        return when {
            mimeType.startsWith("image") -> {
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            }
            mimeType.startsWith("video") -> {
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI
            }
            mimeType.startsWith("audio") -> {
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
            }
            else -> {
                MediaStore.Files.getContentUri("external")
            }
        }
    }

    /**
     * Android R只能通过uri删除图片
     */
    fun deleteUri(uri: Uri) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                context.contentResolver.delete(uri, null)
            }
        } catch (e: Exception) {
        }
    }
}