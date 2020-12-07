package com.jn.kikukt.common.api

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.jn.kikukt.activity.RootActivity
import com.jn.kikukt.common.utils.log
import com.jn.kikukt.fragment.RootVBFragment
import java.lang.reflect.ParameterizedType

/**
 * Author：Stevie.Chen Time：2020/12/4
 * Class Comment：
 */
interface IViewBindingView {

    val viewBinding: ViewBinding?

    fun initViewBinding(inflater: LayoutInflater, container: ViewGroup?, attachToRoot: Boolean)
}

/**
 * 用于Activity
 */
@Suppress("UNCHECKED_CAST")
fun <VB : ViewBinding> RootActivity.initViewBindings(layoutInflater: LayoutInflater): VB? {
    try {
        //使用反射得到viewBinding的class
        val type = javaClass.genericSuperclass as ParameterizedType
        val aClass = type.actualTypeArguments[0] as Class<*>
        val method = aClass.getDeclaredMethod("inflate", LayoutInflater::class.java)
        return method.invoke(null, layoutInflater) as VB
    } catch (e: Exception) {
        e.log()
    }
    return null
}

/**
 * 用于Fragment
 */
@Suppress("UNCHECKED_CAST")
fun <VB : ViewBinding> RootVBFragment<VB>.initViewBindings(
    layoutInflater: LayoutInflater,
    container: ViewGroup?,
    attachToRoot: Boolean,
): VB? {
    try {
        //使用反射得到viewBinding的class
        val type = javaClass.genericSuperclass as ParameterizedType
        val aClass = type.actualTypeArguments[0] as Class<*>
        val method = aClass.getDeclaredMethod(
            "inflate",
            LayoutInflater::class.java,
            ViewGroup::class.java,
            Boolean::class.java
        )
        return method.invoke(null, layoutInflater, container, attachToRoot) as VB
    } catch (e: Exception) {
        e.log()
    }
    return null
}