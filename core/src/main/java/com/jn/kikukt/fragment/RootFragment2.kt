package com.jn.kikukt.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.jn.kikukt.common.api.IBaseView
import com.jn.kikukt.common.api.ILazyFragmentView2
import com.jn.kikukt.common.api.IMvpView
import com.jn.kikukt.common.api.IViewModelView
import com.jn.kikukt.dialog.ProgressDialogFragment
import com.jn.kikukt.mvp.IBPresenter
import com.jn.kikukt.net.coroutines.HttpViewModel

/**
 * Author：Stevie.Chen Time：2019/7/11
 * Class Comment：
 */
abstract class RootFragment2(open val contentLayoutResId: Int) : Fragment(), IBaseView,
    IViewModelView, IMvpView, ILazyFragmentView2 {

    override var mProgressDialog: ProgressDialogFragment? = null
    override val viewModel: HttpViewModel? = null
    override val presenter: IBPresenter? = null
    override var mIsFragmentViewCreated: Boolean = false
    override var mIsFragmentVisible: Boolean = false//标志当前页面是否可见

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return if (contentLayoutResId != 0) {
            inflater.inflate(contentLayoutResId, container, false)
        } else {
            super.onCreateView(inflater, container, savedInstanceState)
        }
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
        if (!mIsFragmentVisible || !mIsFragmentViewCreated || !isLazyLoad())
            return
        onRequest()//数据请求
    }
}