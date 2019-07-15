package com.jn.kikukt.retrofit.exception

/**
 * Author：Stevie.Chen Time：2019/7/15
 * Class Comment：
 */
class OkHttpException : Exception {

    private var errorMsg: String? = null

    constructor()

    constructor(errorMsg: String) : super(errorMsg) {
        this.errorMsg = errorMsg
    }

    constructor(cause: Throwable) : super(cause)

    constructor(cause: Throwable, errorMsg: String) : super(cause) {
        this.errorMsg = errorMsg
    }

    fun getErrorMsg(): String? {
        return errorMsg
    }
}