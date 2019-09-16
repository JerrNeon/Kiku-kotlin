package com.jn.kikukt.service

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.jn.kikukt.R
import com.jn.kikukt.common.utils.file.FileIOUtils
import com.jn.kikukt.common.utils.file.FileUtils
import com.jn.kikukt.common.utils.getInstallIntent
import com.jn.kikukt.entiy.VersionUpdateVO
import com.jn.kikukt.net.RetrofitManage
import com.jn.kikukt.net.retrofit.callback.ProgressListener
import com.jn.kikukt.receiver.VersionUpdateReceiver
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.io.File

/**
 * Author：Stevie.Chen Time：2019/7/12
 * Class Comment：版本更新服务
 */
class VersionUpdateService : Service() {

    private var mNotification: Notification? = null//下载任务栏通知框
    private var mCurrentProgress = 0f//下载进度

    private val channelId = "VersionUpdateService20190712"
    private val channelName = resources.getString(R.string.app_name)
    @RequiresApi(Build.VERSION_CODES.N)
    private val importance = NotificationManager.IMPORTANCE_LOW

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        val versionUpdateVO = intent.getParcelableExtra<VersionUpdateVO>(VersionUpdateVO::class.java.simpleName)
        downloadFile(versionUpdateVO!!)
        return super.onStartCommand(intent, flags, startId)
    }

    /**
     * 下载文件
     *
     * @param versionUpdateVO
     */
    private fun downloadFile(versionUpdateVO: VersionUpdateVO) {
        //通知管理
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        //创建通知渠道
        val notificationChannel = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel(channelId, channelName, importance)
        } else {
            null
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel?.enableVibration(true)
            notificationChannel?.vibrationPattern = longArrayOf(500, 500)
            notificationChannel?.setSound(null, null)
            manager.createNotificationChannel(notificationChannel!!)
        }
        //常见通知
        val builder = NotificationCompat.Builder(this, "1")
            .setSmallIcon(versionUpdateVO.appIconResId)
            .setContentTitle(versionUpdateVO.appName)
            .setTicker(resources.getString(R.string.versionUpdate_downloadStart))

        //下载
        val downLoadFileName = versionUpdateVO.appName
        RetrofitManage.instance
            .getDownloadObservable(
                versionUpdateVO.downLoadUrl!!, object : ProgressListener {
                    override fun onProgress(
                        progressBytes: Long,
                        totalBytes: Long,
                        progressPercent: Float,
                        done: Boolean
                    ) {
                        //计算每百分之5刷新一下通知栏
                        val progress2 = progressPercent * 100
                        if (progress2 - mCurrentProgress > 5) {
                            mCurrentProgress = progress2
                            builder.setContentText(
                                String.format(
                                    resources.getString(R.string.versionUpdate_downloadProgress),
                                    progress2.toInt()
                                )
                            )
                            builder.setProgress(100, progress2.toInt(), false)
                            manager.notify(0, builder.build())
                        }
                    }
                }
            )
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .map { responseBody ->
                val mimeType =
                    responseBody.contentType()!!.type + File.separator + responseBody.contentType()!!.subtype
                val fileSuffix = FileUtils.getFileSuffix(mimeType)//文件后缀名
                val filePath =
                    FileUtils.getFileCacheFile().absolutePath + File.separator + downLoadFileName + "." + fileSuffix
                FileIOUtils.writeFileFromIS(filePath, responseBody.byteStream())
                filePath
            }
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<String> {

                override fun onSubscribe(d: Disposable) {
                    builder.setContentText(
                        String.format(
                            resources.getString(R.string.versionUpdate_downloadProgress),
                            0
                        )
                    )
                    mCurrentProgress = 0f
                    mNotification = builder.build()
                    manager.notify(0, mNotification)
                }

                override fun onNext(filePath: String) {
                    val intent = this@VersionUpdateService.getInstallIntent(filePath)
                    val pendingIntent = PendingIntent.getActivity(
                        this@VersionUpdateService,
                        0,
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                    )
                    builder.setContentIntent(pendingIntent)
                    builder.setAutoCancel(true)//设置点击后消失
                    builder.setContentText(resources.getString(R.string.versionUpdate_downloadComplete))
                    builder.setProgress(100, 100, false)
                    manager.notify(0, builder.build())
                    val broadcastIntent = Intent(VersionUpdateReceiver.VERSION_UPDATE_ACTION)
                    broadcastIntent.putExtra(
                        VersionUpdateReceiver.VERSION_UPDATE_ACTION,
                        resources.getString(R.string.versionUpdate_downloadComplete)
                    )
                    sendBroadcast(broadcastIntent)
                    startActivity(intent)
                    stopSelf()
                }

                override fun onError(e: Throwable) {
                    e.printStackTrace()
                    mCurrentProgress = 0f
                    builder.setContentText(resources.getString(R.string.versionUpdate_downloadFailure))
                    manager.notify(0, builder.build())
                    val broadcastIntent = Intent(VersionUpdateReceiver.VERSION_UPDATE_ACTION)
                    broadcastIntent.putExtra(
                        VersionUpdateReceiver.VERSION_UPDATE_ACTION,
                        resources.getString(R.string.versionUpdate_downloadFailure)
                    )
                    sendBroadcast(broadcastIntent)
                }

                override fun onComplete() {

                }
            })
    }
}