import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Build
import android.telephony.TelephonyManager
import androidx.annotation.RequiresPermission
import com.jn.kikukt.common.utils.IntentUtils

/**
 * Author：Stevie.Chen Time：2020/9/28
 * Class Comment：Context扩展方法
 */

/**
 * 是否支持Ble
 */
fun Context.isSupportBle(): Boolean =
    packageManager.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)

/**
 * 定位服务是否开启
 */
fun Context.isLocationServiceEnable(isVersionInM: Boolean = true): Boolean {
    val manager = getSystemService(Context.LOCATION_SERVICE) as? LocationManager
    val networkProvider = manager?.isProviderEnabled(LocationManager.NETWORK_PROVIDER) ?: false
    val gpsProvider = manager?.isProviderEnabled(LocationManager.GPS_PROVIDER) ?: false
    return if (isVersionInM) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return networkProvider || gpsProvider
        }
        return true
    } else {
        networkProvider || gpsProvider
    }
}


/**
 * 是否有SIM卡
 * 使用这个方法需要申请 Manifest.permission.READ_PHONE_STATE 这个权限
 */
@SuppressLint("HardwareIds")
@RequiresPermission(Manifest.permission.READ_PHONE_STATE)
fun Context.exitSimCard(): Boolean {
    try {
        val tm = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        val simSer = tm.simSerialNumber
        if (simSer == null || simSer == "") {
            return false
        }
    } catch (e: Exception) {
        e.printStackTrace()
        return false
    }
    return true
}

/**
 * 安装Apk
 */
fun Context.install(apkPath: String) {
    startActivity(IntentUtils.getInstallIntent(apkPath))
}

/**
 * 卸载Apk
 */
fun Context.uninstall() {
    startActivity(IntentUtils.getUninstallIntent(packageName))
}