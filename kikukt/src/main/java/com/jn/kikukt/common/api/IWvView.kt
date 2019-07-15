package com.jn.kikukt.common.api

/**
 * Author：Stevie.Chen Time：2019/7/11
 * Class Comment：WebView
 */
interface IWvView {

    /**
     * 初始化WebView
     */
    fun initWvView()

    /**
     * 初始化WebView
     */
    fun initWv()

    /**
     * 加载富文本
     *
     * @param htmlText
     */
    fun loadHtml(htmlText: String)

    /**
     * 加载Url
     *
     * @param url
     */
    fun loadUrl(url: String)

    /**
     * 配置加载信息
     */
    fun setWebViewClient()
}