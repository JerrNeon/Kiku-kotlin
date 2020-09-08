package com.jn.kikukt.common.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Parcelable
import android.widget.Toast
import kotlinx.android.parcel.Parcelize
import java.util.*

/**
 * Author：Stevie.Chen Time：2019/7/15
 * Class Comment：
 */
object MapUtils {
    private const val TAG = "MapUtils: "

    const val BAIDU_PACKAGENAME = "com.baidu.BaiduMap"
    const val AUTONAVI_PACKAGENAME = "com.autonavi.minimap"
    const val GOOGLE_PACKAGENAME = "com.google.android.apps.maps"

    enum class MapType {
        DRIVING, TRANSIT, BIKING, WALKING
    }

    /**
     * 检查手机上是否安装了指定的软件
     *
     * @param context
     * @param packageName：应用包名
     * @return
     */
    fun isInstall(context: Context, packageName: String): Boolean {
        //获取packagemanager
        val packageManager = context.packageManager
        //获取所有已安装程序的包信息
        val packageInfos = packageManager.getInstalledPackages(0)
        //用于存储所有已安装程序的包名
        val packageNames = ArrayList<String>()
        //从pinfo中将包名字逐一取出，压入pName list中
        for (i in packageInfos.indices) {
            val packName = packageInfos[i].packageName
            packageNames.add(packName)
        }
        //判断packageNames中是否有目标程序的包名，有TRUE，没有FALSE
        return packageNames.contains(packageName)
    }

    /**
     * 开启百度地图并导航
     *
     * @param context
     * @param mapType       行驶类型
     * @param originLl      出发地经纬度
     * @param destinationLl 目的地经纬度
     */
    fun openBaiduMap(context: Context, mapType: MapType, originLl: LatLng, destinationLl: LatLng) {
        if (!isInstall(context, BAIDU_PACKAGENAME)) {//未安装
            //market为路径，id为包名
            //显示手机上所有的market商店
            Toast.makeText(context, "您尚未安装百度地图", Toast.LENGTH_LONG).show()
            val uri = Uri.parse("market://details?id=com.baidu.BaiduMap")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            context.startActivity(intent)
            return
        }
        try {
            val intent = Intent()
            var uriString = ""
            when (mapType) {
                MapType.DRIVING -> uriString = String.format(
                    "baidumap://map/navi?location=%s,%s",
                    (destinationLl.latitude).toString() + "", (destinationLl.longitude).toString() + ""
                )
                MapType.TRANSIT -> uriString = String.format(
                    "baidumap://map/direction?destination=%s,%s&mode=transit&target=1",
                    //originLl.latitude + "", originLl.longitude + "",
                    (destinationLl.latitude).toString() + "", (destinationLl.longitude).toString() + ""
                )
                MapType.BIKING -> uriString = String.format(
                    "baidumap://map/bikenavi?origin=%s,%s&destination=%s,%s",
                    (originLl.latitude).toString() + "", (originLl.longitude).toString() + "",
                    (destinationLl.latitude).toString() + "", (destinationLl.longitude).toString() + ""
                )
                MapType.WALKING -> uriString = String.format(
                    "baidumap://map/walknavi?origin=%s,%s&destination=%s,%s",
                    (originLl.latitude).toString() + "", (originLl.longitude).toString() + "",
                    (destinationLl.latitude).toString() + "", (destinationLl.longitude).toString() + ""
                )
            }
            intent.data = Uri.parse(uriString)
            context.startActivity(intent) //启动调用
        } catch (e: Exception) {
            TAG + e.message?.logE()
        }

    }

    fun openAutoNaviMap(context: Context, mapType: MapType, originLl: LatLng, destinationLl: LatLng) {
        if (!isInstall(context, AUTONAVI_PACKAGENAME)) {
            Toast.makeText(context, "您尚未安装高德地图", Toast.LENGTH_LONG).show()
            val uri = Uri.parse("market://details?id=com.autonavi.minimap")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            context.startActivity(intent)
            return
        }
        try {
            val intent = Intent()
            intent.data = Uri.parse(
                ("androidamap://navi?sourceApplication=慧医" +
                        "&poiname=我的目的地" +
                        "&lat=" + destinationLl.latitude + "&lon=" + destinationLl.longitude + "&dev=0")
            )
            context.startActivity(intent)
        } catch (e: Exception) {
            TAG + e.message?.logE()
        }

    }

    fun openGoogleMap(context: Context, mapType: MapType, originLl: LatLng, destinationLl: LatLng) {
        if (!isInstall(context, GOOGLE_PACKAGENAME)) {
            Toast.makeText(context, "您尚未安装谷歌地图", Toast.LENGTH_LONG).show()
            val uri = Uri.parse("market://details?id=com.google.android.apps.maps")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            context.startActivity(intent)
            return
        }
        val gmmIntentUri = Uri.parse(
            "google.navigation:q="
                    + destinationLl.latitude + "," + destinationLl.longitude
                    + ", + Sydney +Australia"
        )
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        mapIntent.setPackage("com.google.android.apps.maps")
        context.startActivity(mapIntent)
    }

}

@Parcelize
data class LatLng(var latitude: Double, var longitude: Double) : Parcelable
