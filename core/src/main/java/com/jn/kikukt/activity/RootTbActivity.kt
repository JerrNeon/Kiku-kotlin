package com.jn.kikukt.activity

import android.os.Bundle
import android.view.View
import com.jn.kikukt.R
import com.jn.kikukt.utils.setStatusBar
import com.jn.kikukt.widget.TitleBarLayout

/**
 * Author：Stevie.Chen Time：2019/7/10
 * Class Comment：
 */
abstract class RootTbActivity(open val layoutResId: Int) :
    RootActivity(R.layout.common_titlebar_layout) {

    protected var titleBarLayout: TitleBarLayout? = null//root layout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initStatusBar()
        initRootTbView()
    }

    open fun initStatusBar() {
        setStatusBar()
    }

    /**
     * init titleBar View
     */
    open fun initRootTbView() {
        titleBarLayout = findViewById<TitleBarLayout>(R.id.tbl_root).apply {
            setTitleSource(contentLayoutResId = layoutResId)
            onTitleClick(onLeadingClick = { finish() })
        }
    }

    /**
     * set title
     *
     * @param titleText     title content
     * @param isShowDivider is or not show divider
     */
    protected open fun setTitleText(titleText: String, isShowDivider: Boolean = false) {
        setTitleSource(
            titleText = titleText,
            dividerVisibility = if (isShowDivider) View.VISIBLE else View.GONE
        )
    }

    /**
     * set title resource
     */
    protected fun setTitleSource(
        bgResId: Int? = null,
        leadingResId: Int? = null,
        titleHeight: Float? = null,
        titleTextSize: Float? = null,
        titleTextColorResId: Int? = null,
        titleText: String? = null,
        menuLayoutResId: Int? = null,
        dividerColorResId: Int? = null,
        dividerHeight: Float? = null,
        dividerVisibility: Int? = null,
        contentLayoutResId: Int? = null
    ) {
        titleBarLayout?.setTitleSource(
            bgResId,
            leadingResId,
            titleHeight,
            titleTextSize,
            titleTextColorResId,
            titleText,
            menuLayoutResId,
            dividerColorResId,
            dividerHeight,
            dividerVisibility,
            contentLayoutResId
        )
    }

    /**
     * title click event
     */
    protected fun onTitleClick(
        onLeadingClick: ((v: View) -> Unit)? = null,
        onMenuClick: ((v: View) -> Unit)? = null
    ) {
        titleBarLayout?.onTitleClick(onLeadingClick, onMenuClick)
    }
}