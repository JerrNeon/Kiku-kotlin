package com.jn.kiku.ttp.share

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.annotation.IntDef
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.jn.kiku.ttp.TtpConstants
import com.jn.kiku.ttp.share.qq.QqApiManager
import com.jn.kiku.ttp.share.qq.QqApiService
import com.jn.kiku.ttp.share.qq.QqShareMessage
import com.jn.kiku.ttp.share.qq.QqUserInfoVO
import com.jn.kikukt.common.utils.*
import com.tencent.connect.UserInfo
import com.tencent.connect.share.QQShare
import com.tencent.connect.share.QzoneShare
import com.tencent.tauth.IUiListener
import com.tencent.tauth.Tencent
import com.tencent.tauth.UiError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.util.*

/**
 * Author：Stevie.Chen Time：2019/8/2
 * Class Comment：QQ登录 、 分享管理
 */
class QqManage private constructor() : DefaultLifecycleObserver {
    private val mContext: Context
        get() = ContextUtils.context
    private var mTencent: Tencent? = null
    private var mIUiListenerIml: IUiListenerIml? = null //登录、分享官方回调
    private var mQqResultListener: QqResultListener? = null //登录、分享结果监听
    private var mQqUserInfoResultListener: QqUserInfoResultListener? = null //用户信息监听
    private var lifecycleOwner: LifecycleOwner? = null

    @QQType
    private var mQQType = 0

    @IntDef(SHARE, LOGIN)
    @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
    private annotation class QQType

    companion object {
        private const val SHARE = 1 //分享
        private const val LOGIN = 2 //登录
        private const val mScope = "all" //要所有权限，不然会再次申请增量权限，这里不要设置成get_user_info,add_t
        val instance: QqManage by lazy(mode = LazyThreadSafetyMode.NONE) { QqManage() }
    }

    private fun init() {
        if (mTencent == null) mTencent = Tencent.createInstance(TtpConstants.QQ_APP_ID, mContext)
        if (mIUiListenerIml == null) mIUiListenerIml = IUiListenerIml()
    }

    /**
     * 登录
     *
     * @param activity activity
     * @param listener 结果监听
     */
    fun login(activity: Activity?, listener: QqResultListener?) {
        init()
        if (checkTencentAvailable()) {
            return
        }
        mQQType = LOGIN
        mQqResultListener = listener
        mTencent?.login(activity, mScope, mIUiListenerIml)
    }

    /**
     * 登录
     *
     * @param activity activity
     * @param listener 用户信息结果监听
     */
    fun login(activity: Activity?, listener: QqUserInfoResultListener?) {
        init()
        if (checkTencentAvailable()) return
        mQQType = LOGIN
        mQqUserInfoResultListener = listener
        mTencent?.login(activity, mScope, mIUiListenerIml)
    }

    /**
     * 注销
     */
    fun logout() {
        if (checkTencentAvailable()) return
        mTencent?.logout(mContext)
    }

