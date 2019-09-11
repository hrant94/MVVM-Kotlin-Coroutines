package com.example.mvvmkotlinapp.model.requestModels

class ResponseModel<T>{
    val success: Boolean? = false
    val message: String? = null
    val data: T? = null
}