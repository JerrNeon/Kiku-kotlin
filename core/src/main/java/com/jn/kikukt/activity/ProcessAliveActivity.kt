package com.jn.kikukt.activity

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.Gravity
import androidx.appcompat.app.AppCompatActivity
import com.jn.kikukt.R
import com.jn.kikukt.common.utils.isScreenOn

/**
 * Author：Stevie.Chen Time：2019/7/11
 * Class Comment：1像素的Activity, 用于息屏时打开此Activity提升优先级达到进程保活
 * <p>
 *  需要使用时请在manifest中注册
 * </p>
 */
class ProcessAliveActivity : AppCompatActivity() {

    companion object {
        const val KIKUKT_FINISH_ACTION = "com.jn.kikukt.processalive.finish"//用于发送关闭此Activity的广播Action
    }

    private var mFinishReceiver: BroadcastReceiver? = null//结束此Activity的广播

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.NoTitleTranslucentTheme)
        setSize()
        registerFinishReceiver()
        checkScreenStatus()
    }

    override fun onResume() {
        checkScreenStatus()
        super.onResume()
    }

    /**
     * 设置此界面的尺寸
     */
    @SuppressLint("RtlHardcoded")
    fun setSize() {
        val window = window
        window.setGravity(Gravity.LEFT or Gravity.TOP)
        val params = window.attributes
        params.x = 0
        params.y = 0
        params.height = 1
        params.width = 1
        window.attributes = params
    }

    /**
     * 注册结束此Activity的广播
     */
    private fun registerFinishReceiver() {
        mFinishReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                finish()
            }
        }
        registerReceiver(mFinishReceiver, IntentFilter(KIKUKT_FINISH_ACTION))
    }

    /**
     * 检查屏幕状态
     */
    private fun checkScreenStatus() {
        if (isScreenOn()) {
            finish()
        }
    }
}