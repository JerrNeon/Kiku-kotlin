package com.jn.examplekotlin.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.jn.examplekotlin.fragment.NewsListFragment
import com.jn.kikukt.activity.RootTabActivity

/**
 * Author：Stevie.Chen Time：2020/8/6
 * Class Comment：
 */
class NewsViewPager2Activity : RootTabActivity() {
    override val fragments: MutableList<Fragment>
        get() = arrayListOf(NewsListFragment(), NewsListFragment(), NewsListFragment())
    override val titles: MutableList<String>
        get() = arrayListOf("News1", "News2", "News3")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTitleText("News DataBinding ViewPager2")
    }
}