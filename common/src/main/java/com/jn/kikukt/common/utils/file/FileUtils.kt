package com.jn.kikukt.common.utils.file

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.widget.ScrollView
import androidx.annotation.ColorInt
import com.jn.kikukt.common.utils.ContextUtils
import com.jn.kikukt.common.utils.UriUtils
import com.jn.kikukt.common.utils.log
import java.io.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt


/**
 * Author：Stevie.Chen Time：2019/7/15
 * Class Comment：文件操作类
 */
object FileUtils {

    private const val APP_CACHE_DIR = "cache"//应用缓存根目录
    private const val IMAGE_CACHE_DIR = "image"//图片缓存目录
    private const val VIDEO_CACHE_DIR = "video"//视频缓存目录
    private const val FILE_CACHE_DIR = "file"//文件缓存目录
    private const val CRASH_CACHE_DIR = "crash"//异常信息目录

    //MIME (Multipurpose Internet Mail Extensions) 是描述消息内容类型的因特网标准。
    private val MIME_TYPE_MAP = HashMap<String, String>()
    private val FILE_TYPE_MAP = HashMap<String, String>()

    init {
        MIME_TYPE_MAP["application/vnd.android.package-archive"] = "apk"
        MIME_TYPE_MAP["application/pdf"] = "pdf"
        MIME_TYPE_MAP["application/vnd.ms-excel"] = "xls"
        MIME_TYPE_MAP["application/vnd.ms-powerpoint"] = "ppt"
        MIME_TYPE_MAP["application/vnd.ms-works"] = "wps"
        MIME_TYPE_MAP["application/zip"] = "zip"
        MIME_TYPE_MAP["audio/mpeg"] = "mp3"
        MIME_TYPE_MAP["audio/x-wav"] = "wav"
        MIME_TYPE_MAP["image/bmp"] = "bmp"
        MIME_TYPE_MAP["image/gif"] = "gif"
        MIME_TYPE_MAP["image/jpeg"] = "jpg"
        MIME_TYPE_MAP["image/png"] = "png"
        MIME_TYPE_MAP["text/css"] = "css"
        MIME_TYPE_MAP["text/html"] = "html"
        MIME_TYPE_MAP["video/x-msvideo"] = "avi"

        FILE_TYPE_MAP["apk"] = "application/vnd.android.package-archive"
        FILE_TYPE_MAP["pdf"] = "application/pdf"
        FILE_TYPE_MAP["xls"] = "application/vnd.ms-excel"
        FILE_TYPE_MAP["ppt"] = "application/vnd.ms-powerpoint"
        FILE_TYPE_MAP["wps"] = "application/vnd.ms-works"
        FILE_TYPE_MAP["zip"] = "application/zip"
        FILE_TYPE_MAP["mp3"] = "audio/mpeg"
        FILE_TYPE_MAP["wav"] = "audio/x-wav"
        FILE_TYPE_MAP["bmp"] = "image/bmp"
        FILE_TYPE_MAP["gif"] = "image/gif"
        FILE_TYPE_MAP["jpg"] = "image/jpeg"
        FILE_TYPE_MAP["png"] = "image/png"
        FILE_TYPE_MAP["css"] = "text/css"
        FILE_TYPE_MAP["html"] = "text/html"
        FILE_TYPE_MAP["avi"] = "video/x-msvideo"
    }

    //获取存储根目录
    //有SDCard放在/mnt/sdcard/Android目录下
    //没有sdcard 放在 data/data/packageName/file 目录下
    val rootDir: File
        get() {
            val application = ContextUtils.context
            return if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {// SD卡已经挂载
                application.getExternalFilesDir(null) ?: application.filesDir
            } else {
                application.filesDir
            }
        }

