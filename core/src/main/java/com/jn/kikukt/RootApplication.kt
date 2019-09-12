package com.jn.kikukt

import android.app.Application
import android.content.ComponentCallbacks2
import android.content.Context
import android.support.multidex.MultiDex
import com.jn.kikukt.common.ActivityManager
import com.jn.kikukt.common.UtilsManager
import com.jn.kikukt.common.exception.CrashHandler
import com.jn.kikukt.net.RetrofitManage
import com.jn.kikukt.utils.WebViewUtils
import com.jn.kikukt.utils.glide.GlideUtil.clearMemory
import com.jn.kikukt.utils.glide.GlideUtil.trimMemory

/**
 * Author：Stevie.Chen Time：2019/7/11
 * Class Comment：
 */
abstract class RootApplication : Application() {

    private var LOG_DEBUG: Boolean = false

    protected abstract fun isLOG_DEBUG(): Boolean

    override fun onCreate() {
        super.onCreate()
        LOG_DEBUG = isLOG_DEBUG()
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)//突破65536个方法数
    }

    override fun onLowMemory() {
        super.onLowMemory()
        clearMemory(this)
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        when (level) {
            ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN ->
                //表示应用程序的所有UI界面被隐藏了，即用户点击了Home键或者Back键导致应用的UI界面不可见．
                // 这时候应该释放一些资源．
                clearMemory(this)
            ComponentCallbacks2.TRIM_MEMORY_RUNNING_MODERATE -> {
            }
            ComponentCallbacks2.TRIM_MEMORY_RUNNING_LOW -> {
            }
            ComponentCallbacks2.TRIM_MEMORY_RUNNING_CRITICAL -> {
            }
            ComponentCallbacks2.TRIM_MEMORY_BACKGROUND -> {
            }
            ComponentCallbacks2.TRIM_MEMORY_MODERATE -> {
            }
            ComponentCallbacks2.TRIM_MEMORY_COMPLETE -> {
            }
            else -> {
            }
        }// 表示应用程序正常运行，并且不会被杀掉。但是目前手机的内存已经有点低了，
        // 系统可能会开始根据LRU缓存规则来去杀死进程了。
        //表示应用程序正常运行，并且不会被杀掉。但是目前手机的内存已经非常低了，
        // 我们应该去释放掉一些不必要的资源以提升系统的性能，同时这也会直接影响到我们应用程序的性能。
        //表示应用程序仍然正常运行，但是系统已经根据LRU缓存规则杀掉了大部分缓存的进程了。
        // 这个时候我们应当尽可能地去释放任何不必要的资源，不然的话系统可能会继续杀掉所有缓存中的进程，
        // 并且开始杀掉一些本来应当保持运行的进程，比如说后台运行的服务。
        //表示手机目前内存已经很低了，系统准备开始根据LRU缓存来清理进程。
        // 这个时候我们的程序在LRU缓存列表的最近位置，是不太可能被清理掉的，
        // 但这时去释放掉一些比较容易恢复的资源能够让手机的内存变得比较充足，
        // 从而让我们的程序更长时间地保留在缓存当中，这样当用户返回我们的程序时会感觉非常顺畅，而不是经历了一次重新启动的过程。
        //表示手机目前内存已经很低了，并且我们的程序处于LRU缓存列表的中间位置，
        // 如果手机内存还得不到进一步释放的话，那么我们的程序就有被系统杀掉的风险了。
        //表示手机目前内存已经很低了，并且我们的程序处于LRU缓存列表的最边缘位置，
        // 系统会最优先考虑杀掉我们的应用程序，在这个时候应当尽可能地把一切可以释放的东西都进行释放。
        trimMemory(this, level)
    }

    /**
     * 初始化ActivityManager
     */
    protected fun initActivityManager() {
        ActivityManager.instance.register(this)
    }

    /**
     * 初始化Retrofit
     *
     * @param BASE_URL 服务器域名地址
     */
    protected fun initRetrofit(BASE_URL: String) {
        RetrofitManage.instance.initRetrofit(BASE_URL)
    }

    /**
     * 初始化工具类的一些相关信息
     *
     * @param tagName Tag名称
     */
    protected fun initUtilsManager(tagName: String) {
        UtilsManager.initLogUtils(LOG_DEBUG, tagName)
        UtilsManager.initContextUtils(this)
    }

    /**
     * 初始化第三方平台一些相关信息
     */
    protected fun initTTpManager() {

    }

    /**
     * 初始化崩溃异常信息
     */
    protected fun initCrashHandler() {
        if (!LOG_DEBUG)
            CrashHandler.instance.init(this)
    }

    /**
     * 搜集本地tbs内核信息并上报服务器，服务器返回结果决定使用哪个内核。
     */
    protected fun initTencentWebView() {
        WebViewUtils.initX5Environment(applicationContext)
    }

    /**
     * 查看App中数据库和sp中的数据及其项目构造
     */
    protected fun initStetho() {

    }

    /**
     * 检查内存泄漏
     */
    protected fun initCanary() {

    }
}