package com.jn.kikukt.utils.manager

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.support.v4.app.Fragment
import java.io.Serializable
import java.util.*

/**
 * Author：Stevie.Chen Time：2019/7/15
 * Class Comment：界面跳转管理
 */
object IntentManager {

    /**
     * @param activity activity
     * @param cls      需要跳转的类
     * @param param    需要传递的参数，为空不传递参数
     */
    fun startActivity(activity: Activity, cls: Class<*>, param: Any?) {
        val intent = getIntent(cls.simpleName, param)
        intent.setClass(activity, cls)
        activity.startActivity(intent)
    }

    /**
     * @param fragment fragment
     * @param cls      需要跳转的类
     * @param param    需要传递的参数，为空不传递参数
     */
    fun startActivity(fragment: Fragment, cls: Class<*>, param: Any?) {
        val intent = getIntent(cls.simpleName, param)
        intent.setClass(Objects.requireNonNull(fragment.activity), cls)
        fragment.startActivity(intent)
    }

    /**
     * @param activity    activity
     * @param cls         需要跳转的类
     * @param param       需要传递的参数，为空不传递参数
     * @param requestCode 请求码，用于目标界面返回回来的参数区分
     */
    fun startActivity(activity: Activity, cls: Class<*>, param: Any?, requestCode: Any?) {
        if (requestCode != null) {
            if (requestCode is Int) {
                val intent = getIntent(cls.simpleName, param)
                intent.setClass(activity, cls)
                activity.startActivityForResult(intent, requestCode)
            } else
                throw IllegalArgumentException("requestCode no support other type,only Integer")
        } else {
            startActivity(activity, cls, param)
        }
    }

    /**
     * @param fragment    fragment
     * @param cls         需要跳转的类
     * @param param       需要传递的参数，为空不传递参数
     * @param requestCode 请求码，用于目标界面返回回来的参数区分
     */
    fun startActivity(fragment: Fragment, cls: Class<*>, param: Any?, requestCode: Any?) {
        if (requestCode != null) {
            if (requestCode is Int) {
                val intent = getIntent(cls.simpleName, param)
                intent.setClass(Objects.requireNonNull(fragment.activity), cls)
                fragment.startActivityForResult(intent, requestCode)
            } else
                throw IllegalArgumentException("requestCode no support other type,only Integer")
        } else {
            startActivity(fragment, cls, param)
        }
    }

    /**
     * 通过类所在包名启动Activity，并且含有Bundle数据和返回数据
     *
     * @param activity          activity
     * @param targetPackageName 需要跳转的类所在完整包名
     * @param bundle            数据
     */
    @Throws(ClassNotFoundException::class)
    fun startActivity(activity: Activity, targetPackageName: String, bundle: Bundle?) {
        val intent = getIntent(targetPackageName, bundle)
        intent.setClass(Objects.requireNonNull(activity), Class.forName(targetPackageName))
        activity.startActivity(intent)
    }

    /**
     * 通过类所在包名启动Activity，并且含有Bundle数据和返回数据
     *
     * @param fragment          fragment
     * @param targetPackageName 需要跳转的类所在完整包名
     * @param bundle            数据
     */
    @Throws(ClassNotFoundException::class)
    fun startActivity(fragment: Fragment, targetPackageName: String, bundle: Bundle?) {
        val intent = getIntent(targetPackageName, bundle)
        intent.setClass(Objects.requireNonNull(fragment.activity), Class.forName(targetPackageName))
        fragment.startActivity(intent)
    }

    /**
     * 获得Fragment对象并传递参数
     *
     * @param params 要传递的参数
     * @param tClass 传递的目的Fragment的Class对象
     * @param <T>    传递的目的Fragment
     * @return
    </T> */
    fun <T : Fragment> newInstance(tClass: Class<T>, params: Any?): T {
        var fragment: T? = null
        try {
            fragment = tClass.newInstance()
        } catch (e: InstantiationException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }

        val bundle = getBundle(tClass.simpleName, params)
        assert(fragment != null)
        fragment!!.arguments = bundle
        return fragment
    }

