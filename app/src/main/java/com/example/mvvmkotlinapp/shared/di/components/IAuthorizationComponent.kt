package com.example.mvvmkotlinapp.shared.di.components

import com.example.mvvmkotlinapp.shared.di.components.root.IAppComponent
import com.example.mvvmkotlinapp.shared.di.modules.AuthorizationModule
import com.example.mvvmkotlinapp.shared.di.scopes.AuthorizationScope
import com.example.mvvmkotlinapp.viewModel.MainViewModel
import dagger.Component

@AuthorizationScope
@Component(dependencies = [IAppComponent::class], modules = [AuthorizationModule::class])
interface IAuthorizationComponent {
    fun inject(mainViewModel: MainViewModel)
}