package com.jn.kikukt.activity

import android.os.Bundle
import androidx.viewbinding.ViewBinding
import androidx.viewpager2.widget.ViewPager2
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jn.kikukt.R
import com.jn.kikukt.adapter.BaseRvAdapter
import com.jn.kikukt.adapter.loadImage
import com.jn.kikukt.common.SPManage
import com.jn.kikukt.common.api.IGuideView
import com.jn.kikukt.databinding.CommonGuideLayoutBinding
import com.jn.kikukt.entiy.GuidePageVO

/**
 * Author：Stevie.Chen Time：2019/7/10
 * Class Comment：
 */
abstract class RootGuideActivity : RootActivity(), IGuideView {

    protected lateinit var mAdapter: BaseRvAdapter<GuidePageVO>
    override val viewBinding: ViewBinding by lazy { CommonGuideLayoutBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
    }

    override fun initView() {
        super.initView()
        mAdapter = getAdapter()
        val imgResourceIds = getImgResourceIds()
        if (imgResourceIds.isNotEmpty()) {
            for (i in imgResourceIds.indices) {
                mAdapter.addData(
                    GuidePageVO(
                        position = i,
                        imgRes = imgResourceIds[i],
                        imgType = 1,
                        isLast = i == imgResourceIds.size - 1
                    )
                )
            }
        }
        viewBinding.root.findViewById<ViewPager2>(R.id.vp_RootGuide).adapter = mAdapter
    }

    open fun getAdapter(): BaseRvAdapter<GuidePageVO> =
        object : BaseRvAdapter<GuidePageVO>(R.layout.common_guideitem_layout) {
            override fun convert(holder: BaseViewHolder, item: GuidePageVO) {
                item.run {
                    if (imgType == 0)
                        holder.loadImage(R.id.iv_rootGuide, imgUrl ?: "")
                    else
                        holder.setImageResource(R.id.iv_rootGuide, imgRes)
                }
            }
        }.apply {
            setOnItemClickListener { _, _, position ->
                if (mAdapter.getItemOrNull(position)?.isLast == true)
                    handlerSkipEvent()
            }
        }

    open fun handlerSkipEvent() {
        openMainActivity()
        SPManage.instance.isFirstGuide = false
        finish()
    }
}