    val rootPath: String
        get() = rootDir.absoluteFile.toString()
    val cacheDir: File
        get() = File("$rootPath${File.separator}$APP_CACHE_DIR").apply {
            if (!exists())
                mkdirs()
        }
    val cachePath: String
        get() = cacheDir.absolutePath
    val imageDir: File
        get() = File("$cachePath${File.separator}$IMAGE_CACHE_DIR").apply {
            if (!exists())
                mkdirs()
        }
    val imagePath: String
        get() = imageDir.absolutePath
    val videoDir: File
        get() = File("$cachePath${File.separator}$VIDEO_CACHE_DIR").apply {
            if (!exists())
                mkdirs()
        }
    val videoPath: String
        get() = videoDir.absolutePath
    val fileDir: File
        get() = File("$cachePath${File.separator}$FILE_CACHE_DIR").apply {
            if (!exists())
                mkdirs()
        }
    val filePath: String
        get() = fileDir.absolutePath
    val crashDir: File
        get() = File("$cachePath${File.separator}$CRASH_CACHE_DIR").apply {
            if (!exists())
                mkdirs()
        }
    val crashPath: String
        get() = crashDir.absolutePath

    fun getImagePath(fileName: String): String = "$imagePath${File.separator}$fileName.png"
    fun getCrashPath(fileName: String): String = "$crashPath${File.separator}$fileName.txt"

    /**
     * 清除缓存目录中的文件
     */
    fun clearCacheDir() {
        val cache = cacheDir
        if (cache.exists())
            deleteAllFiles(cache)
    }

    /**
     * 压缩图片
     *
     * @param filePath   原图片路径
     * @param targetPath 目标图片路径
     * @param quality    质量
     */
    fun compressImage(filePath: String, targetPath: String, quality: Int = 80): String {
        var bm: Bitmap? = getSmallBitmap(filePath)//获取一定尺寸的图片
        val degree = readPictureDegree(filePath)//获取相片拍摄角度
        if (degree != 0) {//旋转照片角度，防止头像横着显示
            bm = rotateBitmap(bm, degree)
        }
        return try {
            val outputFile = File(targetPath)
            if (!outputFile.exists()) {
                outputFile.parentFile?.mkdirs()
                //outputFile.createNewFile();
            } else {
                outputFile.delete()
            }
            val out = FileOutputStream(outputFile)
            bm?.compress(Bitmap.CompressFormat.JPEG, quality, out)
            out.close()
            if (outputFile.exists()) outputFile.path else filePath
        } catch (e: Exception) {
            filePath
        }
    }

