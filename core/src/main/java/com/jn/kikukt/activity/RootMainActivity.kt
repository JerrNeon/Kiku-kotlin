package com.jn.kikukt.activity

import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.commit
import com.google.android.material.tabs.TabLayout
import com.jn.kikukt.R
import com.jn.kikukt.common.api.IMainView
import com.jn.kikukt.common.utils.showToast
import com.jn.kikukt.dialog.ProgressDialogFragment
import com.jn.kikukt.dialog.VersionUpdateDialog
import com.jn.kikukt.entiy.VersionUpdateVO
import com.jn.kikukt.receiver.VersionUpdateReceiver

/**
 * Author：Stevie.Chen Time：2019/7/11
 * Class Comment：
 */
abstract class RootMainActivity : RootActivity(R.layout.common_main_layout), IMainView,
    TabLayout.OnTabSelectedListener {

    open var mCurrPosition = 0//current position
    protected var mTimeExit: Long = 0//Back press time
    protected var mVersionUpdateReceiver: VersionUpdateReceiver? =
        null//versionUpdate downLoad receiver
    protected var mVersionUpdateDialog: VersionUpdateDialog? = null//versionUpdate dialog
    protected var mVersionUpdateVO: VersionUpdateVO? = null//versionUpdate content
    protected lateinit var mTabLayout: TabLayout//TabLayout

    companion object {
        const val TIME_INTERVAL: Long = 2000//two times Interval
        private val FRAGMENT_CONTAINER_VIEW_ID =
            R.id.fcv_RootMainContainer//main content resource ID
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initMainView()
        changeFragment(0)
    }

    override fun initMainView() {
        mTabLayout = findViewById(R.id.tl_RootMain)
        for (i in menuSelectedImgResources.indices) {
            val menuView =
                LayoutInflater.from(this)
                    .inflate(R.layout.common_mainmenu_layout, mTabLayout, false)
            val iv = menuView.findViewById<ImageView>(R.id.iv_menu)
            val tv = menuView.findViewById<TextView>(R.id.tv_menu)
            tv.setTextColor(
                ContextCompat.getColor(applicationContext, menuSelectedTextColorResources[i])
            )
            if (i == 0) {
                iv.setImageResource(menuSelectedImgResources[i])
                tv.isSelected = true
            } else {
                iv.setImageResource(menuUnSelectedImgResources[i])
                tv.isSelected = false
            }
            tv.text = menuTitles[i]
            mTabLayout.addTab(mTabLayout.newTab().setCustomView(menuView))
        }
        mTabLayout.addOnTabSelectedListener(this)
    }

    override fun changeFragment(position: Int) {
        menuFragments.let {
            if (position >= it.size)
                throw ArrayIndexOutOfBoundsException("position is beyond total: ${it.size}")
            supportFragmentManager.commit {
                //隐藏当前显示的Fragment
                supportFragmentManager.findFragmentByTag(mCurrPosition.toString())
                    ?.let { fragment -> hide(fragment) }
                //获取即将显示的Fragment
                val fragment = supportFragmentManager.findFragmentByTag(position.toString())
                if (fragment == null) {
                    add(FRAGMENT_CONTAINER_VIEW_ID, it[position], null, position.toString())
                } else {
                    show(fragment)
                }
                setReorderingAllowed(true)
            }
        }
        mCurrPosition = position
    }

    override fun isExit(): Boolean {
        if (System.currentTimeMillis() - mTimeExit > TIME_INTERVAL) {
            if (mTabLayout.selectedTabPosition == 0) {
                Toast.makeText(
                    applicationContext,
                    resources.getString(R.string.app_exitNoticeMessage),
                    Toast.LENGTH_SHORT
                ).show()
                mTimeExit = System.currentTimeMillis()
            } else {
                mTabLayout.getTabAt(0)?.select()
            }
        } else {
            return true
        }
        return false
    }

    override fun registerVersionUpdateReceiver() {
        if (mVersionUpdateReceiver == null) {
            mVersionUpdateReceiver = VersionUpdateReceiver { _, intent ->
                dismissProgressDialog()
                val message = intent.getStringExtra(VersionUpdateReceiver.VERSION_UPDATE_ACTION)
                message?.let { applicationContext.showToast(it) }
                showVersionUpdateDialog()
            }
        }
        val intentFilter = IntentFilter(VersionUpdateReceiver.VERSION_UPDATE_ACTION)
        registerReceiver(mVersionUpdateReceiver, intentFilter)
    }

    override fun unregisterVersionUpdateReceiver() {
        if (mVersionUpdateReceiver != null)
            unregisterReceiver(mVersionUpdateReceiver)
    }

    override fun showVersionUpdateDialog() {
        if (mVersionUpdateVO == null)
            throw NullPointerException("mVersionUpdateVO is null,please set VersionUpdateVO info")
        if (mVersionUpdateDialog == null) {
            mVersionUpdateVO?.let { mVersionUpdateDialog = VersionUpdateDialog.newInstance(it) }
        }
        mVersionUpdateDialog?.show(supportFragmentManager, "") {
            showProgressDialog(ProgressDialogFragment.TYPE_WHITE, false)
        }
    }

    override fun onTabReselected(tab: TabLayout.Tab?) {
    }

    override fun onTabUnselected(tab: TabLayout.Tab?) {
        tab?.let {
            it.customView?.run {
                findViewById<ImageView>(R.id.iv_menu).setImageResource(menuUnSelectedImgResources[it.position])
                findViewById<TextView>(R.id.tv_menu).isSelected = false
            }
        }
    }

    override fun onTabSelected(tab: TabLayout.Tab?) {
        tab?.let {
            it.customView?.run {
                findViewById<ImageView>(R.id.iv_menu).setImageResource(menuSelectedImgResources[it.position])
                findViewById<TextView>(R.id.tv_menu).isSelected = true
            }
            changeFragment(it.position)
        }
    }

    override fun onPause() {
        unregisterVersionUpdateReceiver()
        super.onPause()
    }

    override fun onBackPressed() {
        if (isExit())
            super.onBackPressed()
    }
}