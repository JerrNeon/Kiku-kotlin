package com.jn.kikukt.activity

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import com.jn.kikukt.R
import com.jn.kikukt.adapter.BasePagerAdapter
import com.jn.kikukt.common.SPManage
import com.jn.kikukt.common.api.IGuideView
import com.jn.kikukt.entiy.GuidePageVO
import com.jn.kikukt.utils.glide.displayImage
import kotlinx.android.synthetic.main.common_guide_layout.*

/**
 * Author：Stevie.Chen Time：2019/7/10
 * Class Comment：
 */
abstract class RootGuideActivity : RootActivity(), IGuideView {

    protected var mAdapter: GuidePagerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.common_guide_layout)
        initView()
    }

    override fun initView() {
        super.initView()
        mAdapter = getAdapter()
            .apply {
                onClickListener = View.OnClickListener {
                    handlerSkipEvent()
                }
            }
        val imgResourceIds = getImgResourceIds()
        if (imgResourceIds.isNotEmpty()) {
            for (i in imgResourceIds.indices) {
                mAdapter?.add(
                    GuidePageVO(
                        position = i,
                        imgRes = imgResourceIds[i],
                        imgType = 1,
                        isLast = i == imgResourceIds.size - 1
                    )
                )
            }
        }
        vp_RootGuide.adapter = mAdapter
    }

    open fun getAdapter(): GuidePagerAdapter = GuidePagerAdapter(this)

    open fun handlerSkipEvent() {
        openMainActivity()
        SPManage.instance.setFirstGuide(false)
        finish()
    }

}

class GuidePagerAdapter(activity: Activity) : BasePagerAdapter<GuidePageVO>(activity) {

    var onClickListener: View.OnClickListener? = null

    override val layoutResourceId: Int
        get() = R.layout.common_guideitem_layout

    override fun getView(view: View?, position: Int, bean: GuidePageVO?) {
        val iv = view?.findViewById<ImageView>(R.id.iv_rootGuide)
        bean?.run {
            if (imgType == 0)
                iv?.displayImage(getImageContext()!!, imgUrl ?: "")
            else
                iv?.setImageResource(imgRes)
            iv?.setOnClickListener {
                if (isLast && onClickListener != null) {
                    onClickListener?.onClick(it)
                }
            }
        }
    }

}

