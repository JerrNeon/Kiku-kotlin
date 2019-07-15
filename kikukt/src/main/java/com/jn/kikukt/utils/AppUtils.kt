package com.jn.kikukt.utils

import android.app.Activity
import android.app.ActivityManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.Rect
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.wifi.WifiManager
import android.os.Build
import android.os.PowerManager
import android.provider.Settings
import android.telephony.TelephonyManager
import android.telephony.TelephonyManager.*
import android.util.DisplayMetrics
import android.view.WindowManager
import com.jn.kikukt.utils.AppUtils.Companion.NET_2G
import com.jn.kikukt.utils.AppUtils.Companion.NET_3G
import com.jn.kikukt.utils.AppUtils.Companion.NET_4G
import com.jn.kikukt.utils.AppUtils.Companion.NET_NO
import com.jn.kikukt.utils.AppUtils.Companion.NET_UNKNOWN
import com.jn.kikukt.utils.AppUtils.Companion.NET_WIFI
import java.io.IOException
import java.net.InetAddress
import java.net.NetworkInterface
import java.net.SocketException
import java.net.UnknownHostException
import java.util.*
import java.util.concurrent.Callable
import java.util.concurrent.ExecutionException
import java.util.concurrent.Executors

/**
 * Author：Stevie.Chen Time：2019/7/12
 * Class Comment：
 */

class AppUtils {
    companion object {
        const val NET_WIFI = 1
        const val NET_4G = 2
        const val NET_3G = 3
        const val NET_2G = 4
        const val NET_UNKNOWN = 5
        const val NET_NO = 6
    }
}

/**
 * 获取应用程序名称
 */
fun Context.getAppName(): String? {
    try {
        val packageManager = packageManager
        val packageInfo = packageManager.getPackageInfo(packageName, 0)
        val labelRes = packageInfo.applicationInfo.labelRes
        return resources.getString(labelRes)
    } catch (e: PackageManager.NameNotFoundException) {
        e.printStackTrace()
    }
    return ""
}

/**
 * 获取应用程序版本名称信息
 */
fun Context.getVersionName(): String? {
    try {
        val packageManager = packageManager
        val packageInfo = packageManager.getPackageInfo(packageName, 0)
        return packageInfo.versionName
    } catch (e: PackageManager.NameNotFoundException) {
        e.printStackTrace()
    }
    return ""
}

/**
 * 获取应用程序版本号信息
 */
fun Context.getVersionCode(): Long {
    try {
        val packageManager = packageManager
        val packageInfo = packageManager.getPackageInfo(packageName, 0)
        return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P)
            packageInfo.versionCode.toLong()
        else
            packageInfo.longVersionCode
    } catch (e: PackageManager.NameNotFoundException) {
        e.printStackTrace()
    }
    return 0
}

/**
 * 获取内存信息
 */
fun Context.getMemoryInfo(): ActivityManager.MemoryInfo? {
    try {
        val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val memoryInfo = ActivityManager.MemoryInfo()
        activityManager.getMemoryInfo(memoryInfo)
        return memoryInfo
    } catch (e: Exception) {
        e.printStackTrace()
    }

    return null
}

/**
 * 判断服务是否开启
 */
fun Context.isServiceRunning(serviceName: String): Boolean {
    if (serviceName.isEmpty())
        return false
    val myManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    val runningService = myManager
        .getRunningServices(Integer.MAX_VALUE) as ArrayList<ActivityManager.RunningServiceInfo>
    for (i in runningService.indices) {
        if (runningService[i].service.className.toString() === serviceName) {
            return true
        }
    }
    return false
}

/**
 * 屏幕是否亮或熄灭
 * @return true：亮 false：熄灭
 */
fun Context.isScreenOn(): Boolean {
    val pm = getSystemService(Context.POWER_SERVICE) as PowerManager
    return pm.isInteractive
}

/**
 * 获取当前进程名称
 */
fun Context.getProcessName(): String {
    val pid = android.os.Process.myPid()
    val mActivityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    for (appProcess in mActivityManager.runningAppProcesses) {
        if (appProcess.pid == pid) {
            return appProcess.processName
        }
    }
    return ""
}

/**
 * 获得屏幕宽度
 */
fun Context.getScreenWidth(): Int {
    val wm = getSystemService(Context.WINDOW_SERVICE) as WindowManager
    val outMetrics = DisplayMetrics()
    wm.defaultDisplay.getMetrics(outMetrics)
    return outMetrics.widthPixels
}

/**
 * 获得屏幕高度
 */
fun Context.getScreenHeight(): Int {
    val wm = getSystemService(Context.WINDOW_SERVICE) as WindowManager
    val outMetrics = DisplayMetrics()
    wm.defaultDisplay.getMetrics(outMetrics)
    return outMetrics.heightPixels
}

/**
 * 获得状态栏的高度
 */
