package com.jn.kikukt.mvp

import com.trello.rxlifecycle2.android.ActivityEvent
import io.reactivex.subjects.BehaviorSubject

/**
 * Author：Stevie.Chen Time：2019/7/11
 * Class Comment：
 */
class BaseModel : IBModel {

    override var mBehaviorSubject: BehaviorSubject<ActivityEvent>? = null

    init {
        mBehaviorSubject = createLifecycleBehaviorSubject()
    }

}