package com.jn.kiku.ttp.map.overlayutil

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import com.baidu.mapapi.map.*
import com.baidu.mapapi.model.LatLng
import com.baidu.mapapi.search.route.DrivingRouteLine
import java.util.*

/**
 * 用于显示一条驾车路线的overlay，自3.4.0版本起可实例化多个添加在地图中显示，当数据中包含路况数据时，则默认使用路况纹理分段绘制
 */
class DrivingRouteOverlay(baiduMap: BaiduMap?) : OverlayManager(baiduMap) {
    private var mRouteLine: DrivingRouteLine? = null
    private var focus: Boolean = false

    override val overlayOptions: List<OverlayOptions>?
        get() {
            if (mRouteLine == null) {
                return null
            }
            val overlayOptionses: MutableList<OverlayOptions> = ArrayList()
            // step node
            if (mRouteLine!!.allStep != null
                && mRouteLine!!.allStep.size > 0
            ) {
                for (step in mRouteLine!!.allStep) {
                    val b = Bundle()
                    b.putInt("index", mRouteLine!!.allStep.indexOf(step))
                    if (step.entrance != null) {
                        overlayOptionses.add(
                            MarkerOptions()
                                .position(step.entrance.location)
                                .anchor(0.5f, 0.5f)
                                .zIndex(10)
                                .rotate((360 - step.direction).toFloat())
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
                        overlayOptionses.add(
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
            if (mRouteLine!!.starting != null) {
                overlayOptionses.add(
                    MarkerOptions()
                        .position(mRouteLine!!.starting.location)
                        .icon(
                            if (getStartMarker() != null) getStartMarker() else BitmapDescriptorFactory
                                .fromAssetWithDpi("Icon_start.png")
                        ).zIndex(10)
                )
            }
            if (mRouteLine!!.terminal != null) {
                overlayOptionses
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
            // poly line
            if (mRouteLine!!.allStep != null
                && mRouteLine!!.allStep.size > 0
            ) {
                val steps = mRouteLine!!.allStep
                val stepNum = steps.size
                val points: MutableList<LatLng> = ArrayList()
                val traffics = ArrayList<Int>()
                var totalTraffic = 0
                for (i in 0 until stepNum) {
                    if (i == stepNum - 1) {
                        points.addAll(steps[i].wayPoints)
                    } else {
                        points.addAll(steps[i].wayPoints.subList(0, steps[i].wayPoints.size - 1))
                    }
                    totalTraffic += steps[i].wayPoints.size - 1
                    if (steps[i].trafficList != null && steps[i].trafficList.size > 0) {
                        for (j in steps[i].trafficList.indices) {
                            traffics.add(steps[i].trafficList[j])
                        }
                    }
                }

//            Bundle indexList = new Bundle();
//            if (traffics.size() > 0) {
//                int raffic[] = new int[traffics.size()];
//                int index = 0;
//                for (Integer tempTraff : traffics) {
//                    raffic[index] = tempTraff.intValue();
//                    index++;
//                }
//                indexList.putIntArray("indexs", raffic);
//            }
                var isDotLine = false
                if (traffics.size > 0) {
                    isDotLine = true
                }
                val option = PolylineOptions().points(points).textureIndex(traffics)
                    .width(7).dottedLine(isDotLine).focus(true)
                    .color(if (getLineColor() != 0) getLineColor() else Color.argb(178, 0, 78, 255))
                    .zIndex(0)
                if (isDotLine) {
                    option.customTextureList(getCustomTextureList())
                }
                overlayOptionses.add(option)
            }
            return overlayOptionses
        }

    /**
     * 设置路线数据
     *
     * @param routeLine
     * 路线数据
     */
    fun setData(routeLine: DrivingRouteLine?) {
        mRouteLine = routeLine
    }

    /**
     * 覆写此方法以改变默认起点图标
     *
     * @return 起点图标
     */
    fun getStartMarker(): BitmapDescriptor? {
        return null
    }

    /**
     * 覆写此方法以改变默认绘制颜色
     * @return 线颜色
     */
    fun getLineColor(): Int {
        return 0
    }

    fun getCustomTextureList(): List<BitmapDescriptor> {
        val list = ArrayList<BitmapDescriptor>()
        list.add(BitmapDescriptorFactory.fromAsset("Icon_road_blue_arrow.png"))
        list.add(BitmapDescriptorFactory.fromAsset("Icon_road_green_arrow.png"))
        list.add(BitmapDescriptorFactory.fromAsset("Icon_road_yellow_arrow.png"))
        list.add(BitmapDescriptorFactory.fromAsset("Icon_road_red_arrow.png"))
        list.add(BitmapDescriptorFactory.fromAsset("Icon_road_nofocus.png"))
        return list
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
     * 覆写此方法以改变默认点击处理
     *
     * @param i
     * 线路节点的 index
     * @return 是否处理了该点击事件
     */
    fun onRouteNodeClick(i: Int): Boolean {
        if (mRouteLine!!.allStep != null
            && mRouteLine!!.allStep[i] != null
        ) {
            Log.i("baidumapsdk", "DrivingRouteOverlay onRouteNodeClick")
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
        var flag = false
        for (mPolyline in mOverlayList!!) {
            if (mPolyline is Polyline && mPolyline == polyline) {
                // 选中
                flag = true
                break
            }
        }
        setFocus(flag)
        return true
    }

    fun setFocus(flag: Boolean) {
        focus = flag
        for (mPolyline in mOverlayList!!) {
            if (mPolyline is Polyline) {
                // 选中
                mPolyline.isFocus = flag
                break
            }
        }
    }
}