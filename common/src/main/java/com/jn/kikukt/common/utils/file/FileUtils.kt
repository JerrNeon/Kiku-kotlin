package com.jn.kikukt.common.utils.file

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Matrix
import android.media.ExifInterface
import android.os.Environment
import android.widget.ScrollView
import androidx.annotation.ColorInt
import com.jn.kikukt.common.utils.ContextUtils
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
    private const val IMAGE_COMPRESSTEMP_FILENAME = "temp"//图片压缩临时文件名

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

    /**
     * 获取图片压缩临时路径
     *
     * @return
     */
    fun getImageCompressTempCachePath(): String {
        return getImageCachePath(IMAGE_COMPRESSTEMP_FILENAME)
    }

    /**
     * 获取图片缓存路径
     *
     * @param imageName 图片名
     * @return 图片路径
     */
    fun getImageCachePath(imageName: String): String {
        val path = getImageCacheFile().absolutePath + File.separator + imageName + ".png"
        val file = File(path)
        file.deleteOnExit()
        return path
    }

    /**
     * 获取视频缓存路径
     *
     * @return
     */
    fun getVideoCachePath(): String {
        return getVideoCacheFile().absolutePath
    }

    /**
     * 获取异常信息路径
     *
     * @param logName 文件名
     * @return log文件
     */
    fun getCrashPath(logName: String): String {
        return getCrashFile().absolutePath + File.separator + logName + ".txt"
    }

    /**
     * 获取图片缓存目录
     *
     * @return File
     */
    fun getImageCacheFile(): File {
        val result = File(
            getCacheRootFile()?.absoluteFile.toString() + "/" + APP_CACHE_DIR + "/" + IMAGE_CACHE_DIR
        )
        if (!result.exists())
            result.mkdirs()
        return result
    }

    /**
     * 获取视频缓存目录
     *
     * @return
     */
    fun getVideoCacheFile(): File {
        val result = File(
            getCacheRootFile()?.absoluteFile.toString() + "/" + APP_CACHE_DIR + "/" + VIDEO_CACHE_DIR
        )
        if (!result.exists())
            result.mkdirs()
        return result
    }

    /**
     * 获取文件缓存目录
     *
     * @return File
     */
    fun getFileCacheFile(): File {
        val result = File(
            getCacheRootFile()?.absoluteFile.toString() + "/" + APP_CACHE_DIR + "/" + FILE_CACHE_DIR
        )
        if (!result.exists())
            result.mkdirs()
        return result
    }

    /**
     * 获取异常信息目录
     *
     * @return File
     */
    fun getCrashFile(): File {
        val result = File(
            getCacheRootFile()?.absoluteFile.toString() + "/" + APP_CACHE_DIR + "/" + CRASH_CACHE_DIR
        )
        if (!result.exists())
            result.mkdirs()
        return result
    }

    /**
     * 获取缓存根目录
     */
    fun getCacheRootFile(): File? {
        return getCacheRootFile(ContextUtils.application)
    }

    /**
     * 获取缓存根目录
     *
     *
     * 有SDcard放在/mnt/sdcard/Android目录下
     * 没有sdcard 放在 data/data/packagename/file 目录下
     *
     *
     * @param application Application
     * @return
     */
    fun getCacheRootFile(application: Application): File? {
        var result: File?
        result = if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {// SD卡已经挂载
            application.getExternalFilesDir(null)
        } else {
            application.filesDir
        }
        if (result == null)
            result = application.filesDir
        return result
    }

    /**
     * 压缩图片
     *
     * @param filePath   原图片路径
     * @param targetPath 目标图片路径
     */
    fun compressImage(filePath: String, targetPath: String): String {
        return compressImage(filePath, targetPath, 80)
    }

    /**
     * 压缩图片
     *
     * @param filePath   原图片路径
     * @param targetPath 目标图片路径
     * @param quality    质量
     */
    fun compressImage(filePath: String, targetPath: String, quality: Int): String {
        var bm: Bitmap? = getSmallBitmap(filePath)//获取一定尺寸的图片
        val degree = readPictureDegree(filePath)//获取相片拍摄角度
        if (degree != 0) {//旋转照片角度，防止头像横着显示
            bm = rotateBitmap(bm, degree)
        }
        val outputFile = File(targetPath)
        try {
            if (!outputFile.exists()) {
                outputFile.parentFile?.mkdirs()
                //outputFile.createNewFile();
            } else {
                outputFile.delete()
            }
            val out = FileOutputStream(outputFile)
            bm!!.compress(Bitmap.CompressFormat.JPEG, quality, out)
        } catch (e: Exception) {
        }

        return outputFile.path
    }

    /**
     * 根据路径获得图片信息并按比例压缩，返回bitmap
     */
    fun getSmallBitmap(filePath: String): Bitmap {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true//只解析图片边沿，获取宽高
        BitmapFactory.decodeFile(filePath, options)
        // 计算缩放比
        options.inSampleSize = calculateInSampleSize(options, 800, 800)
        // 完整解析图片返回bitmap
        options.inJustDecodeBounds = false
        return BitmapFactory.decodeFile(filePath, options)
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
     */
    fun saveLocalResourceFile(resourceId: Int, fileName: String): String? {
        return saveLocalResourceFile(ContextUtils.context, resourceId, fileName)
    }

    /**
     * 保存本地资源文件到手机中
     *
     * @param context
     * @param resourceId
     * @param fileName
     * @return
     */
    fun saveLocalResourceFile(context: Context, resourceId: Int, fileName: String): String? {
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
            val path = getImageCachePath(fileName)
            val outputFile = File(getImageCachePath(fileName))
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

    /**
     * 清除用户上传图片时保存的临时文件
     */
    fun clearTempImage() {
        if (getImageCacheFile().exists())
            deleteAllFiles(getImageCacheFile())
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
    fun getToDayStrAsFileName(): String {
        val sdf = SimpleDateFormat("yyyyMMddHHmmssSSS", Locale.getDefault())
        return sdf.format(Date())
    }

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
}