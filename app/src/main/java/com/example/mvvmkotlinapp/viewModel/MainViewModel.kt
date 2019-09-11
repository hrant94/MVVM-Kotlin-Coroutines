package com.example.mvvmkotlinapp.viewModel

import android.app.Application
import com.example.mvvmkotlinapp.model.requestModels.ResponseModel
import com.example.mvvmkotlinapp.shared.data.remote.networking.NetworkError
import com.example.mvvmkotlinapp.shared.data.remote.networking.ResponseCallBack
import com.example.mvvmkotlinapp.shared.data.remote.services.AuthorizationService
import com.example.mvvmkotlinapp.viewModel.root.BaseViewModel
import androidx.lifecycle.MutableLiveData
import com.example.mvvmkotlinapp.MVVMApp
import kotlinx.coroutines.*
import javax.inject.Inject

class MainViewModel(application: Application) : BaseViewModel(application) {
    @Inject
    lateinit var mService: AuthorizationService

    val smsLiveData = MutableLiveData<String>()

    init {
        MVVMApp.instance.getAuthorizationComponent().inject(this)
    }

    fun sendSMS(){
        scope.launch {
            mService.get("098678161", object : ResponseCallBack<ResponseModel<String>> {
                override fun onSuccess(data: ResponseModel<String>?) {
                    smsLiveData.postValue("success")
                }

                override fun onError(errorMessage: String) {
                    smsLiveData.postValue(errorMessage)
                }

                override fun onFailure(error: NetworkError) {
                    smsLiveData.postValue(error.getAppErrorMessage())
                }
            })
        }
    }
}