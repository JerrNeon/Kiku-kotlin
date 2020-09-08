/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.jn.kiku.ttp.map.overlayutil

import android.graphics.Color
import android.os.Bundle
import com.baidu.mapapi.map.*
import com.baidu.mapapi.model.LatLng
import com.baidu.mapapi.search.route.IndoorRouteLine
import java.util.*

class IndoorRouteOverlay(baiduMap: BaiduMap?) : OverlayManager(baiduMap) {
    private var mRouteLine: IndoorRouteLine? = null
    var colorInfo: IntArray

    /**
     * 设置路线数据
     *
     * @param routeOverlay
     * 路线数据
     */
    fun setData(routeOverlay: IndoorRouteLine?) {
        mRouteLine = routeOverlay
    }

    /**
     * 覆写此方法以改变默认起点图标
     *
     * @return 起点图标
     */
    val startMarker: BitmapDescriptor?
        get() = null

    /**
     * 覆写此方法以改变默认终点图标
     *
     * @return 终点图标
     */
    val terminalMarker: BitmapDescriptor?
        get() = null
    val lineColor: Int
        get() = 0// 最后路段绘制出口点
    // 添加起点starting
    // 添加终点terminal

    // 添加线poly line list
    // 添加step的节点
    override val overlayOptions: List<OverlayOptions>?
        get() {
            if (mRouteLine == null) {
                return null
            }
            val overlayList: MutableList<OverlayOptions> = ArrayList()


            // 添加step的节点
            if (mRouteLine!!.allStep != null && mRouteLine!!.allStep.size > 0) {
                for (step in mRouteLine!!.allStep) {
                    val b = Bundle()
                    b.putInt("index", mRouteLine!!.allStep.indexOf(step))
                    if (step.entrace != null) {
                        overlayList.add(
                            MarkerOptions().position(step.entrace.location)
                                .zIndex(10).anchor(0.5f, 0.5f).extraInfo(b)
                                .icon(BitmapDescriptorFactory.fromAssetWithDpi("Icon_walk_route.png"))
                        )
                    }

                    // 最后路段绘制出口点
                    if (mRouteLine!!.allStep.indexOf(step) == mRouteLine!!.allStep.size - 1
                        && step.exit != null
                    ) {
                        overlayList.add(
                            MarkerOptions().position(step.exit.location).anchor(0.5f, 0.5f)
                                .zIndex(10)
                                .icon(BitmapDescriptorFactory.fromAssetWithDpi("Icon_walk_route.png"))
                        )
                    }
                }
            }
            // 添加起点starting
            if (mRouteLine!!.starting != null) {
                overlayList.add(
                    MarkerOptions().position(mRouteLine!!.starting.location)
                        .icon(
                            if (startMarker != null) startMarker else BitmapDescriptorFactory.fromAssetWithDpi(
                                "Icon_start.png"
                            )
                        )
                        .zIndex(10)
                )
            }
            // 添加终点terminal
            if (mRouteLine!!.terminal != null) {
                overlayList.add(
                    MarkerOptions().position(mRouteLine!!.terminal.location)
                        .icon(
                            if (terminalMarker != null) terminalMarker else BitmapDescriptorFactory.fromAssetWithDpi(
                                "Icon_end.png"
                            )
                        )
                        .zIndex(10)
                )
            }

            // 添加线poly line list
            if (mRouteLine!!.allStep != null && mRouteLine!!.allStep.size > 0) {
                var lastStepLastPoint: LatLng? = null
                var idex = 0
                for (step in mRouteLine!!.allStep) {
                    val watPoints = step.wayPoints
                    if (watPoints != null) {
                        val points: MutableList<LatLng> = ArrayList()
                        if (lastStepLastPoint != null) {
                            points.add(lastStepLastPoint)
                        }
                        points.addAll(watPoints)
                        overlayList.add(
                            PolylineOptions().points(points).width(10)
                                .color(if (lineColor != 0) lineColor else colorInfo[idex++ % 3])
                                .zIndex(0)
                        )
                        lastStepLastPoint = watPoints[watPoints.size - 1]
                    }
                }
            }
            return overlayList
        }

    //    private BitmapDescriptor getIconForStep(IndoorRouteLine.TransitStep step) {
    //        switch (step.getVehileType()) {
    //            case ESTEP_WALK:
    //                return BitmapDescriptorFactory.fromAssetWithDpi("Icon_walk_route.png");
    //            case ESTEP_TRAIN:
    //                return BitmapDescriptorFactory.fromAssetWithDpi("Icon_subway_station.png");
    //            case ESTEP_DRIVING:
    //            case ESTEP_COACH:
    //            case ESTEP_PLANE:
    //            case ESTEP_BUS:
    //                return BitmapDescriptorFactory.fromAssetWithDpi("Icon_bus_station.png");
    //            default:
    //                return null;
    //        }
    //    }
    override fun onMarkerClick(marker: Marker): Boolean {
        return false
    }

    override fun onPolylineClick(polyline: Polyline): Boolean {
        return false
    }

    /**
     * 构造函数
     *
     * @param baiduMap
     * 该TransitRouteOverlay引用的 BaiduMap 对象
     */
    init {
        colorInfo = intArrayOf(
            Color.argb(178, 0, 78, 255), Color.argb(178, 88, 208, 0), Color.argb(
                178, 88, 78,
                255
            )
        )
    }
}