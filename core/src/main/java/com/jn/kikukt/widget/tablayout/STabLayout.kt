package com.jn.kikukt.widget.tablayout

import android.content.Context
import com.google.android.material.tabs.TabLayout
import androidx.core.view.ViewCompat
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.jn.kikukt.common.utils.dp2px
import com.jn.kikukt.common.utils.getScreenWidth

/**
 * Author：Stevie.Chen Time：2019/7/12
 * Class Comment：
 */
class STabLayout : TabLayout {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun addTab(tab: Tab, position: Int, setSelected: Boolean) {
        super.addTab(tab, position, setSelected)
        setWidth(tab)
    }

    override fun addTab(tab: Tab, setSelected: Boolean) {
        super.addTab(tab, setSelected)
        setWidth(tab)
    }

    /**
     * 设置每个Tab的宽度
     *
     * @param tab
     */
    private fun setWidth(tab: Tab) {
        if (tab.customView == null) {
            val tabGroup = getChildAt(0) as ViewGroup
            val tabContainer = tabGroup.getChildAt(tab.position) as ViewGroup
            val parentView = tabContainer.parent as View
            ViewCompat.setPaddingRelative(parentView, 0, 0, context.dp2px(26f).toInt(), 0)//为TabLayout添加paddingEnd
            val textView = tabContainer.getChildAt(1) as TextView
            textView.minimumWidth = (context.getScreenWidth() * 0.15).toInt()//设置每一个tab的宽度
        } else {
            val customView = tab.customView
            val parentView = customView!!.parent as View
            ViewCompat.setPaddingRelative(parentView, 0, 0, context.dp2px(26f).toInt(), 0)//为TabLayout添加paddingEnd
            customView.minimumWidth = (context.getScreenWidth() * 0.15).toInt()//设置每一个tab的宽度
        }
    }
}