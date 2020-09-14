package com.jn.kikukt.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.PixelFormat
import android.net.Uri
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import com.jn.kikukt.common.utils.logI
import com.tencent.smtt.sdk.QbSdk
import com.tencent.smtt.sdk.TbsListener
import com.tencent.smtt.sdk.ValueCallback
import org.jsoup.Jsoup
import java.util.*

/**
 * Author：Stevie.Chen Time：2019/7/11
 * Class Comment：
 */
object WebViewUtils {

    @SuppressLint("SetJavaScriptEnabled")
    fun initWebView(activity: Activity, webView: Any) {
        if (webView is WebView) {
            webView.isVerticalScrollBarEnabled = false
            webView.setVerticalScrollbarOverlay(false)
            webView.isHorizontalScrollBarEnabled = false
            webView.setHorizontalScrollbarOverlay(false)
            val webSettings = webView.settings
            //支持javascript
            webSettings.javaScriptEnabled = true
            // 设置可以支持缩放
            webSettings.setSupportZoom(true)
            // 设置出现缩放工具
            webSettings.builtInZoomControls = false
            //扩大比例的缩放
            webSettings.useWideViewPort = false
            //自适应屏幕
            webSettings.loadWithOverviewMode = true
            //设置默认字体
            //webSettings.setDefaultFontSize(17);
            //提高渲染的优先级
            webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH)
        } else if (webView is com.tencent.smtt.sdk.WebView) {
            activity.window.setFormat(PixelFormat.TRANSLUCENT)//（这个对宿主没什么影响，建议声明）
            activity.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)

