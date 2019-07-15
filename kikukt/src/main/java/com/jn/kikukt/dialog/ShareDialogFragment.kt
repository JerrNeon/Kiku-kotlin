package com.jn.kikukt.dialog

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.jn.kikukt.R
import com.jn.kikukt.adapter.BaseAdapterViewHolder
import com.jn.kikukt.adapter.BaseRvAdapter
import com.jn.kikukt.entiy.ShareVO
import com.jn.kikukt.utils.getScreenWidth

/**
 * Author：Stevie.Chen Time：2019/7/15
 * Class Comment：
 */
class ShareDialogFragment : RootDialogFragment(), BaseQuickAdapter.OnItemClickListener {

    private var mRecyclerView: RecyclerView? = null
    private var mTvCancel: TextView? = null

    private var mOnItemClickListener: OnItemClickListener? = null
    private var mOnShareResultListener: OnShareResultListener? = null//分享回调-个别情况特殊处理(微信登录、分享、支付都必须安装客户端)

    companion object {
        fun newInstance(): ShareDialogFragment = ShareDialogFragment()
    }

    override fun getLayoutResourceId(): Int {
        return R.layout.dialog_share
    }

    override fun getAnimationStyle(): Int {
        return R.style.bottom_in_out
    }

    override fun getCanceledOnTouchOutsideEnable(): Boolean {
        return true
    }

    override fun getLayoutParams(): WindowManager.LayoutParams? {
        val params = mWindow!!.attributes
        params.width = (mContext.getScreenWidth() * 0.94).toInt()//宽度为全屏
        return params
    }

    override fun initView() {
        mRecyclerView = mView!!.findViewById(R.id.rv_share)
        mTvCancel = mView!!.findViewById(R.id.tv_shareCancel)
        mTvCancel!!.setOnClickListener(this)

        val adapter = ShareAdapter(mFragment)
        adapter.addData(ShareVO(R.drawable.ic_kiku_wxcircle_logo, "朋友圈"))
        adapter.addData(ShareVO(R.drawable.ic_kiku_wechat_logo, "微信"))
        adapter.addData(ShareVO(R.drawable.ic_kiku_sina_logo, "微博"))
        adapter.addData(ShareVO(R.drawable.ic_kiku_qq_logo, "QQ"))
        //adapter.add(new ShareVO(R.drawable.ic_qq_logo, "QQ空间"));
        mRecyclerView!!.layoutManager = GridLayoutManager(mContext, 4)
        mRecyclerView!!.adapter = adapter
        adapter.onItemClickListener = this
    }

    override fun initData() {

    }

    /**
     * 显示对话框
     *
     * @param manager               FragmentManager
     * @param tag                   tag标识
     * @param listener              监听器
     * @param onShareResultListener 分享回调
     */
    fun show(
        manager: FragmentManager,
        tag: String,
        listener: OnItemClickListener,
        onShareResultListener: OnShareResultListener
    ) {
        this.show(manager, tag)
        mOnItemClickListener = listener
        mOnShareResultListener = onShareResultListener
    }

    override fun onClick(view: View) {
        super.onClick(view)
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
        if (mOnItemClickListener != null)
            mOnItemClickListener!!.onItemClick(position)
        this.dismiss()
    }

    protected fun shareWithWeChat() {}

    protected fun shareWithWeChatCircle() {}

    protected fun shareWithSina() {}

    protected fun shareWithQq() {

    }

    private inner class ShareAdapter(fragment: Fragment) : BaseRvAdapter<ShareVO>(fragment) {

        override fun getLayoutResourceId(): Int {
            return R.layout.dialog_item_share
        }

        override fun convert(helper: BaseAdapterViewHolder, item: ShareVO) {
            helper.setImageResource(R.id.iv_share, item.img)
            helper.setText(R.id.tv_share, item.title)
        }
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    interface OnShareResultListener {
        fun onFailure()
    }
}