package com.jn.kiku.ttp.share.sina

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Author：Stevie.Chen Time：2020/09/08 15:25
 * Class Comment：
 */
@Parcelize
data class SinaUserInfoVO(
    /**
     * id : 1404376560
     * screen_name : zaku
     * name : zaku
     * province : 11
     * city : 5
     * location : 北京 朝阳区
     * description : 人生五十年，乃如梦如幻；有生斯有死，壮士复何憾。
     * url : http://blog.sina.com.cn/zaku
     * profile_image_url : http://tp1.sinaimg.cn/1404376560/50/0/1
     * domain : zaku
     * gender : m
     * followers_count : 1204
     * friends_count : 447
     * statuses_count : 2908
     * favourites_count : 0
     * created_at : Fri Aug 28 00:00:00 +0800 2009
     * following : false
     * allow_all_act_msg : false
     * geo_enabled : true
     * verified : false
     * allow_all_comment : true
     * avatar_large : http://tp1.sinaimg.cn/1404376560/180/0/1
     * verified_reason :
     * follow_me : false
     * online_status : 0
     * bi_followers_count : 215
     */
    var id: Long = 0,
    var uid: String? = null,//唯一标识
    var access_token: String? = null,//token
    var screen_name: String? = null,//昵称
    var name: String? = null,
    var province: String? = null,
    var city: String? = null,
    var location: String? = null,
    var description: String? = null,
    var url: String? = null,
    var profile_image_url: String? = null,
    var domain: String? = null,
    var gender: String? = null,
    var followers_count: Int = 0,
    var friends_count: Int = 0,
    var statuses_count: Int = 0,
    var favourites_count: Int = 0,
    var created_at: String? = null,
    var following: Boolean = false,
    var allow_all_act_msg: Boolean = false,
    var geo_enabled: Boolean = false,
    var verified: Boolean = false,
    var allow_all_comment: Boolean = false,
    var avatar_large: String? = null,
    var verified_reason: String? = null,
    var follow_me: Boolean = false,
    var online_status: Int = 0,
    var bi_followers_count: Int = 0
) : Parcelable