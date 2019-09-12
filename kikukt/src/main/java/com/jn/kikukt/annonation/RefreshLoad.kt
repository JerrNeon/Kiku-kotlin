package com.jn.kikukt.annonation

import android.support.annotation.IntDef

/**
 * Author：Stevie.Chen Time：2019/7/10
 * Class Comment：
 */
const val ON_CREATE = 1//RecyclerView所在界面执行OnCreate方法
const val ON_REFRESH = 2//下拉刷新
const val ON_ONLOADMORE = 3//上拉加载更多
const val ON_RELOAD = 4//加载失败或重新加载

/**
 * RecyclerView界面操作类型
 */
@IntDef(ON_CREATE, ON_REFRESH, ON_ONLOADMORE, ON_RELOAD)
@Target(AnnotationTarget.FIELD, AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY
)
@Retention(AnnotationRetention.SOURCE)
annotation class RefreshOperateType

const val ALL = 0//刷新和加载更多都有
const val NONE = 1//纯列表不带刷新和加载更多
const val ONLY_REFRESH = 2//只有刷新
const val ONLY_LOADMORE = 3//只有加载更多

/**
 * RecyclerView类型
 */
@IntDef(ALL, NONE, ONLY_REFRESH, ONLY_LOADMORE)
@Target(AnnotationTarget.FIELD, AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.SOURCE)
annotation class RefreshViewType

const val TOTAL = 1//根据总数来判断
const val EMPTY = 2//根据下一页数据是否为空来判断
const val PAGE = 3//根据总页数来判断

/**
 * 是否可以加载更多的判断标志
 */
@IntDef(TOTAL, EMPTY, PAGE)
@Target(AnnotationTarget.FIELD, AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.SOURCE)
annotation class LoadMoreEnableType

const val SUCCESS = 1//加载成功
const val ERROR = 2//加载失败

/**
 * 加载完成类型
 */
@IntDef(SUCCESS, ERROR)
@Target(AnnotationTarget.FIELD, AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.SOURCE)
annotation class LoadCompleteType