package com.jn.kikukt.net.retrofit.exception

/**
 * Author：Stevie.Chen Time：2019/7/15
 * Class Comment：
 */
class OkHttpException : Exception {

    var errorCode: String? = null
    var errorMsg: String? = null

    constructor()

    constructor(errorMsg: String) : super(errorMsg) {
        this.errorMsg = errorMsg
    }

    constructor(cause: Throwable) : super(cause)

    constructor(cause: Throwable, errorMsg: String) : super(cause) {
        this.errorMsg = errorMsg
    }

    constructor(errorCode: String, errorMsg: String) : super(errorMsg) {
        this.errorCode = errorCode
        this.errorMsg = errorMsg
    }

}