package com.jn.kikukt.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.jn.kikukt.common.api.IBaseView
import com.jn.kikukt.common.api.IDisposableView
import com.jn.kikukt.common.api.ILazyFragmentView
import com.jn.kikukt.common.api.IMvpView
import com.jn.kikukt.dialog.ProgressDialogFragment
import com.jn.kikukt.mvp.IBPresenter
import com.jn.kikukt.mvp.IBView
import io.reactivex.disposables.CompositeDisposable

/**
 * Author：Stevie.Chen Time：2019/7/11
 * Class Comment：
 */
abstract class RootFragment : Fragment(), IBaseView, IDisposableView, ILazyFragmentView,
    View.OnClickListener {

    override lateinit var mAppCompatActivity: AppCompatActivity
    override lateinit var mContext: Context
    override var mProgressDialog: ProgressDialogFragment? = null
    override var mCompositeDisposable: CompositeDisposable? = null
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
        if (activity is AppCompatActivity)
            mAppCompatActivity = activity as AppCompatActivity
        mContext = requireActivity().applicationContext
        mFragment = this
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
        onRequest()//数据请求
    }

    override fun onClick(v: View?) {
    }
}

abstract class RootPresenterFragment<P : IBPresenter> : RootFragment(), IMvpView<P> {

    override var mPresenter: P? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initPresenter()
    }

    override fun initPresenter() {
        super.initPresenter()
        mPresenter?.let {
            it.attachView(this as? IBView)
            lifecycle.addObserver(it)
        }
    }
}