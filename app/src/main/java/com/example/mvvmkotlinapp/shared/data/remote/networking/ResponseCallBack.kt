package com.example.mvvmkotlinapp.shared.data.remote.networking

interface ResponseCallBack<T> {
    fun onSuccess(data: T?)

    fun onError(errorMessage: String)

    fun onFailure(error: NetworkError)
}