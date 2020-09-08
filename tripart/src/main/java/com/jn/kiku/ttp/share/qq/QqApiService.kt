package com.jn.kiku.ttp.share.qq

import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import retrofit2.http.Url

/**
 * Author：Stevie.Chen Time：2020/09/08 15:25
 * Class Comment：QQ接口地址
 */
interface QqApiService {
    companion object {
        const val QQBaseUrl = "https://graph.qq.com/"

        /**
         * 获取UNIONID
         */
        const val GET_UNIONID = "https://graph.qq.com/oauth2.0/me"
    }

    /**
     * 获取QQ用户信息
     *
     * @param url          地址
     * @param access_token token
     * @param unionid 唯一ID
     * @return
     */
    @FormUrlEncoded
    @POST
    suspend fun getUserInfo(
        @Url url: String?,
        @Field("access_token") access_token: String?,
        @Field("unionid") unionid: String?
    ): QqUserInfoVO?
}