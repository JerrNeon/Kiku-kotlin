package com.jn.kikukt.activity

import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.view.KeyEvent
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.widget.FrameLayout
import com.jn.kikukt.R
import com.jn.kikukt.common.api.IWvView
import com.jn.kikukt.common.utils.file.FileUtils
import com.jn.kikukt.common.utils.openAlbum
import com.jn.kikukt.common.utils.openCamera
import com.jn.kikukt.dialog.PhotoChoiceDialogFragment
import com.jn.kikukt.utils.PERMISSION_CAMERA
import com.jn.kikukt.utils.PERMISSION_WRITE_EXTERNAL_STORAGE
import com.jn.kikukt.utils.RxPermissionsManager
import com.jn.kikukt.utils.WebViewUtils
import kotlinx.android.synthetic.main.common_wv_progress.*
import kotlinx.android.synthetic.main.common_wv_tencent.*
import java.io.File

/**
 * Author：Stevie.Chen Time：2019/7/11
 * Class Comment：Android自带的WebView
 */
open class RootWvActivity : RootTbActivity(), IWvView {

    private val cameraPath: String
        get() = FileUtils.getImagePath("web")
    private var mValueCallback1: ValueCallback<Array<Uri>>? = null//图片回调
    private var mValueCallback2: com.tencent.smtt.sdk.ValueCallback<Array<Uri>>? = null//图片回调

    private lateinit var mWebView: Any
    private var mWebViewHeight: Int = 0//WebView height

    override val layoutResId: Int = R.layout.common_wv_tencent

    companion object {
        private const val REQUEST_UPDATE_CAMERA = 0x01 //相机
        private const val REQUEST_UPDATE_ALBUM = 0x02 //相册
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
        WebViewUtils.setWebChromeClient(
            mWebView,
            pb_wv_common,
            client1 = object : WebChromeClient() {
                override fun onReceivedTitle(p0: WebView?, p1: String?) {
                    super.onReceivedTitle(p0, p1)
                    setTitleText(p1 ?: "")
                }

                override fun onShowFileChooser(
                    webView: WebView?,
                    valueCallback: ValueCallback<Array<Uri>>?,
                    fileChooserParams: FileChooserParams?
                ): Boolean {
                    mValueCallback1 = valueCallback
                    PhotoChoiceDialogFragment.newInstance().show(
                        supportFragmentManager,
                        {
                            RxPermissionsManager.requestPermission(
                                this@RootWvActivity,
                                PERMISSION_CAMERA
                            ) { aBoolean ->
                                if (aBoolean) openCamera(cameraPath, REQUEST_UPDATE_CAMERA)
                            }
                        }) {
                        RxPermissionsManager.requestPermission(
                            this@RootWvActivity,
                            PERMISSION_WRITE_EXTERNAL_STORAGE
                        ) { aBoolean ->
                            if (aBoolean) openAlbum(REQUEST_UPDATE_ALBUM)
                        }
                    }
                    return true
                }
            },
            client2 = object : com.tencent.smtt.sdk.WebChromeClient() {
                override fun onReceivedTitle(p0: com.tencent.smtt.sdk.WebView?, p1: String?) {
                    super.onReceivedTitle(p0, p1)
                    setTitleText(p1 ?: "")
                }

                override fun onShowFileChooser(
                    p0: com.tencent.smtt.sdk.WebView?,
                    valueCallback: com.tencent.smtt.sdk.ValueCallback<Array<Uri>>?,
                    p2: FileChooserParams?
                ): Boolean {
                    mValueCallback2 = valueCallback
                    PhotoChoiceDialogFragment.newInstance().show(
                        supportFragmentManager,
                        {
                            RxPermissionsManager.requestPermission(
                                this@RootWvActivity,
                                PERMISSION_CAMERA
                            ) { aBoolean ->
                                if (aBoolean) openCamera(cameraPath, REQUEST_UPDATE_CAMERA)
                            }
                        }) {
                        RxPermissionsManager.requestPermission(
                            this@RootWvActivity,
                            PERMISSION_WRITE_EXTERNAL_STORAGE
                        ) { aBoolean ->
                            if (aBoolean) openAlbum(REQUEST_UPDATE_ALBUM)
                        }
                    }
                    return true
                }
            })
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        //video screen back webView bottom will display many white space，this reset webView height on screen switch
        if (mWebView is com.tencent.smtt.sdk.WebView) {
            if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {//LANDSCAPE)
                mWebViewHeight = (mWebView as com.tencent.smtt.sdk.WebView).rootView.height
                WebViewUtils.removeVideoChildView(this)//hide video top view
            } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {//PORTRAIT
                val lp =
                    (mWebView as com.tencent.smtt.sdk.WebView).rootView.layoutParams as FrameLayout.LayoutParams
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
        return if (WebViewUtils.onKeyDown(keyCode, mWebView)) true else super.onKeyDown(
            keyCode,
            event
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            //图片选择回调
            var uri: Uri? = null
            if (requestCode == REQUEST_UPDATE_CAMERA) { //相机
                uri = Uri.fromFile(File(cameraPath))
            } else if (requestCode == REQUEST_UPDATE_ALBUM) { //相册
                if (data != null) {
                    uri = data.data
                }
            }
            if (uri != null) {
                mValueCallback1?.onReceiveValue(arrayOf(uri))
                mValueCallback1?.onReceiveValue(arrayOf(uri))
            }
        }
    }
}