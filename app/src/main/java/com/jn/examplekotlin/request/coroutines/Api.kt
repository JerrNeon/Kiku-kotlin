package com.jn.examplekotlin.request.coroutines

import com.jn.examplekotlin.entiy.HttpResult
import com.jn.examplekotlin.entiy.NewsVO
import com.jn.examplekotlin.request.ApiConfig
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

/**
 * Author：Stevie.Chen Time：2020/7/31
 * Class Comment：
 */
interface Api {

    @FormUrlEncoded
    @POST(ApiConfig.GET_NEW_LIST)
    suspend fun getNewList(
        @Field("page") pageIndex: Int, @Field("count") pageSize: Int, @Field("type")
        type: String
    ): HttpResult<List<NewsVO>>
}