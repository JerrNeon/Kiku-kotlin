package com.jn.kikukt.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.jn.kikukt.common.api.IBaseView
import com.jn.kikukt.common.api.ILazyFragmentView
import com.jn.kikukt.common.api.IMvpView
import com.jn.kikukt.common.api.IViewModelView
import com.jn.kikukt.dialog.ProgressDialogFragment
import com.jn.kikukt.mvp.IBPresenter
import com.jn.kikukt.net.coroutines.HttpViewModel

/**
 * Author：Stevie.Chen Time：2019/7/11
 * Class Comment：
 */
abstract class RootFragment : Fragment(), IBaseView, IViewModelView, IMvpView, ILazyFragmentView {

    override lateinit var mAppCompatActivity: AppCompatActivity
    override var mProgressDialog: ProgressDialogFragment? = null
    override val viewModel: HttpViewModel? = null
    override val presenter: IBPresenter? = null
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
        val view = inflater.inflate(getLayoutResourceId(), container, false)
        if (activity is AppCompatActivity)
            mAppCompatActivity = activity as AppCompatActivity
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mIsFragmentViewCreated = true
        onFragmentLazyLoad()
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
}