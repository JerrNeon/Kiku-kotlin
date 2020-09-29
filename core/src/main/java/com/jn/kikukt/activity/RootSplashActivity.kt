package com.jn.kikukt.activity

import android.app.Activity
import android.os.Bundle
import android.os.Message
import com.jn.kikukt.common.SPManage
import com.jn.kikukt.common.api.ISplashView
import com.jn.kikukt.common.leak.WeakHandler
import com.jn.kikukt.common.utils.statusbar.setTransparent
import requestMultiplePermissions

/**
 * Author：Stevie.Chen Time：2019/7/11
 * Class Comment：
 */
abstract class RootSplashActivity : RootActivity(), ISplashView {

    companion object {
        private const val SKIP_WHAT = 0x01
        private const val SKIP_TIME = 3000L//splash countDowner 3s
    }

    protected var mHandler = WeakHandler(this, handleMessage)

    open val handleMessage
        get() = { activity: Activity, msg: Message ->
            if (msg.what == SKIP_WHAT) {
                if (SPManage.instance.isFirstGuide)
                    openGuideActivity(activity)//open splash first
                else
                    openMainActivity(activity)
                finish()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTransparent()
        initView()
    }

    override fun initView() {
        super.initView()
        if (SPManage.instance.isFirstGuide)
            requestAllPermission()//need check All permission first enter App
        else
            mHandler.sendEmptyMessageDelayed(SKIP_WHAT, SKIP_TIME)
    }

    override fun requestAllPermission() {
        requestMultiplePermissions(getAllPermissions()) {
            mHandler.sendEmptyMessageDelayed(SKIP_WHAT, SKIP_TIME)
        }
    }
}