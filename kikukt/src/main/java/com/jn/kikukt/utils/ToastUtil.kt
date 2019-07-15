package com.jn.kikukt.utils

import android.content.Context
import android.support.v4.app.Fragment
import android.widget.Toast

/**
 * Author：Stevie.Chen Time：2019/7/11
 * Class Comment：
 */
private var oldMsg: String? = null
private var oneTime: Long = 0
private var twoTime: Long = 0
private var mToast: Toast? = null

fun Context.showToast(message: String) {
    showToast(message, duration = Toast.LENGTH_SHORT)
}

fun Context.showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    if (mToast == null) {
        mToast = Toast.makeText(this.applicationContext, message, duration)
        mToast?.show()
        oneTime = System.currentTimeMillis()
    } else {
        twoTime = System.currentTimeMillis()
        if (message == oldMsg) {
            if (twoTime - oneTime > Toast.LENGTH_SHORT) {
                mToast?.show()
            }
        } else {
            oldMsg = message
            mToast?.setText(message)
            mToast?.show()
        }
    }
    oneTime = twoTime
}

fun Fragment.showToast(message: String) {
    showToast(message, duration = Toast.LENGTH_SHORT)
}

fun Fragment.showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    if (mToast == null) {
        mToast = Toast.makeText(this.context?.applicationContext, message, duration)
        mToast?.show()
        oneTime = System.currentTimeMillis()
    } else {
        twoTime = System.currentTimeMillis()
        if (message == oldMsg) {
            if (twoTime - oneTime > Toast.LENGTH_SHORT) {
                mToast?.show()
            }
        } else {
            oldMsg = message
            mToast?.setText(message)
            mToast?.show()
        }
    }
    oneTime = twoTime
}