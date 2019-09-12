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
import com.jn.kikukt.common.api.IBaseView
import com.jn.kikukt.common.api.IDisposableView
import com.jn.kikukt.common.api.ILazyFragmentView
import com.jn.kikukt.dialog.ProgressDialogFragment
import com.jn.kikukt.utils.BaseManager
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.disposables.CompositeDisposable

/**
 * Author：Stevie.Chen Time：2019/7/11
 * Class Comment：
 */
abstract class RootFragment : Fragment(), IBaseView, IDisposableView, ILazyFragmentView,
    View.OnClickListener {

    override lateinit var mActivity: Activity
    override lateinit var mAppCompatActivity: AppCompatActivity
    override lateinit var mContext: Context
    override var mRxPermissions: RxPermissions? = null
    override var mProgressDialog: ProgressDialogFragment? = null
    override var mCompositeDisposable: CompositeDisposable? = null
    override var mBaseManager: BaseManager? = null
    override lateinit var mFragment: Fragment
    override var mView: View? = null
    override var mIsFragmentViewCreated: Boolean = false
    override var mIsFragmentVisible: Boolean = false//标志当前页面是否可见

    @LayoutRes
    abstract fun getLayoutResourceId(): Int

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        mView = inflater.inflate(getLayoutResourceId(), container, false)
        if (activity is Activity)
            mActivity = activity as Activity
        if (activity is AppCompatActivity)
            mAppCompatActivity = activity as AppCompatActivity
        mContext = activity!!.applicationContext
        mFragment = this
        mBaseManager = BaseManager(this)
        lifecycle.addObserver(mBaseManager!!)
        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mIsFragmentViewCreated = true
        onFragmentLazyLoad()
    }

    override fun onDestroyView() {
        dispose()
        super.onDestroyView()
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

    override fun onFragmentLazyLoad() {
        if (!mIsFragmentVisible || !mIsFragmentViewCreated || !isLazyLoadFragment())
            return
        sendRequest()//数据请求
    }

    override fun onClick(v: View?) {
    }
}