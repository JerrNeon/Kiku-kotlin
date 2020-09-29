import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment

/**
 * Author：Stevie.Chen Time：2020/9/28
 * Class Comment：Activity扩展方法
 */

/**
 * 是否授予权限
 */
fun Fragment.isGranted(permission: String) = context?.let {
    ActivityCompat.checkSelfPermission(
        it,
        permission
    )
} == PackageManager.PERMISSION_GRANTED

/**
 * 是否授予权限
 */
fun Fragment.isGranted(vararg permissions: String): Boolean =
    permissions.count { permission -> isGranted(permission) } == permissions.size

/**
 * 是否没有授予权限并且勾选了不再提示
 */
fun Fragment.isNotGrantedAndPrompt(permission: String) =
    activity?.let {
        ActivityCompat.shouldShowRequestPermissionRationale(
            it,
            permission
        )
    } == false

/**
 * 是否没有授予权限并且勾选了不再提示
 */
fun Fragment.isNotGrantedAndPrompt(vararg permissions: String) =
    permissions.any { permission -> isNotGrantedAndPrompt(permission) }

/**
 * 是否授予权限
 */
fun Fragment.isLocationGranted() =
    isGranted(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)

/**
 * 是否没有授予定位权限并且勾选了不再提示
 */
fun Fragment.isLocationNotGrantedAndPrompt() =
    isGranted(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)

/**
 * 请求定位权限
 * <P>
 *  Fragment中的registerForActivityResult必须在OnAttach或onCreate方法中调用
 * </p>
 * @return 请求权限的方法体
 */
fun Fragment.requestPermission(
    permission: String,
    block: (result: Int) -> Unit
): () -> Unit {
    val launcher = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
        if (it) {
            //用户已经同意了权限
            block.invoke(0)
        } else {
            if (activity?.let { ac ->
                    ActivityCompat.shouldShowRequestPermissionRationale(ac, permission)
                } == true) {
                //用户拒绝了权限
                block.invoke(1)
            } else {
                //用户拒绝了权限并且勾选了不再提示
                block.invoke(2)
            }
        }
    }
    return { launcher.launch(permission) }
}

/**
 * 请求定位权限
 * <P>
 *  Fragment中的registerForActivityResult必须在OnAttach或onCreate方法中调用
 * </p>
 * @return 请求权限的方法体
 */
fun Fragment.requestMultiplePermissions(
    permissions: Array<String>,
    block: (result: Int) -> Unit
): () -> Unit {
    val launcher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
        if (it.values.count { result -> result } == it.size) {
            //用户已经同意了权限
            block.invoke(0)
        } else {
            if (it.keys.count { permission ->
                    activity?.let { ac ->
                        ActivityCompat.shouldShowRequestPermissionRationale(ac, permission)
                    } == true
                } == it.size) {
                //用户拒绝了权限
                block.invoke(1)
            } else {
                //用户拒绝了权限并且勾选了不再提示
                block.invoke(2)
            }
        }
    }
    return { launcher.launch(permissions) }
}

/**
 * 请求拍照权限
 */
fun Fragment.requestCameraPermission(block: (result: Int) -> Unit): () -> Unit {
    return requestPermission(Manifest.permission.CAMERA, block)
}

/**
 * 请求存储权限
 */
fun Fragment.requestStoragePermission(block: (result: Int) -> Unit): () -> Unit {
    return requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, block)
}

/**
 * 请求相机&存储权限
 */
fun Fragment.requestCameraStoragePermission(block: (result: Int) -> Unit): () -> Unit {
    return requestMultiplePermissions(
        arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ), block
    )
}

/**
 * 请求电话权限
 */
fun Fragment.requestPhonePermission(block: (result: Int) -> Unit): () -> Unit {
    return requestMultiplePermissions(
        arrayOf(
            Manifest.permission.CALL_PHONE,
            Manifest.permission.READ_PHONE_STATE
        ), block
    )
}

/**
 * 请求定位权限
 */
fun Fragment.requestLocationPermission(block: (result: Int) -> Unit): () -> Unit {
    return requestMultiplePermissions(
        arrayOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        ), block
    )
}

/**
 * startActivityForResult
 * <P>
 *  Fragment中的registerForActivityResult必须在OnAttach或onCreate方法中调用
 * </p>
 * @return 请求权限的方法体
 */
fun Fragment.startActivityForResult(
    intent: Intent,
    successBlock: (result: ActivityResult) -> Unit,
    block: ((result: ActivityResult) -> Unit)? = null
): () -> Unit {
    val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK)
            successBlock.invoke(it)
        block?.invoke(it)
    }
    return { launcher.launch(intent) }
}

/**
 * 请求定位服务
 */
fun Fragment.requestLocationService(block: (result: ActivityResult) -> Unit): () -> Unit {
    return startActivityForResult(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), block)
}

/**
 * 打开应用详情界面
 */
fun Fragment.requestApplicationSettings(block: (result: ActivityResult) -> Unit): () -> Unit {
    return startActivityForResult(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
        data = Uri.fromParts("package", activity?.packageName, null)
        flags = Intent.FLAG_ACTIVITY_NEW_TASK
    }, block)
}

/**
 * 请求蓝牙服务
 */
fun Fragment.requestBluetoothService(block: (result: ActivityResult) -> Unit): () -> Unit {
    return startActivityForResult(Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE), block)
}