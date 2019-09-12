package com.jn.kikukt.activity

import android.os.Bundle
import android.os.Handler
import android.os.Message
import com.jn.kikukt.R
import com.jn.kikukt.common.api.ISplashView
import com.jn.kikukt.utils.RxPermissionsManager
import com.jn.kikukt.common.SPManage
import io.reactivex.functions.Consumer

/**
 * Author：Stevie.Chen Time：2019/7/11
 * Class Comment：
 */
abstract class RootSplashActivity : RootActivity(), ISplashView, Handler.Callback {

    companion object {
        private const val SKIP_WHAT = 0x01
        private const val SKIP_TIME = 3000L//splash countDowner 3s
    }

    protected var mHandler = Handler(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.SplashTheme)
        if (getImgResourceId() != 0)
            window.decorView.setBackgroundResource(getImgResourceId())
        initView()
    }

    override fun initView() {
        super.initView()
        if (SPManage.instance.getFirstGuide())
            requestAllPermission()//need check All permission first enter App
        else
            mHandler.sendEmptyMessageDelayed(SKIP_WHAT, SKIP_TIME)
    }

    override fun requestAllPermission() {
        initRxPermissions()
        RxPermissionsManager.requestAllPermissions(
            mRxPermissions,
            Consumer { mHandler.sendEmptyMessageDelayed(SKIP_WHAT, SKIP_TIME) },
            getAllPermissions()
        )
    }

    override fun handleMessage(msg: Message?): Boolean {
        if (msg?.what == SKIP_WHAT) {
            if (SPManage.instance.getFirstGuide())
                openGuideActivity()//open splash first
            else
                openMainActivity()
            finish()
        }
        return false
    }
}