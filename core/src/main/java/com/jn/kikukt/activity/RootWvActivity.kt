package com.jn.kikukt.activity

import android.content.res.Configuration
import android.os.Bundle
import android.view.KeyEvent
import android.widget.FrameLayout
import com.jn.kikukt.R
import com.jn.kikukt.common.api.IWvView
import com.jn.kikukt.utils.WebViewUtils
import kotlinx.android.synthetic.main.common_wv_progress.*
import kotlinx.android.synthetic.main.common_wv_tencent.*

/**
 * Author：Stevie.Chen Time：2019/7/11
 * Class Comment：Android自带的WebView
 */
open class RootWvActivity : RootTbActivity(), IWvView {

    private lateinit var mWebView: Any
    private var mWebViewHeight: Int = 0//WebView height

    override fun getLayoutResourceId(): Int {
        return R.layout.common_wv_tencent
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initWvView()
        setWebViewClient()
    }

    override fun initWvView() {
        mWebView = wv_common
    }

    override fun initWv() {
        WebViewUtils.initWebView(this, mWebView)
    }

    override fun loadHtml(htmlText: String) {
        WebViewUtils.loadDataWithBaseURL(mWebView, htmlText)
    }

    override fun loadUrl(url: String) {
        WebViewUtils.loadUrl(mWebView, url)
    }

    override fun setWebViewClient() {
        WebViewUtils.setWebViewClient(mWebView)
        WebViewUtils.setWebChromeClient(mWebView, pb_wv_common)
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        //video screen back webView bottom will display many white space，this reset webView height on screen switch
        if (mWebView is com.tencent.smtt.sdk.WebView) {
            if (newConfig?.orientation == Configuration.ORIENTATION_LANDSCAPE) {//LANDSCAPE)
                mWebViewHeight = (mWebView as com.tencent.smtt.sdk.WebView).rootView.height
                WebViewUtils.removeVideoChildView(mActivity)//hide video top view
            } else if (newConfig?.orientation == Configuration.ORIENTATION_PORTRAIT) {//PORTRAIT
                val lp = (mWebView as com.tencent.smtt.sdk.WebView).rootView.layoutParams as FrameLayout.LayoutParams
                lp.height = mWebViewHeight
            }
        }
    }

    override fun onResume() {
        WebViewUtils.onResume(mWebView)
        super.onResume()
    }

    override fun onPause() {
        WebViewUtils.onPause(mWebView)
        super.onPause()
    }

    override fun onDestroy() {
        WebViewUtils.onDestroy(mWebView)
        super.onDestroy()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        return if (WebViewUtils.onKeyDown(keyCode, mWebView)) true else super.onKeyDown(keyCode, event)
    }
}