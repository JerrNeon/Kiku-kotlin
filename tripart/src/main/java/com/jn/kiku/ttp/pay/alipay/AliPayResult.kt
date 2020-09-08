package com.jn.kiku.ttp.pay.alipayimport android.text.TextUtils/** * 支付返回实体 */class AliPayResult(rawResult: Map<String?, String?>?) {    /**     * @return the resultStatus     */    var resultStatus: String? = null    /**     * @return the result     */    var result: String? = null    /**     * @return the memo     */    var memo: String? = null    override fun toString(): String {        return ("resultStatus={" + resultStatus + "};memo={" + memo                + "};result={" + result + "}")    }    init {        if (rawResult != null) {            for (key in rawResult.keys) {                when {                    TextUtils.equals(key, "resultStatus") -> {                        resultStatus = rawResult[key]                    }                    TextUtils.equals(key, "result") -> {                        result = rawResult[key]                    }                    TextUtils.equals(key, "memo") -> {                        memo = rawResult[key]                    }                }            }        }    }}