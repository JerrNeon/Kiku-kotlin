package com.jn.kikukt.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.jn.kikukt.common.api.*
import com.jn.kikukt.dialog.ProgressDialogFragment
import com.jn.kikukt.mvp.IBPresenter
import com.jn.kikukt.net.coroutines.HttpViewModel

/**
 * Author：Stevie.Chen Time：2020/8/6
 * Class Comment：[ViewBinding]Fragment
 */
abstract class RootVBFragment<VB : ViewBinding> : Fragment(),
    IBaseView, IViewModelView, IMvpView, ILazyFragmentView, IViewBindingView {

    override var mProgressDialog: ProgressDialogFragment? = null
    override val viewModel: HttpViewModel? = null
    override val presenter: IBPresenter? = null
    override var isFirstLoad: Boolean = false
    override val viewBinding: VB
        get() = _viewBinding!!
    private var _viewBinding: VB? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        initViewBinding(inflater, container, false)
        return _viewBinding?.root
    }

    override fun initViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        attachToRoot: Boolean
    ) {
        _viewBinding = initViewBindings(inflater, container, attachToRoot)
    }

    override fun onResume() {
        super.onResume()
        if (!isFirstLoad && !isHidden && isLazyLoad) {
            onRequest()
            isFirstLoad = true
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        isFirstLoad = false
        _viewBinding = null
    }
}