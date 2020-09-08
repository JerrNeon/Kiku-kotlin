package com.jn.kiku.ttp.map.callback

import com.baidu.location.BDAbstractLocationListener
import com.baidu.location.BDLocation
import com.jn.kikukt.common.utils.logI

/**
 * Author：Stevie.Chen Time：2020/09/08 11:20
 * Class Comment：百度地图定位回调
 */
class BDLocationListenerIml(
    private val onSuccess: ((location: BDLocation) -> Unit)? = null,
    private val onFailure: ((location: BDLocation) -> Unit)? = null
) : BDAbstractLocationListener() {

    override fun onReceiveLocation(location: BDLocation) {
        //获取定位结果
        val sb = StringBuilder(256)
        sb.append("\ntime : ")
        sb.append(location.time) //获取定位时间
        sb.append("\nerror code : ")
        sb.append(location.locType) //获取类型类型
        sb.append("\nlatitude : ")
        sb.append(location.latitude) //获取纬度信息
        sb.append("\nlontitude : ")
        sb.append(location.longitude) //获取经度信息
        sb.append("\nradius : ")
        sb.append(location.radius) //获取定位精准度
        when (location.locType) {
            BDLocation.TypeGpsLocation -> {
                // GPS定位结果
                sb.append("\nspeed : ")
                sb.append(location.speed) // 单位：公里每小时
                sb.append("\nsatellite : ")
                sb.append(location.satelliteNumber) //获取卫星数
                sb.append("\nheight : ")
                sb.append(location.altitude) //获取海拔高度信息，单位米
                sb.append("\ndirection : ")
                sb.append(location.direction) //获取方向信息，单位度
                sb.append("\naddr : ")
                sb.append(location.addrStr) //获取地址信息
                sb.append("\ndescribe : ")
                sb.append("gps定位成功")
                locationSuccess(location)
            }
            BDLocation.TypeNetWorkLocation -> {
                // 网络定位结果
                sb.append("\naddr : ")
                sb.append(location.addrStr) //获取地址信息

                //sb.append("\noperationers : ");
                //sb.append(location.getOperators());    //获取运营商信息
                sb.append("\ndescribe : ")
                sb.append("网络定位成功")
                locationSuccess(location)
            }
            BDLocation.TypeOffLineLocation -> {
                // 离线定位结果
                sb.append("\ndescribe : ")
                sb.append("离线定位成功，离线定位结果也是有效的")
                locationSuccess(location)
            }
            BDLocation.TypeServerError -> {
                sb.append("\ndescribe : ")
                sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因")
                locationFailure(location)
            }
            BDLocation.TypeNetWorkException -> {
                sb.append("\ndescribe : ")
                sb.append("网络不同导致定位失败，请检查网络是否通畅")
                locationFailure(location)
            }
            BDLocation.TypeCriteriaException -> {
                sb.append("\ndescribe : ")
                sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机")
                locationFailure(location)
            }
        }
        sb.append("\nlocationdescribe : ")
        sb.append(location.locationDescribe) //位置语义化信息
        val list = location.poiList // POI数据
        if (list != null) {
            sb.append("\npoilist size = : ")
            sb.append(list.size)
            for (p in list) {
                sb.append("\npoi= : ")
                sb.append(p.id).append(" ").append(p.name).append(" ").append(p.rank)
            }
        }
        sb.toString().logI()
    }

    override fun onConnectHotSpotMessage(s: String, i: Int) {}

    /**
     * 定位成功
     *
     * @param location BDLocation
     */
    private fun locationSuccess(location: BDLocation) {
        onSuccess?.invoke(location)
    }

    /**
     * 定位失败
     *
     * @param location BDLocation
     */
    private fun locationFailure(location: BDLocation) {
        onFailure?.invoke(location)
    }
}