package com.jn.kikukt.retrofit.callback

/**
 * Author：Stevie.Chen Time：2019/7/15
 * Class Comment：进度监听
 */
interface ProgressListener {

    /**
     * @param progressBytes        已经下载或上传字节数
     * @param totalBytes           总字节数
     * @param progressPercent 进度值
     * @param done            是否完成
     */
    fun onProgress(progressBytes: Long, totalBytes: Long, progressPercent: Float, done: Boolean)
}