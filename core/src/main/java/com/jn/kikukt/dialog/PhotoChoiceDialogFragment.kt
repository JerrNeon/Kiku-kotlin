package com.jn.kikukt.dialog

import android.view.View
import androidx.fragment.app.FragmentManager
import com.jn.kikukt.R
import kotlinx.android.synthetic.main.dialog_photochoicedialog.view.*

/**
 * Author：Stevie.Chen Time：2019/7/15
 * Class Comment：
 */
class PhotoChoiceDialogFragment : RootDialogFragment(), View.OnClickListener {

    private var mTakePhotoOnClickListener: View.OnClickListener? = null
    private var mAlbumOnClickListener: View.OnClickListener? = null

    companion object {
        fun newInstance(): PhotoChoiceDialogFragment {
            return PhotoChoiceDialogFragment()
        }
    }

    override val layoutResourceId: Int = R.layout.dialog_photochoicedialog

    override val animationStyle: Int = R.style.bottom_in_out

    override fun initView() {
        super.initView()
        view?.run {
            tv_takePhoto.setOnClickListener(this@PhotoChoiceDialogFragment)
            tv_album.setOnClickListener(this@PhotoChoiceDialogFragment)
            tv_cancel.setOnClickListener(this@PhotoChoiceDialogFragment)
        }
    }

    fun show(
        manager: FragmentManager,
        takePhotoOnClickListener: View.OnClickListener,
        albumOnClickListener: View.OnClickListener
    ) {
        mTakePhotoOnClickListener = takePhotoOnClickListener
        mAlbumOnClickListener = albumOnClickListener
        super.show(manager, javaClass.simpleName)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.tv_takePhoto -> {
                mTakePhotoOnClickListener?.onClick(view)
            }
            R.id.tv_album -> {
                mAlbumOnClickListener?.onClick(view)
            }
            R.id.tv_cancel -> {

            }
        }
        dismissAllowingStateLoss()
    }
}