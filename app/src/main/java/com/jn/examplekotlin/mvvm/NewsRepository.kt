package com.jn.examplekotlin.mvvm

import com.jn.examplekotlin.request.ApiManager2
import com.jn.kikukt.net.coroutines.BaseRepository

/**
 * Author：Stevie.Chen Time：2020/1/16
 * Class Comment：
 */
class NewsRepository : BaseRepository() {

    suspend fun getNewList(pageIndex: Int, pageSize: Int) =
        ApiManager2.service.getNewList2(pageIndex, pageSize, "video")
}