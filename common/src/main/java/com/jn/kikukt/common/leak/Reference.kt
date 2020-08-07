package com.jn.kikukt.common.leak

import java.lang.ref.SoftReference
import java.lang.ref.WeakReference
import kotlin.properties.ReadOnlyProperty
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * Author：Stevie.Chen Time：2020/8/7
 * Class Comment：[WeakReference]属性委托
 * 可读写属性委托参考：[ReadWriteProperty]
 * 只读属性委托参考：[ReadOnlyProperty]
 */
class Weak<T : Any>(initializer: () -> T?) {
    private var weakReference: WeakReference<T?> = WeakReference(initializer())

    constructor() : this({
        null
    })

    operator fun getValue(thisRef: Any?, property: KProperty<*>): T? {
        return weakReference.get()
    }

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T?) {
        weakReference = WeakReference(value)
    }
}

/**
 * [SoftReference]属性委托
 */
class Soft<T : Any>(initializer: () -> T?) {
    private var softReference: SoftReference<T?> = SoftReference(initializer())

    constructor() : this({
        null
    })

    operator fun getValue(thisRef: Any?, property: KProperty<*>): T? {
        return softReference.get()
    }

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T?) {
        softReference = SoftReference(value)
    }
}