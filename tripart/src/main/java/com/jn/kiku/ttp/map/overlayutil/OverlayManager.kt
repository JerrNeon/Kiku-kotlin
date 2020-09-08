package com.jn.kiku.ttp.map.overlayutil

import com.baidu.mapapi.map.*
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener
import com.baidu.mapapi.map.BaiduMap.OnPolylineClickListener
import com.baidu.mapapi.model.LatLngBounds
import java.util.*

/**
 * 该类提供一个能够显示和管理多个Overlay的基类
 *
 *
 * 复写[.getOverlayOptions] 设置欲显示和管理的Overlay列表
 *
 *
 *
 * 通过
 * [BaiduMap.setOnMarkerClickListener]
 * 将覆盖物点击事件传递给OverlayManager后，OverlayManager才能响应点击事件。
 *
 *
 * 复写[.onMarkerClick] 处理Marker点击事件
 *
 */
abstract class OverlayManager(baiduMap: BaiduMap?) : OnMarkerClickListener,
    OnPolylineClickListener {
    var mBaiduMap: BaiduMap? = null
    private var mOverlayOptionList: MutableList<OverlayOptions>? = null
    var mOverlayList: MutableList<Overlay>? = null

    /**
     * 覆写此方法设置要管理的Overlay列表
     *
     * @return 管理的Overlay列表
     */
    abstract val overlayOptions: List<OverlayOptions>?

    /**
     * 将所有Overlay 添加到地图上
     */
    fun addToMap() {
        if (mBaiduMap == null) {
            return
        }
        removeFromMap()
        val overlayOptions = overlayOptions
        if (overlayOptions != null) {
            mOverlayOptionList!!.addAll(overlayOptions)
        }
        for (option in mOverlayOptionList!!) {
            mOverlayList!!.add(mBaiduMap!!.addOverlay(option))
        }
    }

    /**
     * 将所有Overlay 从 地图上消除
     */
    fun removeFromMap() {
        if (mBaiduMap == null) {
            return
        }
        for (marker in mOverlayList!!) {
            marker.remove()
        }
        mOverlayOptionList!!.clear()
        mOverlayList!!.clear()
    }

    /**
     * 缩放地图，使所有Overlay都在合适的视野内
     *
     *
     * 注： 该方法只对Marker类型的overlay有效
     *
     *
     */
    fun zoomToSpan() {
        if (mBaiduMap == null) {
            return
        }
        if (mOverlayList!!.size > 0) {
            val builder = LatLngBounds.Builder()
            for (overlay in mOverlayList!!) {
                // polyline 中的点可能太多，只按marker 缩放
                if (overlay is Marker) {
                    builder.include(overlay.position)
                }
            }
            mBaiduMap!!.setMapStatus(
                MapStatusUpdateFactory
                    .newLatLngBounds(builder.build())
            )
        }
    }

    /**
     * 通过一个BaiduMap 对象构造
     *
     * @param baiduMap
     */
    init {
        mBaiduMap = baiduMap
        // mBaiduMap.setOnMarkerClickListener(this);
        if (mOverlayOptionList == null) {
            mOverlayOptionList = ArrayList()
        }
        if (mOverlayList == null) {
            mOverlayList = ArrayList()
        }
    }
}