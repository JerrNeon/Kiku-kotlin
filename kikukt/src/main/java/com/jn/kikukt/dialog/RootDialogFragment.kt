package com.jn.kikukt.dialog

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.annotation.CallSuper
import android.support.annotation.CheckResult
import android.support.annotation.LayoutRes
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v7.app.AppCompatActivity
import android.support.v7.app.AppCompatDialogFragment
import android.view.*
import com.jn.kikukt.R
import com.jn.kikukt.common.api.IBaseView
import com.jn.kikukt.utils.manager.RxPermissionsManager
import com.tbruyelle.rxpermissions2.RxPermissions
import com.trello.rxlifecycle2.LifecycleProvider
import com.trello.rxlifecycle2.LifecycleTransformer
import com.trello.rxlifecycle2.RxLifecycle
import com.trello.rxlifecycle2.android.FragmentEvent
import com.trello.rxlifecycle2.android.RxLifecycleAndroid
import io.reactivex.Observable
import io.reactivex.functions.Consumer
import io.reactivex.subjects.BehaviorSubject
import org.greenrobot.eventbus.EventBus

/**
 * Author：Stevie.Chen Time：2019/7/15
 * Class Comment：
 */
abstract class RootDialogFragment : AppCompatDialogFragment(), LifecycleProvider<FragmentEvent>,
    DialogInterface.OnKeyListener, IBaseView, View.OnClickListener {

    //DialogFragment声明周期管理,解决RxJava内存泄漏的问题
    private val mLifecycleSubject = BehaviorSubject.create<FragmentEvent>()

    protected lateinit var mActivity: Activity
    protected lateinit var mAppCompatActivity: AppCompatActivity
    protected lateinit var mContext: Context
    protected lateinit var mFragment: Fragment
    protected var mWindow: Window? = null
    /**
     * fragment布局
     */
    protected var mView: View? = null
    protected var mRxPermissions: RxPermissions? = null

    @CheckResult
    override fun lifecycle(): Observable<FragmentEvent> {
        return mLifecycleSubject.hide()
    }

    @CheckResult
    override fun <T> bindUntilEvent(event: FragmentEvent): LifecycleTransformer<T> {
        return RxLifecycle.bindUntilEvent(mLifecycleSubject, event)
    }

    @CheckResult
    override fun <T> bindToLifecycle(): LifecycleTransformer<T> {
        return RxLifecycleAndroid.bindFragment(mLifecycleSubject)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mLifecycleSubject.onNext(FragmentEvent.ATTACH)
    }

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mLifecycleSubject.onNext(FragmentEvent.CREATE)
    }

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mLifecycleSubject.onNext(FragmentEvent.CREATE_VIEW)
    }

    @CallSuper
    override fun onStart() {
        super.onStart()
        mLifecycleSubject.onNext(FragmentEvent.START)
        mWindow = dialog.window
        if (getLayoutParams() == null) {
            val params = mWindow?.attributes
            params?.gravity = Gravity.BOTTOM//底部显示
            params?.width = WindowManager.LayoutParams.MATCH_PARENT//宽度为全屏
            params?.height = WindowManager.LayoutParams.WRAP_CONTENT//宽度为全屏
            mWindow?.attributes = params
        } else
            mWindow?.attributes = getLayoutParams()
        mWindow?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))//设置半透明背景
    }

    @CallSuper
    override fun onResume() {
        super.onResume()
        mLifecycleSubject.onNext(FragmentEvent.RESUME)
    }

    @CallSuper
    override fun onPause() {
        mLifecycleSubject.onNext(FragmentEvent.PAUSE)
        super.onPause()
    }

    @CallSuper
    override fun onStop() {
        mLifecycleSubject.onNext(FragmentEvent.STOP)
        super.onStop()
    }

    @CallSuper
    override fun onDestroyView() {
        mLifecycleSubject.onNext(FragmentEvent.DESTROY_VIEW)
        unregisterEventBus()
        super.onDestroyView()
    }

    @CallSuper
    override fun onDestroy() {
        mLifecycleSubject.onNext(FragmentEvent.DESTROY)
        super.onDestroy()
    }

    @CallSuper
    override fun onDetach() {
        mLifecycleSubject.onNext(FragmentEvent.DETACH)
        super.onDetach()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)//去掉默认标题
        dialog.setCanceledOnTouchOutside(!getCanceledOnTouchOutsideEnable())//点击边际是否可消失
        if (getAnimationStyle() != 0)
            dialog.window!!.attributes.windowAnimations = getAnimationStyle()
        mView = inflater.inflate(getLayoutResourceId(), container, false)
        mActivity = activity as Activity
        if (activity is AppCompatActivity)
            mAppCompatActivity = activity as AppCompatActivity
        mContext = activity!!.applicationContext
        mFragment = this
        initView()
        initData()
        return mView
    }

    override fun onKey(dialogInterface: DialogInterface, keyCode: Int, keyEvent: KeyEvent): Boolean {
        return keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_SEARCH//不执行父类点击事件
    }

    override fun show(manager: FragmentManager, tag: String) {
        try {
            super.show(manager, tag)
        } catch (e: Exception) {
            //
        }

    }

    /**
     * 布局资源
     *
     * @return
     */
    @LayoutRes
    protected abstract fun getLayoutResourceId(): Int

    /**
     * 动画
     *
     * @return
     */
    protected abstract fun getAnimationStyle(): Int

    /**
     * 点击边际是否可消失
     *
     * @return false可消失
     */
    protected abstract fun getCanceledOnTouchOutsideEnable(): Boolean

    /**
     * 对话框布局参数
     *
     * @return
     */
    protected abstract fun getLayoutParams(): WindowManager.LayoutParams?

    /**
     * 点击物理按键让对话框不消失
     */
    fun setCanceledOnBackPress() {
        dialog.setOnKeyListener(this)
    }

    /**
     * 对话框是否正在显示
     *
     * @return
     */
    fun isShowing(): Boolean {
        return if (dialog != null) dialog.isShowing else false
    }

    override fun initEventBus() {
        EventBus.getDefault().register(this)
    }

    override fun unregisterEventBus() {
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this)
    }

    override fun initRxPermissions() {
        if (mRxPermissions == null)
            mRxPermissions = RxPermissions(mActivity)
    }

    override fun requestPermission(permissionType: Int, consumer: Consumer<Boolean>?) {
        initRxPermissions()
        RxPermissionsManager.requestPermission(
            mAppCompatActivity,
            mRxPermissions,
            permissionType,
            resources.getString(R.string.app_name),
            consumer
        )
    }

    override fun setStatusBar() {

    }

    override fun showProgressDialog() {

    }

    override fun dismissProgressDialog() {

    }

    override fun initView() {

    }

    override fun initData() {

    }

    override fun sendRequest() {

    }

    override fun onClick(view: View) {

    }
}