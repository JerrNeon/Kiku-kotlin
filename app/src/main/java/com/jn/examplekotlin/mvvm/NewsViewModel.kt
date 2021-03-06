package com.jn.examplekotlin.mvvm

import android.app.Application
import com.jn.examplekotlin.entiy.NewsVO
import com.jn.kikukt.mvvm.BaseViewModel

/**
 * Author：Stevie.Chen Time：2020/1/15
 * Class Comment：
 */
class NewsViewModel(application: Application) : BaseViewModel<NewsRepository>(application) {

    fun getNewsList(pageIndex: Int, pageSize: Int) {
        launch<List<NewsVO>>(block = {
            request {
                repository.getNewList(pageIndex, pageSize)
            }
            onSuccess {
                liveData.value = it
            }
            onFailure {
                liveData.value = it
            }
        }, isLoading = true)
    }

    fun getNewsList2(pageIndex: Int, pageSize: Int) {
        launch<List<NewsVO>> {
            onStart {
            }
            request {
                repository.getNewList(pageIndex, pageSize)
            }
            onSuccess {
                liveData.value = it
            }
            onFailure {
                liveData.value = it
            }
            onException {
            }
            onComplete {
            }
        }
    }

    fun getNewsList3(pageIndex: Int, pageSize: Int) {
        launchOnMain(tryBlock = {
            repository.getNewList(pageIndex, pageSize).execute({
                liveData.value = it
            })
        }, catchBlock = { e ->
            onFailure(e, error = {
                liveData.value = it
            })
        })
    }
}