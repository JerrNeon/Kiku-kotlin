package com.jn.kiku.ttp.map.overlayutil

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import com.baidu.mapapi.map.*
import com.baidu.mapapi.model.LatLng
import com.baidu.mapapi.search.route.WalkingRouteLine
import java.util.*

/**
 * 用于显示步行路线的overlay，自3.4.0版本起可实例化多个添加在地图中显示
 */
class WalkingRouteOverlay(baiduMap: BaiduMap?) : OverlayManager(baiduMap) {
    private var mRouteLine: WalkingRouteLine? = null

    /**
     * 设置路线数据。
     *
     * @param line
     * 路线数据
     */
    fun setData(line: WalkingRouteLine?) {
        mRouteLine = line
    }

    override val overlayOptions: List<OverlayOptions>?
        get() {
            if (mRouteLine == null) {
                return null
            }
            val overlayList: MutableList<OverlayOptions> = ArrayList()
            if (mRouteLine!!.allStep != null
                && mRouteLine!!.allStep.size > 0
            ) {
                for (step in mRouteLine!!.allStep) {
                    val b = Bundle()
                    b.putInt("index", mRouteLine!!.allStep.indexOf(step))
                    if (step.entrance != null) {
                        overlayList.add(
                            MarkerOptions()
                                .position(step.entrance.location)
                                .rotate((360 - step.direction).toFloat())
                                .zIndex(10)
                                .anchor(0.5f, 0.5f)
                                .extraInfo(b)
                                .icon(
                                    BitmapDescriptorFactory
                                        .fromAssetWithDpi("Icon_line_node.png")
                                )
                        )
                    }

                    // 最后路段绘制出口点
                    if (mRouteLine!!.allStep.indexOf(step) == mRouteLine!!
                            .allStep.size - 1 && step.exit != null
                    ) {
                        overlayList.add(
                            MarkerOptions()
                                .position(step.exit.location)
                                .anchor(0.5f, 0.5f)
                                .zIndex(10)
                                .icon(
                                    BitmapDescriptorFactory
                                        .fromAssetWithDpi("Icon_line_node.png")
                                )
                        )
                    }
                }
            }
            // starting
            if (mRouteLine!!.starting != null) {
                overlayList.add(
                    MarkerOptions()
                        .position(mRouteLine!!.starting.location)
                        .icon(
                            if (getStartMarker() != null) getStartMarker() else BitmapDescriptorFactory
                                .fromAssetWithDpi("Icon_start.png")
                        ).zIndex(10)
                )
            }
            // terminal
            if (mRouteLine!!.terminal != null) {
                overlayList
                    .add(
                        MarkerOptions()
                            .position(mRouteLine!!.terminal.location)
                            .icon(
                                if (getTerminalMarker() != null) getTerminalMarker() else BitmapDescriptorFactory
                                    .fromAssetWithDpi("Icon_end.png")
                            )
                            .zIndex(10)
                    )
            }

            // poly line list
            if (mRouteLine!!.allStep != null
                && mRouteLine!!.allStep.size > 0
            ) {
                var lastStepLastPoint: LatLng? = null
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
                                .color(
                                    if (getLineColor() != 0) getLineColor() else Color.argb(
                                        178,
                                        0,
                                        78,
                                        255
                                    )
                                ).zIndex(0)
                        )
                        lastStepLastPoint = watPoints[watPoints.size - 1]
                    }
                }
            }
            return overlayList
        }

    /**
     * 覆写此方法以改变默认起点图标
     *
     * @return 起点图标
     */
    fun getStartMarker(): BitmapDescriptor? {
        return null
    }

    fun getLineColor(): Int {
        return 0
    }

    /**
     * 覆写此方法以改变默认终点图标
     *
     * @return 终点图标
     */
    fun getTerminalMarker(): BitmapDescriptor? {
        return null
    }

    /**
     * 处理点击事件
     *
     * @param i
     * 被点击的step在
     * [WalkingRouteLine.getAllStep]
     * 中的索引
     * @return 是否处理了该点击事件
     */
    fun onRouteNodeClick(i: Int): Boolean {
        if (mRouteLine!!.allStep != null
            && mRouteLine!!.allStep[i] != null
        ) {
            Log.i("baidumapsdk", "WalkingRouteOverlay onRouteNodeClick")
        }
        return false
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        for (mMarker in mOverlayList!!) {
            if (mMarker is Marker && mMarker == marker) {
                if (marker.extraInfo != null) {
                    onRouteNodeClick(marker.extraInfo.getInt("index"))
                }
            }
        }
        return true
    }

    override fun onPolylineClick(polyline: Polyline): Boolean {
        return false
    }
}