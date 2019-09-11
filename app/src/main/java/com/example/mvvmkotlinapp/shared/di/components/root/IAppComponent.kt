package com.example.mvvmkotlinapp.shared.di.components.root

import com.example.mvvmkotlinapp.shared.di.modules.root.AppModule
import com.example.mvvmkotlinapp.shared.di.modules.root.NetModule
import com.example.mvvmkotlinapp.shared.helpers.SharedPreferencesHelper
import android.content.Context
import dagger.Component
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, NetModule::class])
interface IAppComponent {
    fun retrofit(): Retrofit

    fun okHttpClient(): OkHttpClient

    fun context(): Context

    fun sharedPreferences(): SharedPreferencesHelper
}