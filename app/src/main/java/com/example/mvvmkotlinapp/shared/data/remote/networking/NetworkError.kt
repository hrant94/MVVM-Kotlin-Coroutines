package com.example.mvvmkotlinapp.shared.data.remote.networking

import com.example.mvvmkotlinapp.view.activities.MainActivity
import com.example.mvvmkotlinapp.MVVMApp
import com.example.mvvmkotlinapp.shared.helpers.SharedPreferencesHelper
import android.content.Context
import android.content.Context.CONNECTIVITY_SERVICE
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import com.example.mvvmkotlinapp.R
import retrofit2.HttpException
import java.net.HttpURLConnection.HTTP_FORBIDDEN
import java.net.HttpURLConnection.HTTP_UNAUTHORIZED


class NetworkError(var mError: Throwable) : Throwable(mError) {
    private var mContext: Context = MVVMApp.instance

    init {
        isAuthFailure()
    }

    private fun isAuthFailure(): Boolean {
        return if (mError is HttpException) {
            if ((mError as HttpException).code() == HTTP_UNAUTHORIZED || (mError as HttpException).code() == HTTP_FORBIDDEN) {
                SharedPreferencesHelper(mContext).deleteSharedPreferences()
                val intent = Intent(mContext, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                mContext.startActivity(intent)
                true
            } else
                false
        } else
            false
    }

    fun getError(): Throwable {
        return mError
    }

    fun getAppErrorMessage(): String {
        return if(isInternetAvailable())
            mContext.resources.getString(R.string.default_error_message)
        else
            mContext.resources.getString(R.string.network_error_message)

    }

    @Suppress("DEPRECATION")
    fun isInternetAvailable(): Boolean {
        var result = false
        val cm = MVVMApp.instance.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager?
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            cm?.run {
                cm.getNetworkCapabilities(cm.activeNetwork)?.run {
                    result = when {
                        hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                        hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                        hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                        else -> false
                    }
                }
            }
        } else {
            cm?.run {
                cm.activeNetworkInfo?.run {
                    if (type == ConnectivityManager.TYPE_WIFI) {
                        result = true
                    } else if (type == ConnectivityManager.TYPE_MOBILE) {
                        result = true
                    }
                }
            }
        }
        return result
    }

}
