package com.example.mvvmkotlinapp.shared.di.modules

import com.example.mvvmkotlinapp.shared.data.remote.api.IAuthorizationService
import com.example.mvvmkotlinapp.shared.data.remote.services.AuthorizationService
import com.example.mvvmkotlinapp.shared.di.scopes.AuthorizationScope
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
class AuthorizationModule {

    @Provides
    @AuthorizationScope
    fun provideIAuthorizationService(retrofit: Retrofit): IAuthorizationService {
        return retrofit.create(IAuthorizationService::class.java)
    }


    @Provides
    @AuthorizationScope
    fun providesAuthorizationService(service: IAuthorizationService): AuthorizationService {
        return AuthorizationService(service)
    }
}