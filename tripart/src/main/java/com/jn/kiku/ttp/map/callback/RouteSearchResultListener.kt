package com.jn.kiku.ttp.map.callback

import com.baidu.mapapi.search.route.BikingRouteLine
import com.baidu.mapapi.search.route.DrivingRouteLine
import com.baidu.mapapi.search.route.TransitRouteLine
import com.baidu.mapapi.search.route.WalkingRouteLine

/**
 * Author：Stevie.Chen Time：2020/09/08 11:20
 * Class Comment：路线搜索结果监听
 */
interface RouteSearchResultListener {
    enum class Type {
        DRIVING, TRANSIT, BIKING, WALKING
    }

    fun onDrivingSuccess(rLines: List<DrivingRouteLine?>?)
    fun onTransitSuccess(rLines: List<TransitRouteLine?>?)
    fun onBikingSuccess(rLines: List<BikingRouteLine?>?)
    fun onWalkingSuccess(rLines: List<WalkingRouteLine?>?)
    fun onFailure(type: Type?)
}