package com.jn.kikukt.fragment

import androidx.fragment.app.Fragment
import com.jn.kikukt.common.api.IBaseView
import com.jn.kikukt.common.api.ILazyFragmentView
import com.jn.kikukt.common.api.IMvpView
import com.jn.kikukt.common.api.IViewModelView
import com.jn.kikukt.dialog.ProgressDialogFragment
import com.jn.kikukt.mvp.IBPresenter
import com.jn.kikukt.net.coroutines.HttpViewModel

/**
 * Author：Stevie.Chen Time：2020/8/6
 * Class Comment：
 */
abstract class RootFragment(contentLayoutId: Int) : Fragment(contentLayoutId), IBaseView,
    IViewModelView, IMvpView, ILazyFragmentView {

    override var mProgressDialog: ProgressDialogFragment? = null
    override val viewModel: HttpViewModel? = null
    override val presenter: IBPresenter? = null
    override var isFirstLoad: Boolean = false

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
    }
}