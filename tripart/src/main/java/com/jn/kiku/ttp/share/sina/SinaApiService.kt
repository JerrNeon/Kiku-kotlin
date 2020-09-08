package com.jn.kiku.ttp.share.sina

import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import retrofit2.http.Url

/**
 * Author：Stevie.Chen Time：2020/09/08 15:25
 * Class Comment：新浪调用地址
 */
interface SinaApiService {

    companion object {
        const val SinaBaseUrl = "https://api.weibo.com/"

        /**
         * 根据用户ID获取用户信息
         *
         *
         * access_token 	true 	string 	采用OAuth授权方式为必填参数，OAuth授权后获得。
         * uid 	false 	int64 	需要查询的用户ID。
         * screen_name 	false 	string 	需要查询的用户昵称。
         *
         */
        const val GET_USERINFO = "https://api.weibo.com/2/users/show.json"
    }

    /**
     * 获取新浪微博用户信息
     *
     * @param url          地址
     * @param access_token 调用接口凭证
     * @param uid          普通用户标识，对该公众帐号唯一
     * @return
     */
    @FormUrlEncoded
    @POST
    suspend fun getUserInfo(
        @Url url: String?,
        @Field("access_token") access_token: String?,
        @Field("uid") uid: String?
    ): SinaUserInfoVO?

}