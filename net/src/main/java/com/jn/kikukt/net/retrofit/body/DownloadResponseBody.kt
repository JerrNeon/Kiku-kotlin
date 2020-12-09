package com.jn.kikukt.net.retrofit.body

import com.jn.kikukt.common.utils.log
import com.jn.kikukt.net.retrofit.callback.ProgressListener
import okhttp3.MediaType
import okhttp3.ResponseBody
import okio.*
import java.io.IOException

/**
 * Author：Stevie.Chen Time：2019/7/15
 * Class Comment：监听下载进度的ResponseBody
 */
class DownloadResponseBody(val responseBody: ResponseBody?, val listener: ProgressListener?) :
    ResponseBody() {

    private var bufferedSource: BufferedSource? = null

    override fun contentType(): MediaType? {
        return responseBody?.contentType()
    }

    override fun contentLength(): Long {
        return responseBody?.contentLength() ?: 0
    }

    override fun source(): BufferedSource {
        if (null == bufferedSource && responseBody != null) {
            bufferedSource = source(responseBody.source()).buffer()
        }
        return bufferedSource!!
    }

    private fun source(source: Source): Source {
        return object : ForwardingSource(source) {
            var totalBytesRead = 0L

            @Throws(IOException::class)
            override fun read(sink: Buffer, byteCount: Long): Long {
                try {
                    val bytesRead = super.read(sink, byteCount)
                    totalBytesRead += if (bytesRead != -1L) bytesRead else 0
                    listener?.onProgress(
                        totalBytesRead,
                        contentLength(),
                        totalBytesRead * 1.0f / (responseBody?.contentLength() ?: 1),
                        bytesRead == -1L
                    )
                    return bytesRead
                } catch (e: Exception) {
                    e.log()
                }
                return 0L
            }
        }
    }
}