package com.jn.kiku.ttp.share.wechat

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Author：Stevie.Chen Time：2020/09/08 15:25
 * Class Comment：微信AccessToken实体
 */
@Parcelize
data class WeChatAccessTokenVO(
    var access_token: String?= null,//接口调用凭证
    var expires_in: Long = 0,//access_token接口调用凭证超时时间，单位（秒）
    var refresh_token: String?= null,//用户刷新access_token
    var openid: String?= null,//授权用户唯一标识
    var scope: String?= null,//用户授权的作用域，使用逗号（,）分隔
    var unionid: String?= null,//当且仅当该移动应用已获得该用户的userinfo授权时，才会出现该字段
    var errcode: String?= null,//40029,
    var errmsg: String?= null//"invalid code"
) : Parcelable