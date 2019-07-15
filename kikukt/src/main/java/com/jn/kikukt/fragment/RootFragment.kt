package com.jn.kikukt.fragment

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jn.kikukt.R
import com.jn.kikukt.common.api.IBaseView
import com.jn.kikukt.common.api.IDisposableView
import com.jn.kikukt.common.api.ILazyFragmentView
import com.jn.kikukt.dialog.ProgressDialogFragment
import com.jn.kikukt.utils.manager.RxPermissionsManager
import com.tbruyelle.rxpermissions2.RxPermissions
import com.trello.rxlifecycle2.android.FragmentEvent
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.subjects.BehaviorSubject
import org.greenrobot.eventbus.EventBus

/**
 * Author：Stevie.Chen Time：2019/7/11
 * Class Comment：
 */
abstract class RootFragment : Fragment(), IBaseView, IDisposableView, ILazyFragmentView, View.OnClickListener {

    protected lateinit var mActivity: Activity
    protected lateinit var mAppCompatActivity: AppCompatActivity
    protected lateinit var mContext: Context
    protected lateinit var mFragment: Fragment
    protected var mRxPermissions: RxPermissions? = null
    protected var mProgressDialog: ProgressDialogFragment? = null
    protected var mCompositeDisposable: CompositeDisposable? = null
    //Fragment声明周期管理,解决RxJava内存泄漏的问题
    protected val mLifecycleSubject = BehaviorSubject.create<FragmentEvent>()
    protected var mView: View? = null

    /**
     * 标志位，标志已经初始化完成，因为setUserVisibleHint是在onCreateView之前调用的，
     * 在视图未初始化的时候，在lazyLoad当中就使用的话，就会有空指针的异常
     */
    protected var mIsFragmentViewCreated: Boolean = false
    protected var mIsFragmentVisible: Boolean = false//标志当前页面是否可见

    @LayoutRes
    abstract fun getLayoutResourceId(): Int

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mLifecycleSubject.onNext(FragmentEvent.ATTACH)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mLifecycleSubject.onNext(FragmentEvent.CREATE)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        mLifecycleSubject.onNext(FragmentEvent.CREATE_VIEW)
        mView = inflater.inflate(getLayoutResourceId(), container, false)
        if (activity is Activity)
            mActivity = activity as Activity
        if (activity is AppCompatActivity)
            mAppCompatActivity = activity as AppCompatActivity
        mContext = activity!!.applicationContext
        mFragment = this
        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mIsFragmentViewCreated = true
        onFragmentLazyLoad()
    }

    override fun onStart() {
        super.onStart()
        mLifecycleSubject.onNext(FragmentEvent.START)
    }

    override fun onResume() {
        super.onResume()
        mLifecycleSubject.onNext(FragmentEvent.RESUME)
    }

    override fun onPause() {
        mLifecycleSubject.onNext(FragmentEvent.PAUSE)
        super.onPause()
    }

    override fun onStop() {
        mLifecycleSubject.onNext(FragmentEvent.STOP)
        super.onStop()
    }

    override fun onDestroyView() {
        mLifecycleSubject.onNext(FragmentEvent.DESTROY_VIEW)
        unregisterEventBus()
        dispose()
        super.onDestroyView()
    }

    override fun onDestroy() {
        mLifecycleSubject.onNext(FragmentEvent.DESTROY)
        super.onDestroy()
    }

    override fun onDetach() {
        mLifecycleSubject.onNext(FragmentEvent.DETACH)
        super.onDetach()
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        //懒加载
        if (userVisibleHint) {
            mIsFragmentVisible = true
            onFragmentVisible()
        } else {
            mIsFragmentVisible = false
            onFragmentInvisible()
        }
    }

    override fun isLazyLoadFragment(): Boolean {
        return false
    }

    override fun onFragmentVisible() {
        onFragmentLazyLoad()
    }

    override fun onFragmentInvisible() {

    }

    override fun onFragmentLazyLoad() {
        if (!mIsFragmentVisible || !mIsFragmentViewCreated || !isLazyLoadFragment())
            return
        sendRequest()//数据请求
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
        if (mProgressDialog == null)
            mProgressDialog = ProgressDialogFragment.newInstance()
        mProgressDialog!!.show(mAppCompatActivity.supportFragmentManager, "")
    }

    override fun dismissProgressDialog() {
        mProgressDialog?.cancel()
    }

    override fun initView() {
    }

    override fun initData() {
    }

    override fun sendRequest() {
    }

    override fun addDisposable(disposable: Disposable) {
        if (mCompositeDisposable == null)
            mCompositeDisposable = CompositeDisposable()
        mCompositeDisposable?.add(disposable)
    }

    override fun dispose() {
        if (!mCompositeDisposable!!.isDisposed)
            mCompositeDisposable!!.dispose()
    }

    override fun onClick(v: View?) {
    }
}