package com.jn.kiku.ttp.map

import com.baidu.location.BDAbstractLocationListener
import com.baidu.location.BDLocation
import com.baidu.location.LocationClient
import com.baidu.location.LocationClientOption
import com.jn.kiku.ttp.map.callback.BDLocationListenerIml
import com.jn.kikukt.common.utils.ContextUtils

/**
 * Author：Stevie.Chen Time：2020/09/08 11:48
 * Class Comment：百度地图管理类
 */
object BaiDuMapManage {
    private var mLocationClient: LocationClient? = null //定位关键类
    private var mServiceLocationClient: LocationClient? = null //定位关键类
    private lateinit var mBDLocationListener: BDAbstractLocationListener

    /**
     * 初始化定位相关类和参数
     */
    private fun initLoc(
        onSuccess: ((location: BDLocation) -> Unit)? = null,
        onFailure: ((location: BDLocation) -> Unit)? = null
    ) {
        if (mLocationClient == null) mLocationClient =
            LocationClient(ContextUtils.context)
        mBDLocationListener = BDLocationListenerIml(onSuccess = { location ->
            onSuccess?.invoke(location)
            mLocationClient?.unRegisterLocationListener(mBDLocationListener)
        }, onFailure = { location ->
            onFailure?.invoke(location)
            mLocationClient?.unRegisterLocationListener(mBDLocationListener)
        })
        mLocationClient?.registerLocationListener(mBDLocationListener)
        //定位参数
        val mOption = getLocationClientOption(0)
        mLocationClient?.locOption = mOption
    }

    /**
     * 初始化定位相关类和参数
     */
    private fun initLoc(
        scanSpanTime: Int,
        onSuccess: ((location: BDLocation) -> Unit)? = null,
        onFailure: ((location: BDLocation) -> Unit)? = null
    ) {
        if (mServiceLocationClient == null) mServiceLocationClient =
            LocationClient(ContextUtils.context)
        val mBDLocationListener = BDLocationListenerIml(onSuccess, onFailure)
        mServiceLocationClient!!.registerLocationListener(mBDLocationListener)
        //定位参数
        val mOption = getLocationClientOption(scanSpanTime)
        mServiceLocationClient!!.locOption = mOption
    }

    /**
     * 获取定位参数
     *
     * @param scanSpanTime 设置发起定位请求的间隔，需要大于等于1000ms才是有效的
     * @return LocationClientOption
     */
    private fun getLocationClientOption(scanSpanTime: Int): LocationClientOption {
        val mOption = LocationClientOption()
        //可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        mOption.locationMode = LocationClientOption.LocationMode.Hight_Accuracy
        //可选，默认gcj02，设置返回的定位结果坐标系
        mOption.setCoorType("bd09ll")
        //可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        mOption.setScanSpan(scanSpanTime)
        //可选，设置是否需要地址信息，默认不需要
        mOption.setIsNeedAddress(true)
        //可选，默认false,设置是否使用gps
        mOption.isOpenGps = true
        //可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果
        mOption.isLocationNotify = true
        //可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        mOption.setIsNeedLocationDescribe(true)
        //可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        mOption.setIsNeedLocationPoiList(true)
        //可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        mOption.setIgnoreKillProcess(false)
        //可选，默认false，设置是否收集CRASH信息，默认收集
        mOption.SetIgnoreCacheException(false)
        //可选，默认false，设置是否需要过滤GPS仿真结果，默认需要
        mOption.setEnableSimulateGps(false)
        return mOption
    }

    /**
     * 开始定位
     */
    fun startLoc(
        onSuccess: ((location: BDLocation) -> Unit)? = null,
        onFailure: ((location: BDLocation) -> Unit)? = null
    ) {
        initLoc(onSuccess, onFailure)
        startLoc()
    }

    /**
     * 开始定位
     *
     * @param scanSpanTime 定位间隔时间点，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
     */
    fun startLoc(
        scanSpanTime: Int,
        onSuccess: ((location: BDLocation) -> Unit)? = null,
        onFailure: ((location: BDLocation) -> Unit)? = null
    ) {
        initLoc(scanSpanTime, onSuccess, onFailure)
        startServiceLoc()
    }

    /**
     * 开始定位
     */
    private fun startLoc() {
        if (mLocationClient != null) mLocationClient?.start()
    }

    /**
     * 停止定位
     */
    fun stopLoc() {
        if (mLocationClient != null) mLocationClient?.stop()
    }

    /**
     * 开始定位
     */
    private fun startServiceLoc() {
        if (mServiceLocationClient != null) mServiceLocationClient?.start()
    }

    /**
     * 停止定位
     */
    fun stopServiceLoc() {
        if (mServiceLocationClient != null) mServiceLocationClient?.stop()
    }

    fun onDestroy() {
        if (mLocationClient != null) mLocationClient = null
    }

    fun onDestroyService() {
        if (mServiceLocationClient != null) mServiceLocationClient = null
    }
}