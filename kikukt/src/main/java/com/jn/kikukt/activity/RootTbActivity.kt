package com.jn.kikukt.activity

import android.os.Bundle
import android.support.annotation.LayoutRes
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.jn.kikukt.R
import kotlinx.android.synthetic.main.common_titlebar_layout.*

/**
 * Author：Stevie.Chen Time：2019/7/10
 * Class Comment：
 */
abstract class RootTbActivity : RootActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.common_titlebar_layout)
        setRootContainerView()
        setStatusBar()
    }

    @LayoutRes
    abstract fun getLayoutResourceId(): Int

    /**
     * set main content View
     */
    fun setRootContainerView() {
        if (getLayoutResourceId() != 0) {
            val contentView = LayoutInflater.from(mContext).inflate(getLayoutResourceId(), null, false)
            val drawable = contentView.background
            if (drawable == null)
                contentView.setBackgroundResource(R.color.white)
            val lp = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT,
                1f
            )
            ll_commonTitleBar.addView(contentView, lp)
        }
    }
}