package com.example.mvvmkotlinapp.shared.di.modules.root


import com.example.mvvmkotlinapp.shared.helpers.SharedPreferencesHelper
import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import com.example.mvvmkotlinapp.BuildConfig
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class NetModule {
    private val mMaxStale = 60 * 60 * 24 * 5 // tolerate 5-days stale

    @Provides
    @Singleton
    fun provideOkHttpCache(application: Application): Cache {
        val cacheSize = 10 * 1024 * 1024
        return Cache(application.cacheDir, cacheSize.toLong())
    }

    @Provides
    @Singleton
    fun provideGson(): Gson {
        val gsonBuilder = GsonBuilder()
        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
        return gsonBuilder.create()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(cache: Cache, application: Application): OkHttpClient {
        val shared = SharedPreferencesHelper(application)
        return OkHttpClient.Builder().addInterceptor { chain ->
            val original = chain.request()
            // Customize the request
            val request = original.newBuilder()
                .header("Content-Type", "application/json")
                /*.header(
                    "Authorization",
                    "Bearer " + shared.getStringSharedPreferences(Constants.TOKEN)
                )*/
                //.header("deviceToken", Objects.requireNonNull(FirebaseInstanceId.getInstance().getToken()))
                .removeHeader("Pragma")
                .header(
                    "Cache-Control", if (isNetworkAvailable(application))
                        "public, max-age=$mMaxStale"
                    else
                        "public, max-stale=$mMaxStale"
                )
                .build()
            val response = chain.proceed(request)
            response.cacheResponse()
            // Customize or return the response
            response
        }
            .cache(cache)
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASEURL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addConverterFactory(ScalarsConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()
    }


    private fun isNetworkAvailable(context: Context): Boolean {
        var status = false
        try {
            val cm = context
                .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            var netInfo = cm.getNetworkInfo(0)

            if (netInfo != null && netInfo.state == NetworkInfo.State.CONNECTED) {
                status = true
            } else {
                netInfo = cm.getNetworkInfo(1)
                if (netInfo != null && netInfo.state == NetworkInfo.State.CONNECTED)
                    status = true
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }

        return status
    }
}