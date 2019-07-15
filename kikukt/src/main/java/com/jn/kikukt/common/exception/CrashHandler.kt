package com.jn.kikukt.common.exception

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Process
import com.jn.kikukt.utils.file.FileUtils
import com.jn.kikukt.utils.logE
import com.jn.kikukt.utils.logI
import com.jn.kikukt.utils.manager.SPManage
import java.io.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * Author：Stevie.Chen Time：2019/7/12
 * Class Comment：全局处理异常
 */
class CrashHandler private constructor() : Thread.UncaughtExceptionHandler {

    private var mDefaultCrashHandler: Thread.UncaughtExceptionHandler? = null
    private var mContext: Context? = null

    companion object {
        val instance: CrashHandler by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            CrashHandler()
        }
    }

    fun init(context: Context) {
        mDefaultCrashHandler = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler(this)
        mContext = context.applicationContext
    }

    /**
     * 这个是最关键的函数，当程序中有未被捕获的异常，系统将会自动调用uncaughtException方法
     * thread为出现未捕获异常的线程，ex为未捕获的异常，有了这个ex，我们就可以得到异常信息
     *
     * @param thread
     * @param ex
     */
    override fun uncaughtException(thread: Thread, ex: Throwable) {
        try {
            //导出异常信息到手机中
            //dumpExceptionToLocal(ex);
            //这里可以上传异常信息到服务器，便于开发人员分析日志从而解决bug
            uploadExceptionToServer(ex)
        } catch (e: IOException) {
            e.printStackTrace()
        }

        ex.printStackTrace()
        //如果系统提供默认的异常处理器，则交给系统去结束程序，否则就由自己结束自己
        if (mDefaultCrashHandler != null) {
            mDefaultCrashHandler!!.uncaughtException(thread, ex)
        } else {
            //自己处理
            try {
                //延迟2秒杀进程
                Thread.sleep(2000)
                //showToast("很抱歉，程序出现异常，即将退出~");
            } catch (e: InterruptedException) {
                "error : $e.message".logE()
            }

            Process.killProcess(Process.myPid())
        }
    }

    /**
     * 导出异常信息到手机中
     *
     * @param ex
     * @throws IOException
     */
    @Throws(IOException::class)
    private fun dumpExceptionToLocal(ex: Throwable) {
        val time = SimpleDateFormat("yyyy-MM-dd HH:MM:SS", Locale.CHINA).format(Date(System.currentTimeMillis()))
        val file = File(FileUtils.getCrashPath(time))
        try {
            val pw = PrintWriter(BufferedWriter(FileWriter(file)))
            pw.println(time)
            dumpPhoneInfo(pw)
            pw.println()
            ex.printStackTrace(pw)
            pw.close()
            "dump crash info seccess".logI()
        } catch (e: Exception) {
            e.message?.logE()
            "dump crash info failed".logE()
        }

    }

    /**
     * 手机信息
     *
     * @param pw
     * @throws PackageManager.NameNotFoundException
     */
    @Throws(PackageManager.NameNotFoundException::class)
    private fun dumpPhoneInfo(pw: PrintWriter) {
        val pm = mContext!!.packageManager
        val pi = pm.getPackageInfo(mContext!!.packageName, PackageManager.GET_ACTIVITIES)
        pw.print("App Version: ")
        pw.print(pi.versionName)
        pw.print('_')
        pw.println(pi.versionCode)
        //Android版本号
        pw.print("OS Version: ")
        pw.print(Build.VERSION.RELEASE)
        pw.print("_")
        pw.println(Build.VERSION.SDK_INT)
        //手机制造商
        pw.print("Vendor: ")
        pw.println(Build.MANUFACTURER)
        //手机型号
        pw.print("Model: ")
        pw.println(Build.MODEL)
        //CPU架构
        pw.print("CPU ABI: ")
        pw.println(Build.CPU_ABI)
    }

    /**
     * 上传异常信息到服务器，便于开发人员分析日志从而解决bug
     */
    @Throws(IOException::class)
    private fun uploadExceptionToServer(ex: Throwable) {
        val time = SimpleDateFormat("yyyy-MM-dd HH:MM:SS", Locale.CHINA).format(Date(System.currentTimeMillis()))
        try {
            val writer = StringWriter()
            val pw = PrintWriter(BufferedWriter(writer))
            pw.println(time)
            dumpPhoneInfo(pw)
            pw.println()
            ex.printStackTrace(pw)
            pw.close()
            SPManage.instance.setExceptionMessage(writer.toString())
            "dump crash info seccess".logI()
        } catch (e: Exception) {
            e.message?.logE()
            "dump crash info failed".logE()
        }

    }
}