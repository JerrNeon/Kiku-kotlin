package com.jn.examplekotlin.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.jn.examplekotlin.R
import com.jn.examplekotlin.databinding.ItemNewsBinding
import com.jn.examplekotlin.entiy.NewsVO

/**
 * Author：Stevie.Chen Time：2020/8/6
 * Class Comment：
 */
class NewDataBindingAdapter :
    BaseQuickAdapter<NewsVO, BaseDataBindingHolder<ItemNewsBinding>>(layoutResId = R.layout.item_news) {
    override fun convert(holder: BaseDataBindingHolder<ItemNewsBinding>, item: NewsVO) {
        holder.dataBinding?.run {
            news = item
            executePendingBindings()
        }
    }
}