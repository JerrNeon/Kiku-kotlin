package com.jn.kikukt.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewStub
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.IntDef
import com.jn.kikukt.R
import com.jn.kikukt.common.api.IMvpView
import com.jn.kikukt.mvp.IBPresenter
import com.jn.kikukt.mvp.IBView

/**
 * Author：Stevie.Chen Time：2019/7/10
 * Class Comment：
 */
abstract class RootTbActivity : RootActivity() {

    companion object {
        protected const val ROOT_LAYOUT = 1//root layout
        protected const val IV_LEFT = 2//left icon
        protected const val TV_TITLE = 3//center title
        protected const val IV_RIGHT = 4//right icon
        protected const val TV_RIGHT = 5//right text
        protected const val VIEW_DIVIDER = 6//divider
        protected const val RESOURCE_TEXT = 1//text resource
        protected const val RESOURCE_COLOR = 2//text color resource or background color resource
        protected const val RESOURCE_DRAWABLE = 3//drawable resource
        protected const val RESOURCE_ENABLE = 4//enable
        protected const val RESOURCE_VISIBLE = 5//visible
    }

    protected var mLlTitleBar: LinearLayout? = null//root layout
    protected var mVsTitleBar: ViewStub? = null//titleBar ViewStub
    protected var mRlTitleBar: RelativeLayout? = null//titleBar layout
    protected var mIvTitleBarLeft: ImageView? = null//left icon
    protected var mTvTitleBarTitle: TextView? = null//center title
    protected var mIvTitleBarRight: ImageView? = null//right icon
    protected var mTvTitleBarRight: TextView? = null//right text
    protected var mViewTitleBarDivider: View? = null//divider

    abstract val layoutResourceId: Int

    @IntDef(ROOT_LAYOUT, IV_LEFT, TV_TITLE, IV_RIGHT, TV_RIGHT, VIEW_DIVIDER)
    @Retention(AnnotationRetention.SOURCE)
    internal annotation class TitleBarType

