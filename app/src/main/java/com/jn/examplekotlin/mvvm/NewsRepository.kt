package com.jn.examplekotlin.mvvm

import com.jn.examplekotlin.request.coroutines.ApiManager
import com.jn.kikukt.mvvm.BaseRepository

/**
 * Author：Stevie.Chen Time：2020/1/16
 * Class Comment：
 */
class NewsRepository : BaseRepository() {

    suspend fun getNewList(pageIndex: Int, pageSize: Int) =
        ApiManager.service.getNewList(pageIndex, pageSize, "video")
}