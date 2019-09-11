package com.example.mvvmkotlinapp.shared.data.remote.services

import com.example.mvvmkotlinapp.model.requestModels.ResponseModel
import com.example.mvvmkotlinapp.shared.data.remote.api.IAuthorizationService
import com.example.mvvmkotlinapp.shared.data.remote.networking.ResponseCallBack
import com.example.mvvmkotlinapp.shared.data.remote.services.root.BaseService

class AuthorizationService(private val mService: IAuthorizationService) : BaseService() {
    suspend fun get(phone: String, callBack: ResponseCallBack<ResponseModel<String>>) {
        safeApiCall(call = { mService.verificationCodeAsync(phone).await() }, callBack = callBack)
    }
}