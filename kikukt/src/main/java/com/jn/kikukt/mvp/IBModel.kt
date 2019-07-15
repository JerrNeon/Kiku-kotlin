package com.jn.kikukt.mvp

import com.trello.rxlifecycle2.LifecycleProvider
import com.trello.rxlifecycle2.LifecycleTransformer
import com.trello.rxlifecycle2.RxLifecycle
import com.trello.rxlifecycle2.android.ActivityEvent
import com.trello.rxlifecycle2.android.RxLifecycleAndroid
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject

/**
 * Author：Stevie.Chen Time：2019/7/11
 * Class Comment：
 */
interface IBModel : LifecycleProvider<ActivityEvent> {

    val mBehaviorSubject: BehaviorSubject<ActivityEvent>?

    fun createLifecycleBehaviorSubject(): BehaviorSubject<ActivityEvent> {
        return BehaviorSubject.create()
    }

    fun getLifecycleBehaviorSubject(): BehaviorSubject<ActivityEvent> {
        return mBehaviorSubject!!
    }

    override fun lifecycle(): Observable<ActivityEvent> {
        return getLifecycleBehaviorSubject().hide()
    }

    override fun <T : Any?> bindUntilEvent(event: ActivityEvent): LifecycleTransformer<T> {
        return RxLifecycle.bindUntilEvent(getLifecycleBehaviorSubject(), event)
    }

    override fun <T : Any?> bindToLifecycle(): LifecycleTransformer<T> {
        return RxLifecycleAndroid.bindActivity(getLifecycleBehaviorSubject())
    }

}