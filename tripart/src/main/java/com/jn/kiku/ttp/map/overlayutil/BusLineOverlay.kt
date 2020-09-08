package com.jn.kiku.ttp.map.overlayutil

import android.graphics.Color
import android.util.Log
import com.baidu.mapapi.map.*
import com.baidu.mapapi.model.LatLng
import com.baidu.mapapi.search.busline.BusLineResult
import java.util.*

/**
 * 用于显示一条公交详情结果的Overlay
 */
class BusLineOverlay
/**
 * 构造函数
 *
 * @param baiduMap
 * 该BusLineOverlay所引用的 BaiduMap 对象
 */
    (baiduMap: BaiduMap?) : OverlayManager(baiduMap) {
    private var mBusLineResult: BusLineResult? = null

    /**
     * 设置公交线数据
     *
     * @param result
     * 公交线路结果数据
     */
    fun setData(result: BusLineResult?) {
        mBusLineResult = result
    }

    override val overlayOptions: List<OverlayOptions>?
        get() {
            if (mBusLineResult == null || mBusLineResult!!.stations == null) {
                return null
            }
            val overlayOptionses: MutableList<OverlayOptions> = ArrayList()
            for (station in mBusLineResult!!.stations) {
                overlayOptionses.add(
                    MarkerOptions()
                        .position(station.location)
                        .zIndex(10)
                        .anchor(0.5f, 0.5f)
                        .icon(
                            BitmapDescriptorFactory
                                .fromAssetWithDpi("Icon_bus_station.png")
                        )
                )
            }
            val points: MutableList<LatLng> = ArrayList()
            for (step in mBusLineResult!!.steps) {
                if (step.wayPoints != null) {
                    points.addAll(step.wayPoints)
                }
            }
            if (points.size > 0) {
                overlayOptionses
                    .add(
                        PolylineOptions().width(10)
                            .color(Color.argb(178, 0, 78, 255)).zIndex(0)
                            .points(points)
                    )
            }
            return overlayOptionses
        }

    /**
     * 覆写此方法以改变默认点击行为
     *
     * @param index
     * 被点击的站点在
     * [BusLineResult.getStations]
     * 中的索引
     * @return 是否处理了该点击事件
     */
    fun onBusStationClick(index: Int): Boolean {
        if (mBusLineResult!!.stations != null
            && mBusLineResult!!.stations[index] != null
        ) {
            Log.i("baidumapsdk", "BusLineOverlay onBusStationClick")
        }
        return false
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        return if (mOverlayList != null && mOverlayList!!.contains(marker)) {
            onBusStationClick(mOverlayList!!.indexOf(marker))
        } else {
            false
        }
    }

    override fun onPolylineClick(polyline: Polyline): Boolean {
        return false
    }
}