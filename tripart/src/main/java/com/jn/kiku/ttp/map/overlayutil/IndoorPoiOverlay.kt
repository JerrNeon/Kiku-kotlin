package com.jn.kiku.ttp.map.overlayutil

import android.os.Bundle
import com.baidu.mapapi.map.*
import com.baidu.mapapi.search.poi.PoiIndoorResult
import java.util.*

/**
 * 用于显示indoorpoi的overly
 */
class IndoorPoiOverlay
/**
 * 构造函数
 *
 * @param baiduMap
 * 该 IndoorPoiOverlay 引用的 BaiduMap 对象
 */
    (baiduMap: BaiduMap?) : OverlayManager(baiduMap) {
    /**
     * 获取该 IndoorPoiOverlay 的 indoorpoi数据
     *
     * @return
     */
    var indoorPoiResult: PoiIndoorResult? = null
        private set

    /**
     * 设置IndoorPoi数据
     *
     * @param indoorpoiResult
     * 设置indoorpoiResult数据
     */
    fun setData(indoorpoiResult: PoiIndoorResult?) {
        indoorPoiResult = indoorpoiResult
    }

    override val overlayOptions: List<OverlayOptions>?
        get() {
            if (indoorPoiResult == null || indoorPoiResult!!.getmArrayPoiInfo() == null) {
                return null
            }
            val markerList: MutableList<OverlayOptions> = ArrayList()
            var markerSize = 0
            var i = 0
            while (i < indoorPoiResult!!.getmArrayPoiInfo().size
                && markerSize < MAX_POI_SIZE
            ) {
                if (indoorPoiResult!!.getmArrayPoiInfo()[i].latLng == null) {
                    i++
                    continue
                }
                markerSize++
                val bundle = Bundle()
                bundle.putInt("index", i)
                markerList.add(
                    MarkerOptions()
                        .icon(
                            BitmapDescriptorFactory.fromAssetWithDpi(
                                "Icon_mark"
                                        + markerSize + ".png"
                            )
                        ).extraInfo(bundle)
                        .position(indoorPoiResult!!.getmArrayPoiInfo()[i].latLng)
                )
                i++
            }
            return markerList
        }

    /**
     * 覆写此方法以改变默认点击行为
     *
     * @param i
     * 被点击的poi在
     * [PoiIndoorResult.getmArrayPoiInfo] } 中的索引
     * @return
     */
    fun onPoiClick(i: Int): Boolean {
        return false
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        if (!mOverlayList!!.contains(marker)) {
            return false
        }
        return if (marker.extraInfo != null) {
            onPoiClick(marker.extraInfo.getInt("index"))
        } else false
    }

    override fun onPolylineClick(polyline: Polyline): Boolean {
        return false
    }

    companion object {
        private const val MAX_POI_SIZE = 10
    }
}