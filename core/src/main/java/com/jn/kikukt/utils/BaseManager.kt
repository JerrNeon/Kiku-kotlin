package com.jn.kikukt.utils

import android.app.Activity
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import com.jn.kikukt.R
import com.jn.kikukt.common.utils.statusbar.setColorNoTranslucent
import org.greenrobot.eventbus.EventBus

/**
 * Author：Stevie.Chen Time：2019/9/11
 * Class Comment：
 */
fun Activity.initEventBus() {
    EventBus.getDefault().register(this)
}

fun Activity.unregisterEventBus() {
    if (EventBus.getDefault().isRegistered(this))
        EventBus.getDefault().unregister(this)
}

fun Activity.setStatusBar(@ColorRes id: Int = R.color.colorPrimary) {
    setColorNoTranslucent(ContextCompat.getColor(applicationContext, id))
}