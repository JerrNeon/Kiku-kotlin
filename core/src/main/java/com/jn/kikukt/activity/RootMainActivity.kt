package com.jn.kikukt.activity

import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.collection.SimpleArrayMap
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.tabs.TabLayout
import com.jn.kikukt.R
import com.jn.kikukt.common.api.IMainView
import com.jn.kikukt.common.utils.showToast
import com.jn.kikukt.dialog.ProgressDialogFragment
import com.jn.kikukt.dialog.VersionUpdateDialog
import com.jn.kikukt.entiy.VersionUpdateVO
import com.jn.kikukt.receiver.VersionUpdateReceiver
import kotlinx.android.synthetic.main.common_main_layout.*

/**
 * Author：Stevie.Chen Time：2019/7/11
 * Class Comment：
 */
abstract class RootMainActivity : RootActivity(), IMainView, TabLayout.OnTabSelectedListener {

    companion object {
        private var mTimeExit: Long = 0//Back press time
        private const val mTimeInterval: Long = 2000//two times Interval
        private val mFlRootMainContainerId = R.id.fl_RootMainContainer//main content resource ID
    }

    protected var mMenuSelectedTextColorResources: IntArray? = null//selected text color resource id
    protected var mMenuSelectedImgResources: IntArray? = null//selected text icon resource id
    protected var mMenuUnSelectedImgResources: IntArray? = null//unselected text color resource id
    protected var mMenuTitles: Array<String>? = null//text title resource
    protected var mMenuFragments: Array<Fragment>? = null//fragment color resource id
    protected var mMenuFragmentMap =
        SimpleArrayMap<Int, Fragment>()//save Fragment add to FragmentTransaction
    protected var mVersionUpdateReceiver: VersionUpdateReceiver? =
        null//versionUpdate downLoad receiver
    protected var mVersionUpdateDialog: VersionUpdateDialog? = null//versionUpdate dialog
    protected var mVersionUpdateVO: VersionUpdateVO? = null//versionUpdate content

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.common_main_layout)
        mMenuSelectedTextColorResources = getMenuSelectedTextColorResources()
        mMenuSelectedImgResources = getMenuSelectedImgResources()
        mMenuUnSelectedImgResources = getMenuUnSelectedImgResources()
        mMenuTitles = getMenuTitles()
        mMenuFragments = getMenuFragments()
        initMainView()
        changeFragment(0)
    }

    override fun initMainView() {
        if (mMenuSelectedImgResources != null) {
            for (i in mMenuSelectedImgResources!!.indices) {
                val menuView =
                    LayoutInflater.from(this)
                        .inflate(R.layout.common_mainmenu_layout, ll_RootMain, false)
                val iv = menuView.findViewById<ImageView>(R.id.iv_menu)
                val tv = menuView.findViewById<TextView>(R.id.tv_menu)
                tv.setTextColor(
                    ContextCompat.getColor(
                        applicationContext,
                        mMenuSelectedTextColorResources!![i]
                    )
                )
                if (i == 0) {
                    iv.setImageResource(mMenuSelectedImgResources!![i])
                    tv.isSelected = true
                } else {
                    iv.setImageResource(mMenuUnSelectedImgResources!![i])
                    tv.isSelected = false
                }
                tv.text = mMenuTitles!![i]
                tl_RootMain.addTab(tl_RootMain.newTab().setCustomView(menuView))
            }
        }
        tl_RootMain.addOnTabSelectedListener(this)
    }


    override fun changeFragment(position: Int) {
        mMenuFragments?.run {
            if (position >= size)
                throw ArrayIndexOutOfBoundsException("position is more than total: $size")
            val fm = supportFragmentManager
            val ft = fm.beginTransaction()
            var fragment = mMenuFragmentMap.get(position)
            if (fragment == null) {
                fragment = this[position]
                mMenuFragmentMap.put(position, fragment)
                ft.add(mFlRootMainContainerId, fragment)
            }
            hideAllFragment(ft)
            ft.show(fragment)
            ft.commit()
        }
    }

    override fun hideAllFragment(fragmentTransaction: FragmentTransaction) {
        mMenuFragments?.run {
            for (fragment in this) {
                fragmentTransaction.hide(fragment)
            }
        }
    }

    override fun isExit(): Boolean {
        if (System.currentTimeMillis() - mTimeExit > mTimeInterval) {
            if (tl_RootMain.selectedTabPosition == 0) {
                Toast.makeText(
                    applicationContext, resources.getString(R.string.app_exitNoticeMessage),
                    Toast.LENGTH_SHORT
                ).show()
                mTimeExit = System.currentTimeMillis()
            } else {
                tl_RootMain.getTabAt(0)!!.select()
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
            mVersionUpdateDialog = VersionUpdateDialog.newInstance(mVersionUpdateVO!!)
        }
        mVersionUpdateDialog?.show(supportFragmentManager, "") {
            showProgressDialog(ProgressDialogFragment.TYPE_WHITE)
        }
    }

    override fun onTabReselected(tab: TabLayout.Tab?) {
    }

    override fun onTabUnselected(tab: TabLayout.Tab?) {
        val menuView = tab?.customView
        menuView?.run {
            val iv = findViewById<ImageView>(R.id.iv_menu)
            val tv = findViewById<TextView>(R.id.tv_menu)
            mMenuUnSelectedImgResources?.let {
                iv.setImageResource(it[tab.position])
            }
            tv.isSelected = false
        }
    }

    override fun onTabSelected(tab: TabLayout.Tab?) {
        tab?.let {
            val menuView = it.customView
            menuView?.run {
                val iv = findViewById<ImageView>(R.id.iv_menu)
                val tv = findViewById<TextView>(R.id.tv_menu)
                mMenuSelectedImgResources?.let { array ->
                    iv.setImageResource(array[it.position])
                }
                tv.isSelected = true
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