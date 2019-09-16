package com.jn.kikukt.net.retrofit.body

import com.jn.kikukt.net.retrofit.callback.ProgressListener
import okhttp3.MediaType
import okhttp3.RequestBody
import okio.*
import java.io.IOException

/**
 * Author：Stevie.Chen Time：2019/7/15
 * Class Comment：监听上传进度的RequestBody
 */
class UploadRequestBody(val requestBody: RequestBody, val progressListener: ProgressListener?) : RequestBody() {

    override fun contentType(): MediaType? {
        return requestBody.contentType()
    }

    @Throws(IOException::class)
    override fun contentLength(): Long {
        return requestBody.contentLength()
    }

    @Throws(IOException::class)
    override fun writeTo(sink: BufferedSink) {
        val bufferedSink = sink(sink).buffer()
        //写入
        requestBody.writeTo(bufferedSink)
        //必须调用flush，否则最后一部分数据可能不会被写入
        bufferedSink.flush()
    }

    private fun sink(delegate: Sink): ForwardingSink {
        return object : ForwardingSink(delegate) {
            var totalBytesWrite = 0L

            @Throws(IOException::class)
            override fun write(source: Buffer, byteCount: Long) {
                super.write(source, byteCount)
                totalBytesWrite += byteCount
                progressListener?.onProgress(
                    totalBytesWrite,
                    requestBody.contentLength(),
                    totalBytesWrite * 1.0f / requestBody.contentLength(),
                    true
                )
            }
        }
    }

}