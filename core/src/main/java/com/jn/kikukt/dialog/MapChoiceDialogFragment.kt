package com.jn.kikukt.dialog

import android.os.Bundle
import android.view.View
import com.jn.kikukt.R
import com.jn.kikukt.common.utils.LatLng
import com.jn.kikukt.common.utils.MapUtils
import kotlinx.android.synthetic.main.dialog_mapchoice.view.*

/**
 * Author：Stevie.Chen Time：2019/7/15
 * Class Comment：地图选择对话框
 */
class MapChoiceDialogFragment : RootDialogFragment() {

    companion object {
        private const val ROUTE_ORIGINLATITUDE = "originLatitude"
        private const val ROUTE_ORIGINLONGITUDE = "originLongitude"
        private const val ROUTE_DESTINATIONLATITUDE = "destinationLatitude"
        private const val ROUTE_DESTINATIONLONGITUDE = "destinationLongitude"

        fun newInstance(
            originLatitude: Double, originLongitude: Double,
            destinationLatitude: Double, destinationLongitude: Double
        ): MapChoiceDialogFragment {
            val fragment = MapChoiceDialogFragment()
            val bundle = Bundle()
            bundle.putDouble(ROUTE_ORIGINLATITUDE, originLatitude)
            bundle.putDouble(ROUTE_ORIGINLONGITUDE, originLongitude)
            bundle.putDouble(ROUTE_DESTINATIONLATITUDE, destinationLatitude)
            bundle.putDouble(ROUTE_DESTINATIONLONGITUDE, destinationLongitude)
            fragment.arguments = bundle
            return fragment
        }
    }

    private lateinit var originLl: LatLng//初始地
    private lateinit var destinationLl: LatLng//目的地

    override val layoutResourceId: Int = R.layout.dialog_mapchoice

    override val animationStyle: Int = R.style.bottom_in_out

    override fun initView() {
        super.initView()
        mView?.run {
            tv_google.setOnClickListener(this@MapChoiceDialogFragment)
            tv_baiduMap.setOnClickListener(this@MapChoiceDialogFragment)
            tv_autoNavi.setOnClickListener(this@MapChoiceDialogFragment)
            tv_cancel.setOnClickListener(this@MapChoiceDialogFragment)
        }
    }

    override fun initData() {
        super.initData()
        arguments?.run {
            val originLatitude = getDouble(ROUTE_ORIGINLATITUDE, 0.0)
            val originLongitude = getDouble(ROUTE_ORIGINLONGITUDE, 0.0)
            val destinationLatitude = getDouble(ROUTE_DESTINATIONLATITUDE, 0.0)
            val destinationLongitude = getDouble(ROUTE_DESTINATIONLONGITUDE, 0.0)
            originLl = LatLng(originLatitude, originLongitude)
            destinationLl = LatLng(destinationLatitude, destinationLongitude)
        }
    }

    override fun onClick(view: View) {
        super.onClick(view)
        when (view.id) {
            R.id.tv_google -> {//Google
                MapUtils.openGoogleMap(
                    requireActivity(),
                    MapUtils.MapType.DRIVING,
                    originLl,
                    destinationLl
                )
            }
            R.id.tv_baiduMap -> {//百度地图
                MapUtils.openBaiduMap(
                    requireActivity(),
                    MapUtils.MapType.DRIVING,
                    originLl,
                    destinationLl
                )
            }
            R.id.tv_autoNavi -> {//高德地图
                MapUtils.openAutoNaviMap(
                    requireActivity(),
                    MapUtils.MapType.DRIVING,
                    originLl,
                    destinationLl
                )
            }
            R.id.tv_cancel -> {
                //
            }
        }
        dismissAllowingStateLoss()
    }
}