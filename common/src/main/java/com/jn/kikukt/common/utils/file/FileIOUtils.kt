package com.jn.kikukt.common.utils.file

import java.io.*
import java.nio.ByteBuffer
import java.nio.channels.FileChannel
import java.nio.charset.Charset
import java.util.*

/**
 * Author：Stevie.Chen Time：2019/7/15
 * Class Comment：文件相关
 */
object FileIOUtils {

    private var sBufferSize = 8192

    /**
     * Write file from input stream.
     *
     * @param filePath The path of file.
     * @param is       The input stream.
     * @return `true`: success<br></br>`false`: fail
     */
    fun writeFileFromIS(filePath: String, `is`: InputStream): Boolean {
        return writeFileFromIS(
            getFileByPath(
                filePath
            ), `is`, false
        )
    }

    /**
     * Write file from input stream.
     *
     * @param filePath The path of file.
     * @param is       The input stream.
     * @param append   True to append, false otherwise.
     * @return `true`: success<br></br>`false`: fail
     */
    fun writeFileFromIS(
        filePath: String,
        `is`: InputStream,
        append: Boolean
    ): Boolean {
        return writeFileFromIS(
            getFileByPath(
                filePath
            ), `is`, append
        )
    }

    /**
     * Write file from input stream.
     *
     * @param file The file.
     * @param is   The input stream.
     * @return `true`: success<br></br>`false`: fail
     */
    fun writeFileFromIS(file: File, `is`: InputStream): Boolean {
        return writeFileFromIS(file, `is`, false)
    }

