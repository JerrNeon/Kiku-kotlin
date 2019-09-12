package com.jn.examplekotlin.adapter

import android.app.Activity
import com.jn.examplekotlin.R
import com.jn.examplekotlin.entiy.NewsVO
import com.jn.kikukt.adapter.BaseAdapterViewHolder
import com.jn.kikukt.adapter.BaseRvAdapter

/**
 * Author：Stevie.Chen Time：2019/9/12
 * Class Comment：
 */
class NewAdapter(activity: Activity) : BaseRvAdapter<NewsVO>(activity) {

    override fun getLayoutResourceId(): Int = R.layout.item_news

    override fun convert(helper: BaseAdapterViewHolder?, item: NewsVO?) {
        item?.let {
            helper?.displayImage(R.id.iv_news, it.text)?.setText(R.id.tv_news, it.thumbnail)
        }
    }
}