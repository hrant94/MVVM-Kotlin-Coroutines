package com.example.mvvmkotlinapp


import com.example.mvvmkotlinapp.shared.di.components.IAuthorizationComponent
import com.example.mvvmkotlinapp.shared.di.components.root.IAppComponent
import com.example.mvvmkotlinapp.shared.di.modules.AuthorizationModule
import com.example.mvvmkotlinapp.shared.di.modules.root.AppModule
import com.example.mvvmkotlinapp.shared.di.modules.root.NetModule
import androidx.multidex.MultiDexApplication
import com.example.mvvmkotlinapp.shared.di.components.DaggerIAuthorizationComponent
import com.example.mvvmkotlinapp.shared.di.components.root.DaggerIAppComponent

class MVVMApp : MultiDexApplication() {
    private lateinit var mAppComponent: IAppComponent
    private var mAuthorizationComponent: IAuthorizationComponent? = null
    companion object {
        lateinit var instance: MVVMApp
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        mAppComponent = DaggerIAppComponent.builder()
            .appModule(AppModule(this))
            .netModule(NetModule())
            .build()
    }

    fun getAuthorizationComponent(): IAuthorizationComponent {
        mAuthorizationComponent = DaggerIAuthorizationComponent.builder()
            .iAppComponent(mAppComponent)
            .authorizationModule(AuthorizationModule())
            .build()
        return mAuthorizationComponent!!
    }

    fun releaseAuthorizationComponent() {
        mAuthorizationComponent = null
    }
}