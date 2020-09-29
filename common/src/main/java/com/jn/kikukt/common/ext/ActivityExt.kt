import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat

/**
 * Author：Stevie.Chen Time：2020/9/28
 * Class Comment：Activity扩展方法
 */

/**
 * 是否授予权限
 */
fun Activity.isGranted(permission: String) = ActivityCompat.checkSelfPermission(
    applicationContext,
    permission
) == PackageManager.PERMISSION_GRANTED

/**
 * 是否授予权限
 */
fun Activity.isGranted(vararg permissions: String): Boolean =
    permissions.count { permission -> isGranted(permission) } == permissions.size

/**
 * 是否没有授予权限并且勾选了不再提示
 */
fun Activity.isNotGrantedAndPrompt(permission: String) =
    !ActivityCompat.shouldShowRequestPermissionRationale(
        this,
        permission
    )

/**
 * 是否没有授予权限并且勾选了不再提示
 */
fun Activity.isNotGrantedAndPrompt(vararg permissions: String) =
    permissions.any { permission -> isNotGrantedAndPrompt(permission) }

/**
 * 是否授予权限
 */
fun Activity.isLocationGranted() =
    isGranted(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)

/**
 * 是否没有授予定位权限并且勾选了不再提示
 */
fun Activity.isLocationNotGrantedAndPrompt() =
    isGranted(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)

/**
 * 请求定位权限
 */
fun AppCompatActivity.requestPermission(permission: String, block: (result: Int) -> Unit) {
    registerForActivityResult(ActivityResultContracts.RequestPermission()) {
        if (it) {
            //用户已经同意了权限
            block.invoke(0)
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                //用户拒绝了权限
                block.invoke(1)
            } else {
                //用户拒绝了权限并且勾选了不再提示
                block.invoke(2)
            }
        }
    }.launch(permission)
}

/**
 * 请求定位权限
 */
fun AppCompatActivity.requestMultiplePermissions(
    permissions: Array<String>,
    block: (result: Int) -> Unit
) {
    registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
        if (it.values.count { result -> result } == it.size) {
            //用户已经同意了权限
            block.invoke(0)
        } else {
            if (it.keys.count { permission ->
                    ActivityCompat.shouldShowRequestPermissionRationale(this, permission)
                } == it.size) {
                //用户拒绝了权限
                block.invoke(1)
            } else {
                //用户拒绝了权限并且勾选了不再提示
                block.invoke(2)
            }
        }
    }.launch(permissions)
}

/**
 * 请求拍照权限
 */
fun AppCompatActivity.requestCameraPermission(block: (result: Int) -> Unit) {
    requestPermission(Manifest.permission.CAMERA, block)
}

/**
 * 请求存储权限
 */
fun AppCompatActivity.requestStoragePermission(block: (result: Int) -> Unit) {
    requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, block)
}

/**
 * 请求相机&存储权限
 */
fun AppCompatActivity.requestCameraStoragePermission(block: (result: Int) -> Unit) {
    requestMultiplePermissions(
        arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ), block
    )
}

/**
 * 请求电话权限
 */
fun AppCompatActivity.requestPhonePermission(block: (result: Int) -> Unit) {
    requestMultiplePermissions(
        arrayOf(
            Manifest.permission.CALL_PHONE,
            Manifest.permission.READ_PHONE_STATE
        ), block
    )
}

/**
 * 请求定位权限
 */
fun AppCompatActivity.requestLocationPermission(block: (result: Int) -> Unit) {
    requestMultiplePermissions(
        arrayOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        ), block
    )
}

/**
 * startActivityForResult
 */
fun AppCompatActivity.startActivityForResult(
    intent: Intent,
    successBlock: (result: ActivityResult) -> Unit,
    block: ((result: ActivityResult) -> Unit)? = null
) {
    registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK)
            successBlock.invoke(it)
        block?.invoke(it)
    }.launch(intent)
}

/**
 * 请求定位服务
 */
fun AppCompatActivity.requestLocationService(block: (result: ActivityResult) -> Unit) {
    startActivityForResult(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), block)
}

/**
 * 打开应用详情界面
 */
fun AppCompatActivity.requestApplicationSettings(block: (result: ActivityResult) -> Unit) {
    startActivityForResult(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
        data = Uri.fromParts("package", packageName, null)
        flags = Intent.FLAG_ACTIVITY_NEW_TASK
    }, block)
}

/**
 * 请求蓝牙服务
 */
fun AppCompatActivity.requestBluetoothService(block: (result: ActivityResult) -> Unit) {
    startActivityForResult(Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE), block)
}