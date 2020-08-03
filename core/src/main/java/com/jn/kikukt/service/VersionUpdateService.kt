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
import com.jn.kikukt.net.coroutines.RetrofitManager
import com.jn.kikukt.net.retrofit.callback.ProgressListener
import com.jn.kikukt.net.rxjava.RetrofitManage
import com.jn.kikukt.receiver.VersionUpdateReceiver
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.*
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

    private val scope = MainScope()

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        val versionUpdateVO =
            intent.getParcelableExtra<VersionUpdateVO>(VersionUpdateVO::class.java.simpleName)
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
        val downLoadUrl = versionUpdateVO.downLoadUrl ?: ""
        //RxJava + Retrofit下载
        //downloadByRxJava(manager, builder, downLoadUrl, downLoadFileName)
        //Coroutines + Retrofit下载
        downloadByCoroutines(manager, builder, downLoadUrl, downLoadFileName)
    }

    private fun downloadByRxJava(
        manager: NotificationManager,
        builder: NotificationCompat.Builder,
        downLoadUrl: String,
        downLoadFileName: String?
    ) {
        RetrofitManage.download(downLoadUrl, object : ProgressListener {
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
                            getString(R.string.versionUpdate_downloadProgress),
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
                var fileSuffix = ""
                responseBody.contentType()?.run {
                    val mimeType = type + File.separator + subtype
                    fileSuffix = FileUtils.getFileSuffix(mimeType)//文件后缀名
                }
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
                            getString(R.string.versionUpdate_downloadProgress),
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
                    builder.run {
                        setContentIntent(pendingIntent)
                        setAutoCancel(true)//设置点击后消失
                        setContentText(getString(R.string.versionUpdate_downloadComplete))
                        setProgress(100, 100, false)
                    }
                    manager.notify(0, builder.build())
                    val broadcastIntent = Intent(VersionUpdateReceiver.VERSION_UPDATE_ACTION)
                    broadcastIntent.putExtra(
                        VersionUpdateReceiver.VERSION_UPDATE_ACTION,
                        getString(R.string.versionUpdate_downloadComplete)
                    )
                    sendBroadcast(broadcastIntent)
                    startActivity(intent)
                    stopSelf()
                }

                override fun onError(e: Throwable) {
                    e.printStackTrace()
                    mCurrentProgress = 0f
                    builder.setContentText(getString(R.string.versionUpdate_downloadFailure))
                    manager.notify(0, builder.build())
                    val broadcastIntent = Intent(VersionUpdateReceiver.VERSION_UPDATE_ACTION)
                    broadcastIntent.putExtra(
                        VersionUpdateReceiver.VERSION_UPDATE_ACTION,
                        getString(R.string.versionUpdate_downloadFailure)
                    )
                    sendBroadcast(broadcastIntent)
                }

                override fun onComplete() {
                }
            })
    }

    private fun downloadByCoroutines(
        manager: NotificationManager,
        builder: NotificationCompat.Builder,
        downLoadUrl: String,
        downLoadFileName: String?
    ) {
        scope.launch {
            try {
                //设置下载开始通知栏进度
                builder.setContentText(
                    String.format(
                        getString(R.string.versionUpdate_downloadProgress),
                        0
                    )
                )
                mCurrentProgress = 0f
                mNotification = builder.build()
                manager.notify(0, mNotification)
                //开始下载
                val filePathDeferred = async(Dispatchers.IO) {
                    val responseBody =
                        RetrofitManager.download(downLoadUrl) { _, _, progressPercent, _ ->
                            //计算每百分之5刷新一下通知栏
                            val progress2 = progressPercent * 100
                            if (progress2 - mCurrentProgress > 5) {
                                mCurrentProgress = progress2
                                builder.setContentText(
                                    String.format(
                                        getString(R.string.versionUpdate_downloadProgress),
                                        progress2.toInt()
                                    )
                                )
                                builder.setProgress(100, progress2.toInt(), false)
                                manager.notify(0, builder.build())
                            }
                        }
                    //下载完成(保存文件到手机)
                    var fileSuffix = ""
                    responseBody.contentType()?.run {
                        val mimeType = type + File.separator + subtype
                        fileSuffix = FileUtils.getFileSuffix(mimeType)//文件后缀名
                    }
                    val filePath =
                        FileUtils.getFileCacheFile().absolutePath + File.separator + downLoadFileName + "." + fileSuffix
                    FileIOUtils.writeFileFromIS(filePath, responseBody.byteStream())
                    filePath
                }
                val filePath = filePathDeferred.await()
                //设置点击通知栏进行Apk的安装
                val intent = this@VersionUpdateService.getInstallIntent(filePath)
                val pendingIntent = PendingIntent.getActivity(
                    this@VersionUpdateService,
                    0,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT
                )
                builder.run {
                    setContentIntent(pendingIntent)
                    setAutoCancel(true)//设置点击后消失
                    setContentText(getString(R.string.versionUpdate_downloadComplete))
                    setProgress(100, 100, false)
                }
                manager.notify(0, builder.build())
                //发送下载完成的广播
                val broadcastIntent = Intent(VersionUpdateReceiver.VERSION_UPDATE_ACTION)
                broadcastIntent.putExtra(
                    VersionUpdateReceiver.VERSION_UPDATE_ACTION,
                    getString(R.string.versionUpdate_downloadComplete)
                )
                sendBroadcast(broadcastIntent)
                startActivity(intent)
                stopSelf()
            } catch (e: Throwable) {
                //下载失败
                e.printStackTrace()
                mCurrentProgress = 0f
                builder.setContentText(getString(R.string.versionUpdate_downloadFailure))
                manager.notify(0, builder.build())
                val broadcastIntent = Intent(VersionUpdateReceiver.VERSION_UPDATE_ACTION)
                broadcastIntent.putExtra(
                    VersionUpdateReceiver.VERSION_UPDATE_ACTION,
                    resources.getString(R.string.versionUpdate_downloadFailure)
                )
                sendBroadcast(broadcastIntent)
            }
        }
    }

    override fun onDestroy() {
        scope.cancel()
        super.onDestroy()
    }
}