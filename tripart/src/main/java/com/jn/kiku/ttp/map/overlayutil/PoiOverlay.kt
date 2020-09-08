package com.jn.kiku.ttp.map.overlayutil

import android.os.Bundle
import com.baidu.mapapi.map.*
import com.baidu.mapapi.search.poi.PoiResult
import java.util.*

/**
 * 用于显示poi的overly
 */
class PoiOverlay(baiduMap: BaiduMap?) : OverlayManager(baiduMap) {

    companion object {
        private const val MAX_POI_SIZE = 10
    }

    /**
     * 获取该 PoiOverlay 的 poi数据
     *
     * @return
     */
    var poiResult: PoiResult? = null
        private set

    /**
     * 设置POI数据
     *
     * @param poiResult
     * 设置POI数据
     */
    fun setData(poiResult: PoiResult?) {
        this.poiResult = poiResult
    }

    override val overlayOptions: List<OverlayOptions>?
        get() {
            if (poiResult == null || poiResult!!.allPoi == null) {
                return null
            }
            val markerList: MutableList<OverlayOptions> = ArrayList()
            var markerSize = 0
            var i = 0
            while (i < poiResult!!.allPoi.size
                && markerSize < MAX_POI_SIZE
            ) {
                if (poiResult!!.allPoi[i].location == null) {
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
                        .position(poiResult!!.allPoi[i].location)
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
     * [PoiResult.getAllPoi] 中的索引
     * @return
     */
    fun onPoiClick(i: Int): Boolean {
//        if (mPoiResult.getAllPoi() != null
//                && mPoiResult.getAllPoi().get(i) != null) {
//            Toast.makeText(BMapManager.getInstance().getContext(),
//                    mPoiResult.getAllPoi().get(i).name, Toast.LENGTH_LONG)
//                    .show();
//        }
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
}