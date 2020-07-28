package com.jn.examplekotlin.adapter

import com.bumptech.glide.RequestManager
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jn.examplekotlin.R
import com.jn.examplekotlin.entiy.NewsVO
import com.jn.kikukt.adapter.BaseRvAdapter
import com.jn.kikukt.adapter.displayImage

/**
 * Author：Stevie.Chen Time：2019/9/12
 * Class Comment：
 */
class NewAdapter(
    requestManager: RequestManager
) : BaseRvAdapter<NewsVO>(requestManager, layoutResId = R.layout.item_news) {

    override fun convert(holder: BaseViewHolder, item: NewsVO) {
        item.run {
            holder.displayImage(R.id.iv_news, requestManager, thumbnail)
                .setText(R.id.tv_news, text)
        }
    }
}