    /**
     * QQ分享
     *
     * @param activity activity
     * @param params   分享参数
     * @param listener 结果监听
     *
     *
     * Bundle params = new Bundle();
     * params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
     * //分享的标题。注：PARAM_TITLE、PARAM_IMAGE_URL、PARAM_SUMMARY不能全为空，最少必须有一个是有值的。
     * params.putString(QQShare.SHARE_TO_QQ_TITLE, "我在测试");
     * //这条分享消息被好友点击后的跳转URL。
     * params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, "http://connect.qq.com/");
     * //分享的图片URL
     * params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL,
     * "http://img3.cache.netease.com/photo/0005/2013-03-07/8PBKS8G400BV0005.jpg");
     * //分享的消息摘要，最长50个字
     * params.putString(QQShare.SHARE_TO_QQ_SUMMARY, "测试");
     * //手Q客户端顶部，替换“返回”按钮文字，如果为空，用返回代替
     * params.putString(QQShare.SHARE_TO_QQ_APP_NAME, mContext.getResources().getString(R.string.app_name));
     *
     */
    fun shareWithQQ(activity: Activity?, params: Bundle?, listener: QqResultListener?) {
        init()
        if (checkTencentAvailable()) return
        mQQType = SHARE
        mQqResultListener = listener
        if (params != null) {
            params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT)
            //手Q客户端顶部，替换“返回”按钮文字，如果为空，用返回代替
            if ("" == params.getString(
                    QQShare.SHARE_TO_QQ_APP_NAME,
                    ""
                )
            ) params.putString(QQShare.SHARE_TO_QQ_APP_NAME, "test")
            mTencent!!.shareToQQ(activity, params, mIUiListenerIml)
        }
    }

    /**
     * QQ分享
     *
     * @param activity       activity
     * @param qqShareMessage 分享参数
     * @param listener       结果监听
     *
     *
     * Bundle params = new Bundle();
     * params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
     * //分享的标题。注：PARAM_TITLE、PARAM_IMAGE_URL、PARAM_SUMMARY不能全为空，最少必须有一个是有值的。
     * params.putString(QQShare.SHARE_TO_QQ_TITLE, "我在测试");
     * //这条分享消息被好友点击后的跳转URL。
     * params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, "http://connect.qq.com/");
     * //分享的图片URL
     * params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL,
     * "http://img3.cache.netease.com/photo/0005/2013-03-07/8PBKS8G400BV0005.jpg");
     * //分享的消息摘要，最长50个字
     * params.putString(QQShare.SHARE_TO_QQ_SUMMARY, "测试");
     * //手Q客户端顶部，替换“返回”按钮文字，如果为空，用返回代替
     * params.putString(QQShare.SHARE_TO_QQ_APP_NAME, mContext.getResources().getString(R.string.app_name));
     *
     */
    fun shareWithQQ(
        activity: Activity?,
        qqShareMessage: QqShareMessage?,
        listener: QqResultListener?
    ) {
        init()
        if (checkTencentAvailable()) return
        mQQType = SHARE
        mQqResultListener = listener
        if (qqShareMessage != null) {
            val bundle = Bundle()
            bundle.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT)
            if (qqShareMessage.appName != null) bundle.putString(
                QQShare.SHARE_TO_QQ_APP_NAME,
                qqShareMessage.appName
            )
            if (qqShareMessage.title != null) bundle.putString(
                QzoneShare.SHARE_TO_QQ_TITLE,
                qqShareMessage.title
            ) //必填
            if (qqShareMessage.targetUrl != null) bundle.putString(
                QzoneShare.SHARE_TO_QQ_TARGET_URL,
                qqShareMessage.targetUrl
            ) //必填
            if (qqShareMessage.summary != null) bundle.putString(
                QzoneShare.SHARE_TO_QQ_SUMMARY,
                qqShareMessage.summary
            ) //选填
            if (qqShareMessage.imageUrl != null) bundle.putString(
                QQShare.SHARE_TO_QQ_IMAGE_URL,
                qqShareMessage.imageUrl
            ) // 图片地址
            mTencent!!.shareToQQ(activity, bundle, mIUiListenerIml)
        }
    }

    /**
     * Qzone分享
     *
     * @param activity activity
     * @param params   分享参数
     * @param listener 结果监听
     */
    fun shareWithQZone(activity: Activity?, params: Bundle?, listener: QqResultListener?) {
        init()
        if (checkTencentAvailable()) return
        mQQType = SHARE
        mQqResultListener = listener
        if (params == null) {
            val bundle = Bundle()
            bundle.putInt(
                QzoneShare.SHARE_TO_QZONE_KEY_TYPE,
                QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT
            )
            bundle.putString(QzoneShare.SHARE_TO_QQ_TITLE, "标题") //必填
            bundle.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, "http://connect.qq.com/") //必填
            bundle.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, "摘要") //选填
            val imgUrlList = ArrayList<String>()
            imgUrlList.add("http://f.hiphotos.baidu.com/image/h%3D200/sign=6f05c5f929738bd4db21b531918a876c/6a600c338744ebf8affdde1bdef9d72a6059a702.jpg")
            bundle.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, imgUrlList) // 图片地址
            mTencent!!.shareToQzone(activity, bundle, mIUiListenerIml)
        } else mTencent!!.shareToQzone(activity, params, mIUiListenerIml)
    }

    /**
     * Qzone分享
     *
     * @param activity       activity
     * @param qqShareMessage 分享参数
     * @param listener       结果监听
     */
    fun shareWithQZone(
        activity: Activity?,
        qqShareMessage: QqShareMessage?,
        listener: QqResultListener?
    ) {
        init()
        if (checkTencentAvailable()) return
        mQQType = SHARE
        mQqResultListener = listener
        if (qqShareMessage != null) {
            val bundle = Bundle()
            bundle.putInt(
                QzoneShare.SHARE_TO_QZONE_KEY_TYPE,
                QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT
            )
            if (qqShareMessage.title != null) bundle.putString(
                QzoneShare.SHARE_TO_QQ_TITLE,
                qqShareMessage.title
            ) //必填
            if (qqShareMessage.targetUrl != null) bundle.putString(
                QzoneShare.SHARE_TO_QQ_TARGET_URL,
                qqShareMessage.targetUrl
            ) //必填
            if (qqShareMessage.summary != null) bundle.putString(
                QzoneShare.SHARE_TO_QQ_SUMMARY,
                qqShareMessage.summary
            ) //选填
            if (qqShareMessage.imageUrl != null) {
                val imgUrlList = ArrayList<String?>()
                imgUrlList.add(qqShareMessage.imageUrl)
                bundle.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, imgUrlList) // 图片地址
            }
            mTencent!!.shareToQzone(activity, bundle, mIUiListenerIml)
        }
    }

    fun onActivityResultData(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = Tencent.onActivityResultData(requestCode, resultCode, data, mIUiListenerIml)
        logI("onActivityResultData：$result")
    }

    /**
     * 获取用户信息
     */
    fun getUserInfo(openId: String, accessToken: String?) {
        if (null == mTencent) return
        val userInfo = UserInfo(mContext, mTencent!!.qqToken)
        userInfo.getUserInfo(IUiUserInfoListenerIml(openId, accessToken))
    }

    /**
     * 获取UnionID
     *
     * "client_id": "",
     * "openid": "",
     * "unionid": ""
     *
     */
    fun getUnionID(userInfoVO: QqUserInfoVO) {
        if (mTencent == null) return
        lifecycleOwner?.lifecycleScope?.launch {
            val result = async(Dispatchers.IO) {
                QqApiManager.service.getUserInfo(
                    QqApiService.GET_UNIONID,
                    mTencent!!.accessToken,
                    "1"
                )
            }
            result.await()?.run {
                userInfoVO.client_id = client_id
                userInfoVO.openid = openid
                userInfoVO.unionid = unionid
            }
            mQqUserInfoResultListener?.onSuccess(userInfoVO)
        }
    }

    private fun checkTencentAvailable(): Boolean {
        return mTencent == null
        //        if (mTencent.isSessionValid())
//            return false;
    }

    /**
     * 登录、分享回调
     */
    private inner class IUiListenerIml : IUiListener {
        override fun onComplete(response: Any) {
            val jsonResponse = response as JSONObject
            if (jsonResponse.length() == 0) {
                logI("onComplete: 登录失败")
                if (mQQType == LOGIN) showToast("登录失败") else if (mQQType == SHARE) showToast("分享失败")
                return
            }
            logI("onComplete: " + jsonResponse.toJson())
            if (mQQType == LOGIN) {
                //showToast(mContext, "登录成功");
            } else if (mQQType == SHARE) showToast("分享成功")
            val ret = jsonResponse.optInt("ret")
            if (ret == 0) {
                val openId = jsonResponse.optString("openid")
                val accessToken = jsonResponse.optString("access_token")
                val expires = jsonResponse.optLong("expires_in")
                val vo: QqUserInfoVO = QqUserInfoVO().apply {
                    openid = openId
                    access_token = accessToken
                }
                if (mQqResultListener != null) mQqResultListener!!.onSuccess(vo)
                if (mQqUserInfoResultListener != null && mTencent != null) {
                    mTencent!!.openId = openId
                    mTencent!!.setAccessToken(accessToken, expires.toString())
                    getUserInfo(openId, accessToken)
                }
            }
        }

        override fun onError(uiError: UiError) {
            logE("onError: " + uiError.errorMessage)
            if (mQQType == LOGIN) showToast("登录失败") else if (mQQType == SHARE) showToast("分享失败")
            if (mQqResultListener != null) mQqResultListener!!.onFailure()
        }

        override fun onCancel() {
            logI("onCancel: ")
            if (mQQType == LOGIN) showToast("取消登录") else if (mQQType == SHARE) showToast("取消分享")
        }
    }

    /**
     * 用户信息回调
     */
    private inner class IUiUserInfoListenerIml(
        private val openId: String,
        private val accessToken: String?
    ) : IUiListener {
        override fun onComplete(response: Any) {
            val jsonResponse = response as JSONObject
            if (jsonResponse.length() == 0) {
                logI("onComplete: 获取用户信息失败")
                return
            }
            val ret = jsonResponse.optInt("ret")
            if (ret == 0) {
                try {
                    val json = jsonResponse.toString()
                    val qqUserInfoVO: QqUserInfoVO? = json.fromJson<QqUserInfoVO>()?.apply {
                        openid = openId
                        access_token = accessToken
                    }
                    //getUnionID(qqUserInfoVO);
                    mQqUserInfoResultListener?.onSuccess(qqUserInfoVO)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

        override fun onError(uiError: UiError) {}
        override fun onCancel() {}
    }

    private fun logI(message: String) {
        message.logI()
    }

    private fun logE(message: String) {
        message.logE()
    }

    private fun showToast(message: String) {
        mContext.showToast(message)
    }

    private fun onDestroy() {
        if (mTencent != null) mTencent = null
        if (mIUiListenerIml != null) mIUiListenerIml = null
        if (mQqResultListener != null) mQqResultListener = null
        if (mQqUserInfoResultListener != null) mQqUserInfoResultListener = null
        if (lifecycleOwner != null) lifecycleOwner = null
    }

    interface QqResultListener {
        fun onSuccess(response: QqUserInfoVO?)
        fun onFailure()
    }

    interface QqUserInfoResultListener {
        fun onSuccess(response: QqUserInfoVO?)
    }

    override fun onCreate(owner: LifecycleOwner) {
        lifecycleOwner = owner
        init()
    }

    override fun onDestroy(owner: LifecycleOwner) {
        onDestroy()
    }
}