    /**
     * Write file from input stream.
     *
     * @param file   The file.
     * @param is     The input stream.
     * @param append True to append, false otherwise.
     * @return `true`: success<br></br>`false`: fail
     */
    fun writeFileFromIS(
        file: File?,
        `is`: InputStream?,
        append: Boolean
    ): Boolean {
        if (!createOrExistsFile(file) || `is` == null) return false
        var os: OutputStream? = null
        try {
            os = BufferedOutputStream(FileOutputStream(file!!, append))
            val data = ByteArray(sBufferSize)
            var len: Int
            len = `is`.read(data, 0, sBufferSize)
            while (len != -1) {
                os.write(data, 0, len)
                len = `is`.read(data, 0, sBufferSize)
            }
            return true
        } catch (e: IOException) {
            e.printStackTrace()
            return false
        } finally {
            try {
                `is`.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }

            try {
                os?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
    }

    /**
     * Write file from bytes by stream.
     *
     * @param filePath The path of file.
     * @param bytes    The bytes.
     * @return `true`: success<br></br>`false`: fail
     */
    fun writeFileFromBytesByStream(filePath: String, bytes: ByteArray): Boolean {
        return writeFileFromBytesByStream(
            getFileByPath(
                filePath
            ), bytes, false
        )
    }

    /**
     * Write file from bytes by stream.
     *
     * @param filePath The path of file.
     * @param bytes    The bytes.
     * @param append   True to append, false otherwise.
     * @return `true`: success<br></br>`false`: fail
     */
    fun writeFileFromBytesByStream(
        filePath: String,
        bytes: ByteArray,
        append: Boolean
    ): Boolean {
        return writeFileFromBytesByStream(
            getFileByPath(
                filePath
            ), bytes, append
        )
    }

    /**
     * Write file from bytes by stream.
     *
     * @param file  The file.
     * @param bytes The bytes.
     * @return `true`: success<br></br>`false`: fail
     */
    fun writeFileFromBytesByStream(file: File, bytes: ByteArray): Boolean {
        return writeFileFromBytesByStream(file, bytes, false)
    }

    /**
     * Write file from bytes by stream.
     *
     * @param file   The file.
     * @param bytes  The bytes.
     * @param append True to append, false otherwise.
     * @return `true`: success<br></br>`false`: fail
     */
    fun writeFileFromBytesByStream(
        file: File?,
        bytes: ByteArray?,
        append: Boolean
    ): Boolean {
        if (bytes == null || !createOrExistsFile(file)) return false
        var bos: BufferedOutputStream? = null
        try {
            bos = BufferedOutputStream(FileOutputStream(file!!, append))
            bos.write(bytes)
            return true
        } catch (e: IOException) {
            e.printStackTrace()
            return false
        } finally {
            try {
                bos?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
    }

    /**
     * Write file from bytes by channel.
     *
     * @param filePath The path of file.
     * @param bytes    The bytes.
     * @param isForce  是否写入文件
     * @return `true`: success<br></br>`false`: fail
     */
    fun writeFileFromBytesByChannel(
        filePath: String,
        bytes: ByteArray,
        isForce: Boolean
    ): Boolean {
        return writeFileFromBytesByChannel(
            getFileByPath(
                filePath
            ), bytes, false, isForce
        )
    }

    /**
     * Write file from bytes by channel.
     *
     * @param filePath The path of file.
     * @param bytes    The bytes.
     * @param append   True to append, false otherwise.
     * @param isForce  True to force write file, false otherwise.
     * @return `true`: success<br></br>`false`: fail
     */
    fun writeFileFromBytesByChannel(
        filePath: String,
        bytes: ByteArray,
        append: Boolean,
        isForce: Boolean
    ): Boolean {
        return writeFileFromBytesByChannel(
            getFileByPath(
                filePath
            ), bytes, append, isForce
        )
    }

    /**
     * Write file from bytes by channel.
     *
     * @param file    The file.
     * @param bytes   The bytes.
     * @param isForce True to force write file, false otherwise.
     * @return `true`: success<br></br>`false`: fail
     */
    fun writeFileFromBytesByChannel(
        file: File,
        bytes: ByteArray,
        isForce: Boolean
    ): Boolean {
        return writeFileFromBytesByChannel(file, bytes, false, isForce)
    }

    /**
     * Write file from bytes by channel.
     *
     * @param file    The file.
     * @param bytes   The bytes.
     * @param append  True to append, false otherwise.
     * @param isForce True to force write file, false otherwise.
     * @return `true`: success<br></br>`false`: fail
     */
    fun writeFileFromBytesByChannel(
        file: File?,
        bytes: ByteArray?,
        append: Boolean,
        isForce: Boolean
    ): Boolean {
        if (bytes == null) return false
        var fc: FileChannel? = null
        try {
            fc = FileOutputStream(file!!, append).channel
            fc!!.position(fc.size())
            fc.write(ByteBuffer.wrap(bytes))
            if (isForce) fc.force(true)
            return true
        } catch (e: IOException) {
            e.printStackTrace()
            return false
        } finally {
            try {
                fc?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
    }

    /**
     * Write file from bytes by map.
     *
     * @param filePath The path of file.
     * @param bytes    The bytes.
     * @param isForce  True to force write file, false otherwise.
     * @return `true`: success<br></br>`false`: fail
     */
    fun writeFileFromBytesByMap(
        filePath: String,
        bytes: ByteArray,
        isForce: Boolean
    ): Boolean {
        return writeFileFromBytesByMap(filePath, bytes, false, isForce)
    }

    /**
     * Write file from bytes by map.
     *
     * @param filePath The path of file.
     * @param bytes    The bytes.
     * @param append   True to append, false otherwise.
     * @param isForce  True to force write file, false otherwise.
     * @return `true`: success<br></br>`false`: fail
     */
    fun writeFileFromBytesByMap(
        filePath: String,
        bytes: ByteArray,
        append: Boolean,
        isForce: Boolean
    ): Boolean {
        return writeFileFromBytesByMap(
            getFileByPath(
                filePath
            ), bytes, append, isForce
        )
    }

    /**
     * Write file from bytes by map.
     *
     * @param file    The file.
     * @param bytes   The bytes.
     * @param isForce True to force write file, false otherwise.
     * @return `true`: success<br></br>`false`: fail
     */
    fun writeFileFromBytesByMap(
        file: File,
        bytes: ByteArray,
        isForce: Boolean
    ): Boolean {
        return writeFileFromBytesByMap(file, bytes, false, isForce)
    }

    /**
     * Write file from bytes by map.
     *
     * @param file    The file.
     * @param bytes   The bytes.
     * @param append  True to append, false otherwise.
     * @param isForce True to force write file, false otherwise.
     * @return `true`: success<br></br>`false`: fail
     */
    fun writeFileFromBytesByMap(
        file: File?,
        bytes: ByteArray?,
        append: Boolean,
        isForce: Boolean
    ): Boolean {
        if (bytes == null || !createOrExistsFile(file)) return false
        var fc: FileChannel? = null
        try {
            fc = FileOutputStream(file!!, append).channel
            val mbb = fc!!.map(FileChannel.MapMode.READ_WRITE, fc.size(), bytes.size.toLong())
            mbb.put(bytes)
            if (isForce) mbb.force()
            return true
        } catch (e: IOException) {
            e.printStackTrace()
            return false
        } finally {
            try {
                fc?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
    }

    /**
     * Write file from string.
     *
     * @param filePath The path of file.
     * @param content  The string of content.
     * @return `true`: success<br></br>`false`: fail
     */
    fun writeFileFromString(filePath: String, content: String): Boolean {
        return writeFileFromString(
            getFileByPath(
                filePath
            ), content, false
        )
    }

    /**
     * Write file from string.
     *
     * @param filePath The path of file.
     * @param content  The string of content.
     * @param append   True to append, false otherwise.
     * @return `true`: success<br></br>`false`: fail
     */
    fun writeFileFromString(
        filePath: String,
        content: String,
        append: Boolean
    ): Boolean {
        return writeFileFromString(
            getFileByPath(
                filePath
            ), content, append
        )
    }

    /**
     * Write file from string.
     *
     * @param file    The file.
     * @param content The string of content.
     * @return `true`: success<br></br>`false`: fail
     */
    fun writeFileFromString(file: File, content: String): Boolean {
        return writeFileFromString(file, content, false)
    }

    /**
     * Write file from string.
     *
     * @param file    The file.
     * @param content The string of content.
     * @param append  True to append, false otherwise.
     * @return `true`: success<br></br>`false`: fail
     */
    fun writeFileFromString(
        file: File?,
        content: String?,
        append: Boolean
    ): Boolean {
        if (file == null || content == null) return false
        if (!createOrExistsFile(file)) return false
        var bw: BufferedWriter? = null
        try {
            bw = BufferedWriter(FileWriter(file, append))
            bw.write(content)
            return true
        } catch (e: IOException) {
            e.printStackTrace()
            return false
        } finally {
            try {
                bw?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // the divide line of write and read
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Return the lines in file.
     *
     * @param filePath The path of file.
     * @return the lines in file
     */
    fun readFile2List(filePath: String): List<String>? {
        return readFile2List(
            getFileByPath(
                filePath
            ), null
        )
    }

    /**
     * Return the lines in file.
     *
     * @param filePath    The path of file.
     * @param charsetName The name of charset.
     * @return the lines in file
     */
    fun readFile2List(filePath: String, charsetName: String): List<String>? {
        return readFile2List(
            getFileByPath(
                filePath
            ), charsetName
        )
    }

    /**
     * Return the lines in file.
     *
     * @param file The file.
     * @return the lines in file
     */
    fun readFile2List(file: File): List<String>? {
        return readFile2List(file, 0, 0x7FFFFFFF, null)
    }

    /**
     * Return the lines in file.
     *
     * @param file        The file.
     * @param charsetName The name of charset.
     * @return the lines in file
     */
    fun readFile2List(file: File?, charsetName: String?): List<String>? {
        return readFile2List(file, 0, 0x7FFFFFFF, charsetName)
    }

    /**
     * Return the lines in file.
     *
     * @param filePath The path of file.
     * @param st       The line's index of start.
     * @param end      The line's index of end.
     * @return the lines in file
     */
    fun readFile2List(filePath: String, st: Int, end: Int): List<String>? {
        return readFile2List(
            getFileByPath(
                filePath
            ), st, end, null
        )
    }

    /**
     * Return the lines in file.
     *
     * @param filePath    The path of file.
     * @param st          The line's index of start.
     * @param end         The line's index of end.
     * @param charsetName The name of charset.
     * @return the lines in file
     */
    fun readFile2List(
        filePath: String,
        st: Int,
        end: Int,
        charsetName: String
    ): List<String>? {
        return readFile2List(
            getFileByPath(
                filePath
            ), st, end, charsetName
        )
    }

    /**
     * Return the lines in file.
     *
     * @param file The file.
     * @param st   The line's index of start.
     * @param end  The line's index of end.
     * @return the lines in file
     */
    fun readFile2List(file: File, st: Int, end: Int): List<String>? {
        return readFile2List(file, st, end, null)
    }

    /**
     * Return the lines in file.
     *
     * @param file        The file.
     * @param st          The line's index of start.
     * @param end         The line's index of end.
     * @param charsetName The name of charset.
     * @return the lines in file
     */
    fun readFile2List(
        file: File?,
        st: Int,
        end: Int,
        charsetName: String?
    ): List<String>? {
        if (!isFileExists(file)) return null
        if (st > end) return null
        var reader: BufferedReader? = null
        try {
            var line: String?
            var curLine = 1
            val list = ArrayList<String>()
            reader = if (isSpace(charsetName)) {
                BufferedReader(InputStreamReader(FileInputStream(file!!)))
            } else {
                BufferedReader(
                    InputStreamReader(FileInputStream(file!!), charsetName!!)
                )
            }
            line = reader.readLine()
            while (line != null) {
                if (curLine > end) break
                if (curLine in st..end) list.add(line)
                ++curLine
                line = reader.readLine()
            }
            return list
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        } finally {
            try {
                reader?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
    }

    /**
     * Return the string in file.
     *
     * @param filePath The path of file.
     * @return the string in file
     */
    fun readFile2String(filePath: String): String? {
        return readFile2String(
            getFileByPath(
                filePath
            ), null
        )
    }

    /**
     * Return the string in file.
     *
     * @param filePath    The path of file.
     * @param charsetName The name of charset.
     * @return the string in file
     */
    fun readFile2String(filePath: String, charsetName: String): String? {
        return readFile2String(
            getFileByPath(
                filePath
            ), charsetName
        )
    }

    /**
     * Return the string in file.
     *
     * @param file The file.
     * @return the string in file
     */
    fun readFile2String(file: File): String? {
        return readFile2String(file, null)
    }

    /**
     * Return the string in file.
     *
     * @param file        The file.
     * @param charsetName The name of charset.
     * @return the string in file
     */
    fun readFile2String(file: File?, charsetName: String?): String? {
        val bytes = readFile2BytesByStream(file) ?: return null
        return if (isSpace(charsetName)) {
            String(bytes)
        } else {
            try {
                String(bytes, Charset.forName(charsetName!!))
            } catch (e: UnsupportedEncodingException) {
                e.printStackTrace()
                ""
            }

        }
    }

    /**
     * Return the bytes in file by stream.
     *
     * @param filePath The path of file.
     * @return the bytes in file
     */
    fun readFile2BytesByStream(filePath: String): ByteArray? {
        return readFile2BytesByStream(
            getFileByPath(
                filePath
            )
        )
    }

    /**
     * Return the bytes in file by stream.
     *
     * @param file The file.
     * @return the bytes in file
     */
    fun readFile2BytesByStream(file: File?): ByteArray? {
        if (!isFileExists(file)) return null
        try {
            return is2Bytes(FileInputStream(file!!))
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            return null
        }

    }

    /**
     * Return the bytes in file by channel.
     *
     * @param filePath The path of file.
     * @return the bytes in file
     */
    fun readFile2BytesByChannel(filePath: String): ByteArray? {
        return readFile2BytesByChannel(
            getFileByPath(
                filePath
            )
        )
    }

    /**
     * Return the bytes in file by channel.
     *
     * @param file The file.
     * @return the bytes in file
     */
    fun readFile2BytesByChannel(file: File?): ByteArray? {
        if (!isFileExists(file)) return null
        var fc: FileChannel? = null
        try {
            fc = RandomAccessFile(file!!, "r").channel
            val byteBuffer = ByteBuffer.allocate(fc!!.size().toInt())
            while (true) {
                if (fc.read(byteBuffer) <= 0) break
            }
            return byteBuffer.array()
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        } finally {
            try {
                fc?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
    }

    /**
     * Return the bytes in file by map.
     *
     * @param filePath The path of file.
     * @return the bytes in file
     */
    fun readFile2BytesByMap(filePath: String): ByteArray? {
        return readFile2BytesByMap(
            getFileByPath(
                filePath
            )
        )
    }

    /**
     * Return the bytes in file by map.
     *
     * @param file The file.
     * @return the bytes in file
     */
    fun readFile2BytesByMap(file: File?): ByteArray? {
        if (!isFileExists(file)) return null
        var fc: FileChannel? = null
        try {
            fc = RandomAccessFile(file!!, "r").channel
            val size = fc!!.size().toInt()
            val mbb = fc.map(FileChannel.MapMode.READ_ONLY, 0, size.toLong()).load()
            val result = ByteArray(size)
            mbb.get(result, 0, size)
            return result
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        } finally {
            try {
                fc?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
    }

    /**
     * Set the buffer's size.
     *
     * Default size equals 8192 bytes.
     *
     * @param bufferSize The buffer's size.
     */
    fun setBufferSize(bufferSize: Int) {
        sBufferSize = bufferSize
    }

    private fun getFileByPath(filePath: String): File? {
        return if (isSpace(filePath)) null else File(filePath)
    }

    private fun createOrExistsFile(filePath: String): Boolean {
        return createOrExistsFile(
            getFileByPath(
                filePath
            )
        )
    }

    private fun createOrExistsFile(file: File?): Boolean {
        if (file == null) return false
        if (file.exists()) return file.isFile
        if (!createOrExistsDir(file.parentFile)) return false
        try {
            return file.createNewFile()
        } catch (e: IOException) {
            e.printStackTrace()
            return false
        }

    }

    private fun createOrExistsDir(file: File?): Boolean {
        return file != null && if (file.exists()) file.isDirectory else file.mkdirs()
    }

    private fun isFileExists(file: File?): Boolean {
        return file != null && file.exists()
    }

    private fun isSpace(s: String?): Boolean {
        if (s == null) return true
        var i = 0
        val len = s.length
        while (i < len) {
            if (!Character.isWhitespace(s[i])) {
                return false
            }
            ++i
        }
        return true
    }

    private fun is2Bytes(`is`: InputStream?): ByteArray? {
        if (`is` == null) return null
        var os: ByteArrayOutputStream? = null
        try {
            os = ByteArrayOutputStream()
            val b = ByteArray(sBufferSize)
            var len: Int
            len = `is`.read(b, 0, sBufferSize)
            while (len != -1) {
                os.write(b, 0, len)
                len = `is`.read(b, 0, sBufferSize)
            }
            return os.toByteArray()
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        } finally {
            try {
                `is`.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }

            try {
                os?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
    }
}