    /**
     * 压缩图片
     *
     * @param fileUri   原图片路径
     * @param targetPath 目标图片路径
     * @param quality    质量
     */
    fun compressImage(fileUri: Uri, targetPath: String, quality: Int = 80): String {
        val filePath = UriUtils.getPathFromUri(fileUri) ?: ""
        var bm: Bitmap? =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                val usableUri = UriUtils.getUri(filePath)
                if (usableUri != null) getSmallBitmap(usableUri) else null
            } else {
                getSmallBitmap(filePath)
            }//获取一定尺寸的图片
        val degree = readPictureDegree(filePath)//获取相片拍摄角度
        if (degree != 0) {//旋转照片角度，防止头像横着显示
            bm = rotateBitmap(bm, degree)
        }
        return try {
            val outputFile = File(targetPath)
            if (!outputFile.exists()) {
                outputFile.parentFile?.mkdirs()
                //outputFile.createNewFile();
            } else {
                outputFile.delete()
            }
            val out = FileOutputStream(outputFile)
            bm?.compress(Bitmap.CompressFormat.JPEG, quality, out)
            out.close()
            if (outputFile.exists()) outputFile.path else filePath
        } catch (e: Exception) {
            filePath
        }
    }

    /**
     * 根据路径获得图片信息并按比例压缩，返回bitmap
     */
    fun getSmallBitmap(filePath: String): Bitmap? {
        return try {
            val options = BitmapFactory.Options()
            options.inJustDecodeBounds = true//只解析图片边沿，获取宽高
            BitmapFactory.decodeFile(filePath, options)
            // 计算缩放比
            options.inSampleSize = calculateInSampleSize(options, 800, 800)
            // 完整解析图片返回bitmap
            options.inJustDecodeBounds = false
            return BitmapFactory.decodeFile(filePath, options)
        } catch (e: Exception) {
            e.log()
            null
        }
    }

    /**
     * 根据路径获得图片信息并按比例压缩，返回bitmap
     */
    fun getSmallBitmap(fileUri: Uri): Bitmap? {
        return try {
            val options = BitmapFactory.Options()
            options.inJustDecodeBounds = true//只解析图片边沿，获取宽高
            BitmapFactory.decodeFile(filePath, options)
            // 计算缩放比
            options.inSampleSize = calculateInSampleSize(options, 800, 800)
            // 完整解析图片返回bitmap
            options.inJustDecodeBounds = false
            val inputStream = ContextUtils.context.contentResolver.openInputStream(fileUri)
            BitmapFactory.decodeStream(inputStream, null, options)
        } catch (e: Exception) {
            e.log()
            null
        }
    }

    /**
     * 计算图片的缩放值
     *
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
        // Raw height and width of image
        val height = options.outHeight
        val width = options.outWidth
        var inSampleSize = 1
        if (height > reqHeight || width > reqWidth) {
            // Calculate ratios of height and width to requested height and
            // width
            val heightRatio = (height.toFloat() / reqHeight.toFloat()).roundToInt()
            val widthRatio = (width.toFloat() / reqWidth.toFloat()).roundToInt()
            // Choose the smallest ratio as inSampleSize value, this will
            // guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = if (heightRatio < widthRatio) heightRatio else widthRatio
        }
        return inSampleSize
    }

    /**
     * 获取照片角度
     *
     * @param path
     * @return
     */
    fun readPictureDegree(path: String): Int {
        var degree = 0
        try {
            val exifInterface = ExifInterface(path)
            val orientation = exifInterface.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_NORMAL
            )
            when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> degree = 90
                ExifInterface.ORIENTATION_ROTATE_180 -> degree = 180
                ExifInterface.ORIENTATION_ROTATE_270 -> degree = 270
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return degree
    }

    /**
     * 旋转照片
     */
    fun rotateBitmap(bm: Bitmap?, degress: Int): Bitmap? {
        var bitmap = bm
        if (bitmap != null) {
            val m = Matrix()
            m.postRotate(degress.toFloat())
            bitmap = Bitmap.createBitmap(
                bitmap, 0, 0, bitmap.width,
                bitmap.height, m, true
            )
            return bitmap
        }
        return bitmap
    }

    /**
     * 保存本地资源文件到手机中
     *
     * @param context
     * @param resourceId
     * @param fileName
     * @return
     */
    fun saveLocalResourceFile(
        resourceId: Int,
        fileName: String,
        context: Context = ContextUtils.context
    ): String? {
        val bitmap = BitmapFactory.decodeStream(context.resources.openRawResource(resourceId))
        return saveBitmap(bitmap, fileName)
    }

    /**
     * 保存Bitmap到手机中
     *
     * @param bitmap
     * @param fileName
     * @return
     */
    fun saveBitmap(bitmap: Bitmap, fileName: String): String? {
        return try {
            val path = getImagePath(fileName)
            val outputFile = File(getImagePath(fileName))
            if (!outputFile.exists()) {
                outputFile.parentFile?.mkdirs()
            } else {
                outputFile.delete()
            }
            val out = FileOutputStream(outputFile)
            bitmap.compress(Bitmap.CompressFormat.PNG, 80, out)
            path
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            null
        }

    }

    /**
     * 保存Scrollview中的内容到手机中
     *
     * @param scrollView      ScrollView
     * @param backgroundColor 图片背景色
     * @param fileName        图片名
     * @return
     */
    fun saveBitmap(
        scrollView: ScrollView,
        @ColorInt backgroundColor: Int,
        fileName: String
    ): String? {
        var height = 0
        val bitmap: Bitmap
        for (i in 0 until scrollView.childCount) {
            height += scrollView.getChildAt(i).height
        }
        bitmap = Bitmap.createBitmap(scrollView.width, height, Bitmap.Config.RGB_565)
        val canvas = Canvas(bitmap)
        canvas.drawColor(backgroundColor)
        scrollView.draw(canvas)
        return saveBitmap(bitmap, fileName)
    }

    fun makeDir(dir: File?) {
        if (dir?.parentFile?.exists() != true) {
            makeDir(dir?.parentFile)
        }
        dir?.mkdir()
    }

    /**
     * 方法描述：将系统限定的路径转换为绝对正确的路径
     */
    fun getGeneralFilePath(oriPath: String?): String? {
        var originalPath = oriPath
        if (null != originalPath && "" != originalPath) {
            val strPath =
                originalPath.split("\\\\|/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            originalPath = ""
            // 拼接jar路径
            for (i in strPath.indices) {
                if ("" != strPath[i] && "" != strPath[i].trim { it <= ' ' }) {
                    originalPath = originalPath!! + strPath[i].trim { it <= ' ' }
                    if (i < strPath.size - 1) {
                        originalPath += File.separator
                    }
                }
            }
        }
        return originalPath
    }

    /**
     * 复制文件(以超快的速度复制文件)
     *
     * @param srcFile  源文件File
     * @param destFile 目标目录File
     * @return 实际复制的字节数，如果文件、目录不存在、文件为null或者发生IO异常，返回-1
     */
    @Throws(Exception::class)
    fun copyFile(srcFile: File, destFile: File): Long {
        val copySizes: Long
        val fcin = FileInputStream(srcFile).channel
        val fcout = FileOutputStream(destFile).channel
        val size = fcin.size()
        fcin.transferTo(0, fcin.size(), fcout)
        fcin.close()
        fcout.close()
        copySizes = size
        return copySizes
    }

    /**
     * 获取当前时间作为文件名称
     */
    fun getToDayStrAsFileName(): String =
        SimpleDateFormat("yyyyMMddHHmmssSSS", Locale.getDefault()).format(Date())

    /**
     * 删除单个文件
     *
     * @param fileName 被删除文件的文件名
     * @return 单个文件删除成功返回true, 否则返回false
     */
    fun deleteFile(fileName: String): Boolean {
        val file = File(fileName)
        return if (file.isFile && file.exists()) {
            file.delete()
            println("删除单个文件" + fileName + "成功！")
            true
        } else {
            println("删除单个文件" + fileName + "失败！")
            false
        }
    }

    /**
     * 删除指定目录下的所有文件
     *
     * @param root
     */
    fun deleteAllFiles(root: File) {
        val files = root.listFiles()
        if (files != null)
            for (f in files) {
                if (f.isDirectory) { // 判断是否为文件夹
                    deleteAllFiles(f)
                    try {
                        f.delete()
                    } catch (e: Exception) {
                    }

                } else {
                    if (f.exists()) { // 判断是否存在
                        deleteAllFiles(f)
                        try {
                            f.delete()
                        } catch (e: Exception) {
                        }

                    }
                }
            }
    }

    /**
     * 获取文件路径获取文件后缀
     *
     * @param filePath
     * @return
     */
    fun getFileSuffix(filePath: String): String {
        return try {
            val file = File(filePath)
            val fileName = file.name
            fileName.substring(fileName.lastIndexOf("") + 1)
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }

    /**
     * 根据文件MimeType获取文件后缀名
     *
     * @param mimeType 描述消息内容类型的因特网标准。
     * @return
     */
    fun getFileSuffixByMimeType(mimeType: String): String? {
        return MIME_TYPE_MAP[mimeType]
    }

    /**
     * 获取MimeType
     *
     * @param filePath 文件路径
     * @return
     */
    fun getFileMimeType(filePath: String): String? {
        return MIME_TYPE_MAP[getFileSuffix(filePath).replace("", "")]
    }

    /**
     * 删除
     *
     * 注意：
     * 只适用于应用自身创建的媒体文件；
     * 文档等其他类型文件无法删除，其他App的文件也无法删除成功，只能删除媒体库里的Uri数据，实际文件并没有删除。
     * 操作其他App的数据需要用户授予权限，catch RecoverableSecurityException 异常，然后请求权限，具体见官方文档。
     *
     * 访问共享存储空间中的媒体文件：https://developer.android.com/training/data-storage/shared/media#remove-item
     *
     * @param context context
     * @param uri     uri
     * @return boolean
     */
    fun delete(uri: Uri, context: Context = ContextUtils.context): Boolean {
        var delete = false
        val contentResolver = context.contentResolver
        try {
            delete = contentResolver.delete(uri, null, null) > 0
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return delete
    }
}