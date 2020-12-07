package com.jn.kikukt.widget

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.jn.kikukt.R
import kotlin.properties.Delegates

/**
 * Author：Stevie.Chen Time：2020/8/10
 * Class Comment：common title bar
 */
class TitleBarLayout : ConstraintLayout {

    companion object {
        private const val DEFAULT_TITLE_BACKGROUND = Color.WHITE//默认标题栏背景色
        private const val DEFAULT_TITLE_HEIGHT = 46f//默认标题栏高度
        private const val DEFAULT_TITLE_TEXT_SIZE = 16f//默认标题字体大小
        private const val DEFAULT_DIVIDER_HEIGHT = 0.5f//默认分割线高度
    }

    private val defaultTitleLeading = R.drawable.ic_kiku_arrowleft//默认leading资源
    private val defaultTitleTextColor = Color.argb(0xFF, 0x33, 0x33, 0x33)//默认标题文字颜色
    private val defaultDividerColor = Color.argb(0xFF, 0xE9, 0xE9, 0xE9)//默认分割线颜色
    private var titleHeight by Delegates.notNull<Float>()//标题高度

    private lateinit var viewBg: View//title background
    private lateinit var ivLeading: ImageView//title leading(left)
    private lateinit var tvTitle: TextView//title text(center)
    private lateinit var divider: View//divider
    private var menu: View? = null//title menu(right)
    private var content: View? = null//content

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(context, attrs)
    }

    fun init(context: Context, attrs: AttributeSet?) {
        //get custom attribute
        val a = context.obtainStyledAttributes(attrs, R.styleable.TitleBarLayout)
        val titleBackground =
            a.getColor(R.styleable.TitleBarLayout_titleBackground, DEFAULT_TITLE_BACKGROUND)
        titleHeight =
            a.getDimension(
                R.styleable.TitleBarLayout_titleHeight, TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    DEFAULT_TITLE_HEIGHT,
                    context.resources.displayMetrics
                )
            )
        val titleLeading = a.getDrawable(R.styleable.TitleBarLayout_titleLeading)
        val titleTextSize = a.getDimension(
            R.styleable.TitleBarLayout_titleTextSize,
            TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP,
                DEFAULT_TITLE_TEXT_SIZE,
                context.resources.displayMetrics
            )
        )
        val titleTextColor =
            a.getColor(R.styleable.TitleBarLayout_titleTextColor, defaultTitleTextColor)
        val titleMenuLayoutResId =
            a.getResourceId(R.styleable.TitleBarLayout_titleMenuLayoutResId, 0)
        val dividerColor =
            a.getColor(R.styleable.TitleBarLayout_dividerColor, defaultDividerColor)
        val dividerHeight = a.getDimension(
            R.styleable.TitleBarLayout_dividerHeight, TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                DEFAULT_DIVIDER_HEIGHT,
                context.resources.displayMetrics
            )
        )
        val dividerVisibility = a.getInt(R.styleable.TitleBarLayout_dividerVisibility, 2)
        //layout resource
        val contentLayoutResId = a.getResourceId(R.styleable.TitleBarLayout_contentLayoutResId, 0)
        a.recycle()
        LayoutInflater.from(context).inflate(R.layout.view_titlebar_layout, this, true)
        viewBg = findViewById(R.id.view_titleBarBg)
        ivLeading = findViewById(R.id.iv_titleBar_leading)
        tvTitle = findViewById(R.id.tv_titleBar_Title)
        divider = findViewById(R.id.view_titleBar_Divider)
        //set view resource
        viewBg.setBackgroundColor(titleBackground)
        viewBg.layoutParams.height = titleHeight.toInt()
        if (titleLeading != null)
            ivLeading.setImageDrawable(titleLeading)
        else
            ivLeading.setImageResource(defaultTitleLeading)
        tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, titleTextSize)
        tvTitle.setTextColor(titleTextColor)
        buildMenuView(titleMenuLayoutResId)
        divider.setBackgroundColor(dividerColor)
        divider.layoutParams.height = dividerHeight.toInt()
        divider.visibility = when (dividerVisibility) {
            0 -> View.VISIBLE
            1 -> View.INVISIBLE
            else -> View.GONE
        }
        buildContentView(contentLayoutResId)
    }

    /**
     * 标题栏菜单(右侧部分)
     */
    private fun buildMenuView(titleMenuLayoutResId: Int) {
        if (titleMenuLayoutResId != 0) {
            menu = LayoutInflater.from(context).inflate(titleMenuLayoutResId, this, false)
            addView(menu, LayoutParams(LayoutParams.WRAP_CONTENT, titleHeight.toInt()).apply {
                topToTop = LayoutParams.PARENT_ID
                endToEnd = LayoutParams.PARENT_ID
            })
        }
    }

    /**
     * 主区域内容
     */
    private fun buildContentView(contentLayoutResId: Int) {
        if (contentLayoutResId != 0) {
            buildContentView(LayoutInflater.from(context).inflate(contentLayoutResId, this, false))
        }
    }

    /**
     * 主区域内容
     */
    private fun buildContentView(contentView: View) {
        content = contentView
        content?.let {
            val drawable = it.background
            if (drawable == null) {
                it.setBackgroundColor(Color.WHITE)
            }
        }
        addView(
            content,
            LayoutParams(LayoutParams.MATCH_PARENT, 0).apply {
                verticalWeight = 1f
                topToBottom = R.id.view_titleBar_Divider
                bottomToBottom = LayoutParams.PARENT_ID
            })
    }

    /**
     * 设置标题资源
     */
    fun setTitleSource(
        bgResId: Int? = null,
        leadingResId: Int? = null,
        titleHeight: Float? = null,
        titleTextSize: Float? = null,
        titleTextColorResId: Int? = null,
        titleText: String? = null,
        menuLayoutResId: Int? = null,
        dividerColorResId: Int? = null,
        dividerHeight: Float? = null,
        dividerVisibility: Int? = null,
        contentLayoutResId: Int? = null,
        contentView: View? = null,
    ) {
        bgResId?.let {
            viewBg.setBackgroundColor(
                ContextCompat.getColor(
                    context,
                    it
                )
            )
        }
        leadingResId?.let {
            ivLeading.setImageResource(it)
        }
        titleHeight?.let {
            ivLeading.layoutParams.height = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                it,
                context.resources.displayMetrics
            ).toInt()
        }
        titleTextSize?.let {
            tvTitle.textSize = it
        }
        titleTextColorResId?.let {
            tvTitle.setTextColor(
                ContextCompat.getColor(
                    context,
                    it
                )
            )
        }
        titleText?.let {
            tvTitle.text = it
        }
        menuLayoutResId?.let {
            buildMenuView(it)
        }
        dividerVisibility?.let {
            divider.visibility = it
        }
        dividerColorResId?.let {
            divider.setBackgroundColor(
                ContextCompat.getColor(
                    context,
                    it
                )
            )
        }
        dividerHeight?.let {
            divider.layoutParams.height = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                it,
                context.resources.displayMetrics
            ).toInt()
        }
        contentLayoutResId?.let {
            buildContentView(it)
        }
        contentView?.let {
            buildContentView(it)
        }
    }

    /**
     * 标题点击
     */
    fun onTitleClick(
        onLeadingClick: ((v: View) -> Unit)? = null,
        onMenuClick: ((v: View) -> Unit)? = null
    ) {
        onLeadingClick?.let {
            ivLeading.setOnClickListener(it)
        }
        onMenuClick?.let {
            menu?.setOnClickListener(it)
        }
    }
}