fun Context.getStatusHeight(): Int {
    var statusHeight = -1
    try {
        val clazz = Class.forName("com.android.internal.R\$dimen")
        val `object` = clazz.newInstance()
        val height = Integer.parseInt(
            clazz.getField("status_bar_height")
                .get(`object`).toString()
        )
        statusHeight = resources.getDimensionPixelSize(height)
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return statusHeight
}

/**
 * 获取状态栏的高度
 */
fun Context.getStatusBarHeight(): Int {
    var result = 0
    val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
    if (resourceId > 0) {
        result = resources.getDimensionPixelSize(resourceId)
    }
    return result
}

/**
 * 获取当前屏幕截图，包含状态栏
 */
fun Activity.snapShotWithStatusBar(): Bitmap {
    val view = window.decorView
    view.isDrawingCacheEnabled = true
    view.buildDrawingCache()
    val bmp = view.drawingCache
    val width = getScreenWidth()
    val height = getScreenHeight()
    val bp: Bitmap = Bitmap.createBitmap(bmp, 0, 0, width, height)
    view.destroyDrawingCache()
    return bp

}

/**
 * 获取当前屏幕截图，不包含状态栏
 */
fun Activity.snapShotWithoutStatusBar(): Bitmap {
    val view = window.decorView
    view.isDrawingCacheEnabled = true
    view.buildDrawingCache()
    val bmp = view.drawingCache
    val frame = Rect()
    window.decorView.getWindowVisibleDisplayFrame(frame)
    val statusBarHeight = frame.top

    val width = getScreenWidth()
    val height = getScreenHeight()
    val bp: Bitmap = Bitmap.createBitmap(bmp, 0, statusBarHeight, width, height - statusBarHeight)
    view.destroyDrawingCache()
    return bp
}

/**
 * 是否横屏
 * @return true为横屏，false为竖屏
 */
fun Context.isLandscape(): Boolean {
    return resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
}

/**
 * 判断是否是平板 这个方法是从 Google I/O App for Android 的源码里找来的，非常准确。
 */
fun Context.isTablet(): Boolean {
    return resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK >= Configuration.SCREENLAYOUT_SIZE_LARGE
}

/**
 * 获取活动网络信息
 *
 * 需添加权限 `<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>`
 *
 * @return NetworkInfo
 */
private fun Context.getActiveNetworkInfo(): NetworkInfo? {
    try {
        return (getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).activeNetworkInfo
    } catch (e: NullPointerException) {
        e.printStackTrace()
    } catch (e: Exception) {
        e.printStackTrace()
    }

    return null
}

/**
 * 判断网络是否连接
 */
fun Context.isConnected(): Boolean {
    val info = getActiveNetworkInfo()
    return info != null && info.isConnected
}

/**
 * 判断是否是wifi连接
 */
fun Context.isWifi(): Boolean {
    val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    return cm.activeNetworkInfo.type == ConnectivityManager.TYPE_WIFI
}

/**
 * 判断wifi是否打开
 *
 * 需添加权限 `<uses-permission android:name="android.permission.ACCESS_WIFI_STATE"/>`
 *
 * @return `true`: 是<br></br>`false`: 否
 */
fun Context.getWifiEnabled(): Boolean {
    val wifiManager = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
    return wifiManager.isWifiEnabled
}

/**
 * 判断移动数据是否打开
 *
 * @return `true`: 是<br></br>`false`: 否
 */
fun Context.getMobileDataEnabled(): Boolean {
    try {
        val tm = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        val getMobileDataEnabledMethod = tm.javaClass.getDeclaredMethod("getDataEnabled")
        return getMobileDataEnabledMethod.invoke(tm) as Boolean
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return false
}

/**
 * 打开或关闭wifi
 *
 * 需添加权限 `<uses-permission android:name="android.permission.CHANGE_WIFI_STATE"/>`
 *
 * @param enabled `true`: 打开<br></br>`false`: 关闭
 */
fun Context.setWifiEnabled(enabled: Boolean) {
    val wifiManager = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
    if (enabled) {
        if (!wifiManager.isWifiEnabled) {
            wifiManager.isWifiEnabled = true
        }
    } else {
        if (wifiManager.isWifiEnabled) {
            wifiManager.isWifiEnabled = false
        }
    }
}

/**
 * 打开或关闭移动数据
 *
 * 需系统应用 需添加权限`<uses-permission android:name="android.permission.MODIFY_PHONE_STATE"/>`
 *
 * @param enabled `true`: 打开<br></br>`false`: 关闭
 */
fun Context.setMobileDataEnabled(enabled: Boolean) {
    try {
        val tm = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        val setMobileDataEnabledMethod =
            tm.javaClass.getDeclaredMethod("setDataEnabled", Boolean::class.javaPrimitiveType)
        setMobileDataEnabledMethod.invoke(tm, enabled)
    } catch (e: Exception) {
        e.printStackTrace()
    }

}

/**
 * 获取当前网络类型
 *
 * @return NO：没有网络 WIFI：WIFI网络 CMWAP：WAP网络 CMNET：NET网络
 */
fun Context.getNetworkType(): Int {
    var netType = NET_NO
    val info = getActiveNetworkInfo()
    if (info != null && info.isAvailable) {
        when {
            info.type == ConnectivityManager.TYPE_WIFI -> netType = NET_WIFI
            info.type == ConnectivityManager.TYPE_MOBILE -> when (info.subtype) {
                NETWORK_TYPE_GSM, NETWORK_TYPE_GPRS, NETWORK_TYPE_CDMA, NETWORK_TYPE_EDGE, NETWORK_TYPE_1xRTT, NETWORK_TYPE_IDEN -> netType =
                    NET_2G
                NETWORK_TYPE_TD_SCDMA, NETWORK_TYPE_EVDO_A, NETWORK_TYPE_UMTS, NETWORK_TYPE_EVDO_0, NETWORK_TYPE_HSDPA, NETWORK_TYPE_HSUPA,
                NETWORK_TYPE_HSPA, NETWORK_TYPE_EVDO_B, NETWORK_TYPE_EHRPD, NETWORK_TYPE_HSPAP -> netType =
                    NET_3G
                NETWORK_TYPE_IWLAN, NETWORK_TYPE_LTE -> netType = NET_4G
                else -> {
                    val subtypeName = info.subtypeName
                    netType = if (subtypeName.equals("TD-SCDMA", ignoreCase = true)
                        || subtypeName.equals("WCDMA", ignoreCase = true)
                        || subtypeName.equals("CDMA2000", ignoreCase = true)
                    ) {
                        NET_3G
                    } else {
                        NET_UNKNOWN
                    }
                }
            }
            else -> netType = NET_UNKNOWN
        }
    }
    return netType
}

/**
 * 获取网络运营商名称
 *
 * 中国移动、如中国联通、中国电信
 *
 * @return 运营商名称
 */
fun Context.getNetworkOperatorName(): String? {
    val tm = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
    return tm.networkOperatorName
}

/**
 * 打开网络设置界面
 */
fun Activity.openSetting() {
    startActivity(Intent(Settings.ACTION_WIRELESS_SETTINGS).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
    val intent = Intent("/")
    val cm = ComponentName(
        "com.android.settings",
        "com.android.settings.WirelessSettings"
    )
    intent.component = cm
    intent.action = "android.intent.action.VIEW"
    startActivityForResult(intent, 0)
}

/**
 * 获取IP地址
 *
 * 需添加权限 `<uses-permission android:name="android.permission.INTERNET"/>`
 *
 * @param useIPv4 是否用IPv4
 * @return IP地址
 */
fun getIPAddress(useIPv4: Boolean): String? {
    try {
        val nis = NetworkInterface.getNetworkInterfaces()
        while (nis.hasMoreElements()) {
            val ni = nis.nextElement()
            // 防止小米手机返回10.0.2.15
            if (!ni.isUp) continue
            val addresses = ni.inetAddresses
            while (addresses.hasMoreElements()) {
                val inetAddress = addresses.nextElement()
                if (!inetAddress.isLoopbackAddress) {
                    val hostAddress = inetAddress.hostAddress
                    val isIPv4 = hostAddress.indexOf(':') < 0
                    if (useIPv4) {
                        if (isIPv4) return hostAddress
                    } else {
                        if (!isIPv4) {
                            val index = hostAddress.indexOf('%')
                            return if (index < 0) hostAddress.toUpperCase() else hostAddress.substring(
                                0,
                                index
                            ).toUpperCase()
                        }
                    }
                }
            }
        }
    } catch (e: SocketException) {
        e.printStackTrace()
    }

    return null
}

/**
 * 获取域名ip地址
 *
 * 需添加权限 `<uses-permission android:name="android.permission.INTERNET"/>`
 *
 * @param domain 域名
 * @return ip地址
 */
fun getDomainAddress(domain: String): String? {
    try {
        val exec = Executors.newCachedThreadPool()
        val fs = exec.submit(Callable {
            val inetAddress: InetAddress
            try {
                inetAddress = InetAddress.getByName(domain)
                return@Callable inetAddress.hostAddress
            } catch (e: UnknownHostException) {
                e.printStackTrace()
            }

            null
        })
        return fs.get()
    } catch (e: InterruptedException) {
        e.printStackTrace()
    } catch (e: ExecutionException) {
        e.printStackTrace()
    }

    return null
}

/**
 * 根据key获取config.properties里面的值
 *
 * @param key
 * @return
 */
fun Context.getProperty(key: String): String {
    try {
        val props = Properties()
        val input = assets.open("config.properties")
        props.load(input)
        return props.getProperty(key)
    } catch (e: IOException) {
        e.printStackTrace()
    }
    return ""
}

