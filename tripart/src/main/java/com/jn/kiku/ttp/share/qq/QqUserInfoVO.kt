package com.jn.kiku.ttp.share.qq

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Author：Stevie.Chen Time：2020/09/08 15:25
 * Class Comment：QQ用户信息
 */
@Parcelize
data class QqUserInfoVO(
    /**
     * is_yellow_year_vip : 0
     * ret : 0
     * figureurl_qq_1 : http://q.qlogo.cn/qqapp/222222/8C75BBE3DC6B0E9A64BD31449A3C8CB0/40
     * figureurl_qq_2 : http://q.qlogo.cn/qqapp/222222/8C75BBE3DC6B0E9A64BD31449A3C8CB0/100
     * nickname : 小罗
     * yellow_vip_level : 0
     * msg :
     * figureurl_1 : http://qzapp.qlogo.cn/qzapp/222222/8C75BBE3DC6B0E9A64BD31449A3C8CB0/50
     * vip : 0
     * level : 0
     * figureurl_2 : http://qzapp.qlogo.cn/qzapp/222222/8C75BBE3DC6B0E9A64BD31449A3C8CB0/100
     * is_yellow_vip : 0
     * gender : 男
     * figureurl : http://qzapp.qlogo.cn/qzapp/222222/8C75BBE3DC6B0E9A64BD31449A3C8CB0/30
     */
    var client_id: String? = null,//应用的APPID
    var openid: String? = null,//QQ用户的唯一标示，对当前开发者账号唯一
    var unionid: String? = null,//QQ用户的统一标示，对当前开发者账号唯一
    var access_token: String? = null,
    var ret: Int = 0,
    var figureurl_qq_1: String? = null,//大小为40×40像素的QQ头像URL。
    var figureurl_qq_2: String? = null,//大小为100×100像素的QQ头像URL。需要注意，不是所有的用户都拥有QQ的100×100的头像，但40×40像素则是一定会有。
    var nickname: String? = null,//用户在QQ空间的昵称。
    var gender: String? = null,//性别。 如果获取不到则默认返回”男”
    var msg: String? = null,//如果ret<0，会有相应的错误信息提示，返回数据全部用UTF-8编码。
    var figureurl: String? = null, //大小为30×30像素的QQ空间头像URL。
    var figureurl_1: String? = null,//大小为50×50像素的QQ空间头像URL。
    var figureurl_2: String? = null,//大小为100×100像素的QQ空间头像URL。
    var vip: String? = null,//标识用户是否为黄钻用户（0：不是；1：是）
    var level: String? = null,//黄钻等级
    var yellow_vip_level: String? = null,//黄钻等级
    var is_yellow_vip: String? = null,//标识用户是否为黄钻用户（0：不是；1：是）。
    var is_yellow_year_vip: String? = null//标识是否为年费黄钻用户（0：不是； 1：是）
) : Parcelable