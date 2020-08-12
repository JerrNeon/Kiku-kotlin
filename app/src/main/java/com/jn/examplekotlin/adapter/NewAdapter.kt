package com.jn.examplekotlin.adapter

import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jn.examplekotlin.R
import com.jn.examplekotlin.entiy.NewsVO
import com.jn.kikukt.adapter.BaseRvAdapter
import com.jn.kikukt.adapter.loadImage

/**
 * Author：Stevie.Chen Time：2019/9/12
 * Class Comment：
 */
class NewAdapter : BaseRvAdapter<NewsVO>(R.layout.item_news) {

    override fun convert(holder: BaseViewHolder, item: NewsVO) {
        item.run {
            holder.loadImage(R.id.iv_news, thumbnail)
                .setText(R.id.tv_news, text)
        }
    }
}