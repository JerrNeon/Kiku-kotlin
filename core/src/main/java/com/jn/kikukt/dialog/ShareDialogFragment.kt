package com.jn.kikukt.dialog

import android.view.View
import android.view.WindowManager
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jn.kikukt.R
import com.jn.kikukt.adapter.BaseRvAdapter
import com.jn.kikukt.common.utils.getScreenWidth
import com.jn.kikukt.entiy.ShareVO
import com.jn.kikukt.utils.glide.requestManager

/**
 * Author：Stevie.Chen Time：2019/7/15
 * Class Comment：
 */
class ShareDialogFragment : RootDialogFragment(), OnItemClickListener, View.OnClickListener {

    private var mRecyclerView: RecyclerView? = null
    private var mTvCancel: TextView? = null

    private var mOnItemClickListener: ((position: Int) -> Unit)? = null
    private var mOnShareTypeListener: ((type: ShareType) -> Unit)? =
        null//分享回调-个别情况特殊处理(微信登录、分享、支付都必须安装客户端)

    companion object {
        fun newInstance(): ShareDialogFragment = ShareDialogFragment()
    }

    enum class ShareType {
        WeChat, WxCircle, Sina, QQ
    }

    override val layoutResourceId: Int = R.layout.dialog_share

    override val animationStyle: Int = R.style.bottom_in_out

    override val isCanceledOnTouchOutsideEnable: Boolean = true

    override val layoutParams: WindowManager.LayoutParams? = mWindow?.attributes?.apply {
        width = (requireContext().getScreenWidth() * 0.94).toInt()//宽度
    }

    override fun initView() {
        view?.run {
            mRecyclerView = findViewById(R.id.rv_share)
            mTvCancel = findViewById(R.id.tv_shareCancel)
        }
        mTvCancel?.setOnClickListener(this)

        val adapter = object :
            BaseRvAdapter<ShareVO>(requestManager(), layoutResId = R.layout.dialog_item_share) {

            override fun convert(holder: BaseViewHolder, item: ShareVO) {
                holder.setImageResource(R.id.iv_share, item.img)
                holder.setText(R.id.tv_share, item.title)
            }
        }.apply {
            addData(ShareVO(R.drawable.ic_kiku_wxcircle_logo, "朋友圈"))
            addData(ShareVO(R.drawable.ic_kiku_wechat_logo, "微信"))
            addData(ShareVO(R.drawable.ic_kiku_sina_logo, "微博"))
            addData(ShareVO(R.drawable.ic_kiku_qq_logo, "QQ"))
            //add(new ShareVO(R.drawable.ic_qq_logo, "QQ空间"));
            setOnItemClickListener(this@ShareDialogFragment)
        }
        mRecyclerView?.run {
            layoutManager = GridLayoutManager(requireContext(), 4)
            this.adapter = adapter
        }
    }

    override fun initData() {}

    /**
     * 显示对话框
     *
     * @param manager               FragmentManager
     * @param tag                   tag标识
     * @param listener              监听器
     * @param onShareTypeListener 分享回调
     */
    fun show(
        manager: FragmentManager,
        tag: String,
        listener: ((position: Int) -> Unit)?,
        onShareTypeListener: ((type: ShareType) -> Unit)?
    ) {
        this.show(manager, tag)
        mOnItemClickListener = listener
        mOnShareTypeListener = onShareTypeListener
    }

    override fun onClick(view: View) {
        if (view.id == R.id.tv_shareCancel) {
            this.dismiss()
        }
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        when (position) {
            0 -> shareWithWeChat()
            1 -> shareWithWeChatCircle()
            2 -> shareWithSina()
            3 -> shareWithQq()
            else -> {
            }
        }
        mOnItemClickListener?.run {
            (position)
        }
        this.dismiss()
    }

    private fun shareWithWeChat() {
        mOnShareTypeListener?.run {
            (ShareType.WeChat)
        }
    }

    private fun shareWithWeChatCircle() {
        mOnShareTypeListener?.run {
            (ShareType.WxCircle)
        }
    }

    private fun shareWithSina() {
        mOnShareTypeListener?.run {
            (ShareType.Sina)
        }
    }

    private fun shareWithQq() {
        mOnShareTypeListener?.run {
            (ShareType.QQ)
        }
    }

}