            //自适应屏幕
            val webSettings = webView.settings
            //支持javascript
            webSettings.javaScriptEnabled = true
            // 设置可以支持缩放
            webSettings.setSupportZoom(true)
            // 设置出现缩放工具
            webSettings.builtInZoomControls = false
            //扩大比例的缩放
            //webSettings.setUseWideViewPort(false);
            //自适应屏幕
            //webSettings.setLoadWithOverviewMode(true);
        }
    }

    /**
     * 初始化腾讯的WebViewX5内核环境
     */
    fun initX5Environment(context: Context) {
        val cb = object : QbSdk.PreInitCallback {

            override fun onViewInitFinished(arg0: Boolean) {
                "TencentWebView: onViewInitFinished is $arg0".logI()
            }

            override fun onCoreInitFinished() {}
        }
        QbSdk.setTbsListener(object : TbsListener {
            override fun onDownloadFinish(i: Int) {
                "TencentWebView: onDownloadFinish".logI()
            }

            override fun onInstallFinish(i: Int) {
                "TencentWebView: onInstallFinish".logI()
            }

            override fun onDownloadProgress(i: Int) {
                "TencentWebView: onDownloadProgress:$i".logI()
            }
        })
        QbSdk.initX5Environment(context, cb)
    }

    fun setWebViewClient(webView: Any) {
        if (webView is WebView) {
            webView.webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(webView: WebView, s: String): Boolean {
                    webView.loadUrl(s)
                    return true
                }

                override fun onPageStarted(webView: WebView, s: String, bitmap: Bitmap) {
                    super.onPageStarted(webView, s, bitmap)
                }

                override fun onPageFinished(webView: WebView, s: String) {
                    super.onPageFinished(webView, s)
                }

                override fun onReceivedError(webView: WebView, i: Int, s: String, s1: String) {
                    super.onReceivedError(webView, i, s, s1)
                }
            }
        } else if (webView is com.tencent.smtt.sdk.WebView) {
            webView.webViewClient = object : com.tencent.smtt.sdk.WebViewClient() {
                override fun shouldOverrideUrlLoading(
                    p0: com.tencent.smtt.sdk.WebView?,
                    s: String
                ): Boolean {
                    webView.loadUrl(s)
                    return true
                }
            }
        }
    }

    fun setWebChromeClient(
        webView: Any,
        progressBar: ProgressBar,
        client1: WebChromeClient? = null,
        client2: com.tencent.smtt.sdk.WebChromeClient? = null
    ) {
        if (webView is WebView) {
            webView.webChromeClient = object : WebChromeClient() {

                override fun onReceivedTitle(view: WebView?, title: String?) {
                    super.onReceivedTitle(view, title)
                    client1?.onReceivedTitle(view, title)
                }

                override fun onProgressChanged(view: WebView, newProgress: Int) {
                    progressBar.progress = newProgress
                    if (newProgress == 100) {
                        progressBar.visibility = View.GONE
                    } else {
                        progressBar.visibility = View.VISIBLE
                    }
                    client1?.onProgressChanged(view, newProgress)
                }

                override fun onShowFileChooser(
                    webView: WebView?,
                    filePathCallback: android.webkit.ValueCallback<Array<Uri>>?,
                    fileChooserParams: FileChooserParams?
                ): Boolean {
                    val result =
                        client1?.onShowFileChooser(webView, filePathCallback, fileChooserParams)
                    return result ?: super.onShowFileChooser(
                        webView,
                        filePathCallback,
                        fileChooserParams
                    )
                }
            }
        } else if (webView is com.tencent.smtt.sdk.WebView) {
            webView.webChromeClient = object : com.tencent.smtt.sdk.WebChromeClient() {
                override fun onReceivedTitle(p0: com.tencent.smtt.sdk.WebView?, p1: String?) {
                    super.onReceivedTitle(p0, p1)
                    client2?.onReceivedTitle(p0, p1)
                }

                override fun onProgressChanged(
                    view: com.tencent.smtt.sdk.WebView?,
                    newProgress: Int
                ) {
                    progressBar.progress = newProgress
                    if (newProgress == 100) {
                        progressBar.visibility = View.GONE
                    } else {
                        progressBar.visibility = View.VISIBLE
                    }
                    client2?.onProgressChanged(view, newProgress)
                }

                override fun onShowFileChooser(
                    p0: com.tencent.smtt.sdk.WebView?,
                    p1: ValueCallback<Array<Uri>>?,
                    p2: FileChooserParams?
                ): Boolean {
                    val result = client2?.onShowFileChooser(p0, p1, p2)
                    return result ?: super.onShowFileChooser(p0, p1, p2)
                }
            }
        }
    }

    /**
     * 加载富文本
     */
    fun loadDataWithBaseURL(webView: Any, htmlText: String) {
        val document = Jsoup.parse(htmlText)
        val imgElements = document.getElementsByTag("img")//图片标签
        val iFrameElements = document.getElementsByTag("iframe")//视频标签
        val videoElements = document.getElementsByTag("video")//视频标签
        val aElements = document.getElementsByTag("a")//链接标签
        //设置图片宽高
        for (element in imgElements) {
            element.attr("width", "100%").attr("height", "auto")
                .attr("style", "")//防止富文本中的该属性设置了width值会覆盖width属性
                .attr(
                    "onerror",
                    "javascript:this.src='https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1557135451684&di=eb2d7915bdde403dcd0e4fd08e1efd63&imgtype=0&src=http%3A%2F%2Fhbimg.b0.upaiyun.com%2F1e3ead27ad747c7c92e659ac5774587a680bb8d25252-mRVFlu_fw658'"
                )//无效图片加载地址
        }
        //设置视频宽高
        for (element in iFrameElements) {
            element.attr("width", "100%").attr("height", "auto")
                .attr("style", "")
                .attr("allowfullscreen", "true")
                .attr("webkitallowfullscreen", "true")
                .attr("mozallowfullscreen", "true")
        }
        for (element in videoElements) {
            element.attr("width", "100%").attr("height", "auto")
        }
        //去掉链接
        for (element in aElements) {
            element.attr("color", "#000000").attr("text-decoration", "none")
        }
        if (webView is WebView)
            webView.loadDataWithBaseURL(null, document.toString(), "text/html", "UTF-8", null)
        else if (webView is com.tencent.smtt.sdk.WebView)
            webView.loadDataWithBaseURL(null, document.toString(), "text/html", "UTF-8", null)
    }

    fun loadUrl(webView: Any, url: String) {
        if (webView is WebView)
            webView.loadUrl(url)
        else if (webView is com.tencent.smtt.sdk.WebView)
            webView.loadUrl(url)
    }

    /**
     * 隐藏WebView播放视频全屏时顶部菜单按钮&分享功能
     *
     * @param activity
     */
    fun removeVideoChildView(activity: Activity) {
        try {
            val outView = ArrayList<View>()
            activity.window.decorView.findViewsWithText(outView, "about", View.FIND_VIEWS_WITH_TEXT)
            "outViewCount>>> ${outView.size}".logI()
            for (view in outView) {
                val viewParent = view.parent as ViewGroup
                val childCount = viewParent.childCount
                //第0个是返回键，不隐藏
                if (childCount > 0) {
                    for (i in 1 until childCount) {
                        viewParent.getChildAt(i).visibility = View.GONE
                    }
                }
                view.visibility = View.GONE
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 问题：在不做任何处理前提下 ，浏览网页时点击系统的“Back”键,整个 Browser 会调用 finish()而结束自身
     * 目标：点击返回后，是网页回退而不是推出浏览器
     * 解决方案：在当前Activity中处理并消费掉该 Back 事件
     *
     * @param keyCode
     * @return
     */
    fun onKeyDown(keyCode: Int, webView: Any?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (webView is WebView) {
                if (webView.canGoBack()) {
                    webView.goBack()
                    return true
                }
            } else if (webView is com.tencent.smtt.sdk.WebView) {
                if (webView.canGoBack()) {
                    webView.goBack()
                    return true
                }
            }
        }
        return false
    }

    fun onResume(webView: Any?) {
        if (webView is WebView)
            webView.onResume()
        else if (webView is com.tencent.smtt.sdk.WebView)
            webView.onResume()
    }

    fun onPause(webView: Any?) {
        if (webView is WebView)
            webView.onPause()
        else if (webView is com.tencent.smtt.sdk.WebView)
            webView.onPause()
    }

    fun onDestroy(webView: Any?) {
        if (webView is WebView)
            webView.destroy()
        else if (webView is com.tencent.smtt.sdk.WebView)
            webView.destroy()
    }
}