    @IntDef(RESOURCE_TEXT, RESOURCE_COLOR, RESOURCE_DRAWABLE, RESOURCE_ENABLE, RESOURCE_VISIBLE)
    @Retention(AnnotationRetention.SOURCE)
    internal annotation class ResourceType

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.common_titlebar_layout)
        initRootTbParentView()
        initRootTbView()
        setRootContainerView()
        setStatusBar()
    }

    /**
     * init titleBar Parent View
     */
    protected fun initRootTbParentView() {
        if (layoutResourceId != 0) {
            mLlTitleBar = findViewById(R.id.ll_commonTitleBar)
            mVsTitleBar = findViewById(R.id.vs_commonTitleBar)
            mViewTitleBarDivider = findViewById(R.id.view_commonTitleBarDivider)
        }
    }

    /**
     * init titleBar View
     */
    protected fun initRootTbView() {
        if (layoutResourceId != 0) {
            mVsTitleBar?.inflate()
            mRlTitleBar = findViewById(R.id.rl_commonTitleBar)
            mIvTitleBarLeft = findViewById(R.id.iv_commonTitleBar_left)
            mTvTitleBarTitle = findViewById(R.id.tv_commonTitleBar_Title)
            mIvTitleBarRight = findViewById(R.id.iv_commonTitleBar_right)
            mTvTitleBarRight = findViewById(R.id.tv_commonTitleBar_right)
            mIvTitleBarLeft?.setOnClickListener(this)
            mTvTitleBarTitle?.setOnClickListener(this)
            mIvTitleBarRight?.setOnClickListener(this)
            mTvTitleBarRight?.setOnClickListener(this)
        }
    }

    /**
     * set main content View
     */
    protected fun setRootContainerView() {
        if (layoutResourceId != 0) {
            val contentView =
                LayoutInflater.from(mActivity).inflate(layoutResourceId, null, false)
            val drawable = contentView.background
            if (drawable == null) {
                contentView.setBackgroundResource(R.color.white)
            }
            val lp = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT,
                1f
            )
            mLlTitleBar?.addView(contentView, lp)
        }
    }

    /**
     * set titleBar View style
     *
     * @param titleBarType TitleBarType
     * @param resourceType ResourceType
     * @param content      content
     */
    protected fun setTitleBarView(
        @TitleBarType titleBarType: Int, @ResourceType resourceType: Int, content: Any
    ) {
        when (titleBarType) {
            ROOT_LAYOUT//root layout
            -> if (resourceType == RESOURCE_COLOR && content is Int) {
                mRlTitleBar?.setBackgroundResource(content)
            } else {
                throw IllegalArgumentException("not support type")
            }
            IV_LEFT//left icon
            -> if (resourceType == RESOURCE_DRAWABLE && content is Int) {
                mIvTitleBarLeft?.setImageResource(content)
                mIvTitleBarLeft?.visibility = View.VISIBLE
            } else if (resourceType == RESOURCE_ENABLE && content is Boolean) {
                mIvTitleBarLeft?.isEnabled = content
            } else if (resourceType == RESOURCE_VISIBLE && content is Int) {
                mIvTitleBarLeft?.visibility = content
            } else {
                throw IllegalArgumentException("not support type")
            }
            TV_TITLE//center title
            -> if (resourceType == RESOURCE_TEXT) {
                when (content) {
                    is Int -> mTvTitleBarTitle?.text = resources.getString(content)
                    is String -> mTvTitleBarTitle?.text = content
                    is CharSequence -> mTvTitleBarTitle?.text = content
                }
                mTvTitleBarTitle?.visibility = View.VISIBLE
            } else if (resourceType == RESOURCE_COLOR) {
                if (content is Int) {
                    mTvTitleBarTitle?.setTextColor(content)
                }
                mTvTitleBarTitle?.visibility = View.VISIBLE
            } else if (resourceType == RESOURCE_ENABLE && content is Boolean) {
                mTvTitleBarTitle?.isEnabled = content
            } else if (resourceType == RESOURCE_VISIBLE && content is Int) {
                mTvTitleBarTitle?.visibility = content
            } else {
                throw IllegalArgumentException("not support type")
            }
            IV_RIGHT//right icon
            -> if (resourceType == RESOURCE_DRAWABLE && content is Int) {
                mIvTitleBarRight?.setImageResource(content)
                mIvTitleBarRight?.visibility = View.VISIBLE
            } else if (resourceType == RESOURCE_ENABLE && content is Boolean) {
                mIvTitleBarRight?.isEnabled = content
            } else if (resourceType == RESOURCE_VISIBLE && content is Int) {
                mIvTitleBarRight?.visibility = content
            } else {
                throw IllegalArgumentException("not support type")
            }
            TV_RIGHT//right text
            -> if (resourceType == RESOURCE_TEXT) {
                when (content) {
                    is Int -> mTvTitleBarRight?.text = resources.getString(content)
                    is String -> mTvTitleBarRight?.text = content
                    is CharSequence -> mTvTitleBarRight?.text = content
                }
                mTvTitleBarRight?.visibility = View.VISIBLE
            } else if (resourceType == RESOURCE_COLOR) {
                if (content is Int) {
                    mTvTitleBarRight?.setTextColor(content)
                }
                mTvTitleBarRight?.visibility = View.VISIBLE
            } else if (resourceType == RESOURCE_ENABLE && content is Boolean) {
                mTvTitleBarRight?.isEnabled = content
            } else if (resourceType == RESOURCE_VISIBLE && content is Int) {
                mTvTitleBarRight?.visibility = content
            } else {
                throw IllegalArgumentException("not support type")
            }
            VIEW_DIVIDER//divider
            -> if (resourceType == RESOURCE_COLOR && content is Int) {
                mViewTitleBarDivider?.setBackgroundResource(content)
                mViewTitleBarDivider?.visibility = View.VISIBLE
            } else if (resourceType == RESOURCE_VISIBLE && content is Int) {
                mViewTitleBarDivider?.visibility = content
            } else {
                throw IllegalArgumentException("not support type")
            }
            else -> {
            }
        }
    }

    /**
     * set title
     *
     * @param titleText     title content
     * @param isShowDivider is or not show divider
     */
    protected fun setTitleText(titleText: String, isShowDivider: Boolean = true) {
        setTitleBarView(TV_TITLE, RESOURCE_TEXT, titleText)
        setTitleBarView(
            VIEW_DIVIDER,
            RESOURCE_VISIBLE,
            if (isShowDivider) View.VISIBLE else View.GONE
        )
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.iv_commonTitleBar_left -> {
                onClickTitleBar(IV_LEFT)
                finish()
            }
            R.id.tv_commonTitleBar_Title -> onClickTitleBar(TV_TITLE)
            R.id.iv_commonTitleBar_right -> onClickTitleBar(IV_RIGHT)
            R.id.tv_commonTitleBar_right -> onClickTitleBar(TV_RIGHT)
        }
    }

    /**
     * onClick titleBar view
     *
     * @param titleBarType TitleBarType
     */
    protected fun onClickTitleBar(@TitleBarType titleBarType: Int) {

    }
}

abstract class RootTbPresenterActivity<P : IBPresenter> : RootTbActivity(),
    IMvpView<P> {

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