package com.example.mvvmkotlinapp.shared.data.remote.api

import com.example.mvvmkotlinapp.model.requestModels.ResponseModel
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface IAuthorizationService {
    @GET("api/user/getVC")
    fun verificationCodeAsync(@Query("phone") phone: String): Deferred<Response<ResponseModel<String>>>
}