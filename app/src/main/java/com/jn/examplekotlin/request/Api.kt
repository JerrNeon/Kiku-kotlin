package com.jn.examplekotlin.request

import com.jn.examplekotlin.entiy.HttpResult
import com.jn.examplekotlin.entiy.NewsVO
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

/**
 * Author：Stevie.Chen Time：2019/9/12
 * Class Comment：
 */
interface Api {

    @FormUrlEncoded
    @POST(ApiConfig.GET_NEW_LIST)
    fun getNewList(
        @Field("page") pageIndex: Int, @Field("count") pageSize: Int, @Field("type")
        type: String
    ): Observable<HttpResult<List<NewsVO>>>

    @FormUrlEncoded
    @POST(ApiConfig.GET_NEW_LIST)
    suspend fun getNewList2(
        @Field("page") pageIndex: Int, @Field("count") pageSize: Int, @Field("type")
        type: String
    ): HttpResult<List<NewsVO>>
}