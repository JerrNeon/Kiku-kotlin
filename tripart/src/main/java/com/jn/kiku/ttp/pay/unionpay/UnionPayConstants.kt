package com.jn.kiku.ttp.pay.unionpay

/**
 * Author：Stevie.Chen Time：2020/09/08 14:59
 * Class Comment：银联支付常量
 */
object UnionPayConstants {
    /**
     * 插件可用
     */
    const val PLUGIN_VALID = 0

    /**
     * 插件未导入
     */
    const val PLUGIN_NOT_INSTALLED = -1

    /**
     * 插件更新
     */
    const val PLUGIN_NEED_UPGRADE = 2

    /**
     * 流水号获取地址
     */
    const val GET_TN_URL = "http://202.101.25.178:8080/sim/gettn"

    /**
     * 银联正式环境
     */
    const val UNION_OFFICIAL_CONNECT = "00"

    /**
     * 银联测试环境
     */
    const val UNION_TEST_CONNECT = "01"
}