package com.jn.examplekotlin.request.rxjava

import com.jn.examplekotlin.entiy.ApiResult
import com.jn.examplekotlin.entiy.NewsVO
import com.jn.examplekotlin.request.ApiConfig
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
    ): Observable<ApiResult<List<NewsVO>>>

}