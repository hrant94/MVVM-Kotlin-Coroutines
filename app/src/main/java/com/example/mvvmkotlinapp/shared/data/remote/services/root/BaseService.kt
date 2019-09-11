package com.example.mvvmkotlinapp.shared.data.remote.services.root

import com.example.mvvmkotlinapp.model.requestModels.ResponseModel
import com.example.mvvmkotlinapp.shared.data.remote.networking.NetworkError
import com.example.mvvmkotlinapp.shared.data.remote.networking.ResponseCallBack
import retrofit2.HttpException
import retrofit2.Response

open class BaseService {
    suspend fun <T : Any> safeApiCall(
        call: suspend () -> Response<ResponseModel<T>>,
        callBack: ResponseCallBack<ResponseModel<T>>
    ) {
        try {
            val response = call.invoke()

            if (!response.isSuccessful) {
                callBack.onFailure(NetworkError(HttpException(response)))
                return
            }

            if (!response.body()?.success!!) {
                callBack.onError(response.body()?.message!!)
                return
            }

            callBack.onSuccess(response.body())


        } catch (e: Throwable) {
            callBack.onFailure(NetworkError(e))
        }
    }
}