    /**
     * 获取Intent对象
     *
     * @param key   需要传递的参数Key
     * @param param 需要传递的参数，为空不传递参数
     * @return
     */
    private fun getIntent(key: String, param: Any?): Intent {
        val intent = Intent()
        if (param != null) {
            when (param) {
                is Int -> intent.putExtra(key, param)
                is Long -> intent.putExtra(key, param)
                is Float -> intent.putExtra(key, param)
                is Double -> intent.putExtra(key, param)
                is String -> intent.putExtra(key, param as String?)
                is Bundle -> intent.putExtras((param as Bundle?)!!)
                is List<*> -> {
                    val list = param as List<*>?
                    intent.putExtra(key, list as Serializable?)
                }
                is Map<*, *> -> {
                    val map = param as Map<*, *>?
                    intent.putExtra(key, map as Serializable?)
                }
                is Serializable -> intent.putExtra(key, param as Serializable?)
                is Parcelable -> intent.putExtra(key, param as Parcelable?)
            }
        }
        return intent
    }

    /**
     * 获取Bundle对象
     *
     * @param key   需要传递的参数Key
     * @param param 需要传递的参数，为空不传递参数
     * @return
     */
    private fun getBundle(key: String, param: Any?): Bundle {
        var bundle = Bundle()
        if (param != null) {
            when (param) {
                is Int -> bundle.putInt(key, param)
                is Long -> bundle.putLong(key, param)
                is Float -> bundle.putFloat(key, param)
                is Double -> bundle.putDouble(key, param)
                is String -> bundle.putString(key, param as String?)
                is Bundle -> bundle = param
                is List<*> -> {
                    val list = param as List<*>?
                    bundle.putSerializable(key, list as Serializable?)
                }
                is Map<*, *> -> {
                    val map = param as Map<*, *>?
                    bundle.putSerializable(key, map as Serializable?)
                }
                is Serializable -> bundle.putSerializable(key, param as Serializable?)
                is Parcelable -> bundle.putParcelable(key, param as Parcelable?)
            }
        }
        return bundle
    }

    /**
     * 获取上一个界面传递过来的参数
     *
     * @param activity      activity
     * @param cls           当前界面Class
     * @param defaultObject 默认值
     * @return
     */
    fun getParam(activity: Activity, cls: Class<*>, defaultObject: Any?): Any? {
        return if (defaultObject is Bundle)
            activity.intent.extras
        else {
            getParam(activity.intent.extras, cls.simpleName, defaultObject)
        }
    }

    /**
     * 获取上一个界面传递过来的参数
     *
     * @param fragment      fragment
     * @param cls           当前界面Class
     * @param defaultObject 默认值
     * @return
     */
    fun getParam(fragment: Fragment, cls: Class<*>, defaultObject: Any?): Any? {
        return if (defaultObject is Bundle)
            fragment.arguments
        else {
            getParam(fragment.arguments, cls.simpleName, defaultObject)
        }
    }

    /**
     * 获取参数
     *
     * @param bundle        bundle
     * @param key           对应的key
     * @param defaultObject 默认值
     * @return
     * @throws Exception
     */
    fun getParam(bundle: Bundle?, key: String, defaultObject: Any?): Any? {
        try {
            if (bundle != null) {
                if (defaultObject == null)
                    return null
                else {
                    when (defaultObject) {
                        is Int -> return bundle.getInt(key, defaultObject)
                        is Long -> return bundle.getLong(key, defaultObject)
                        is Float -> return bundle.getFloat(key, defaultObject)
                        is Double -> return bundle.getDouble(key, defaultObject)
                        is String -> return if (bundle.getString(key) != null) bundle.getString(key) else defaultObject
                        is List<*> -> {
                            val list = bundle.getSerializable(key) as List<*>
                            return list
                        }
                        is Map<*, *> -> {
                            return bundle.getSerializable(key) as Map<*, *>
                        }
                        is Serializable -> return bundle.getSerializable(key)
                        is Parcelable -> return bundle.getParcelable(key)
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }
}