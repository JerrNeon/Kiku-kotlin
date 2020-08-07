package com.jn.kikukt.common.ext

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment

/**
 * Author：Stevie.Chen Time：2020/8/7
 * Class Comment：
 */
inline fun <reified T> Activity.startActivity() {
    startActivity(Intent(this, T::class.java))
}

inline fun <reified T> Activity.startActivity(block: Intent.() -> Unit) {
    startActivity(Intent(this, T::class.java).apply(block))
}

inline fun <reified T> Activity.getExtra(key: String): T? {
    return intent.extras?.get(key) as? T
}

inline fun <reified T> Fragment.startActivity() {
    startActivity(Intent(context, T::class.java))
}

inline fun <reified T> Fragment.startActivity(block: Intent.() -> Unit) {
    startActivity(Intent(context, T::class.java).apply(block))
}

inline fun <reified T> Context.startActivity() {
    startActivity(Intent(this, T::class.java))
}

inline fun <reified T> Context.startActivity(block: Intent.() -> Unit) {
    startActivity(Intent(this, T::class.java).apply(block))
}