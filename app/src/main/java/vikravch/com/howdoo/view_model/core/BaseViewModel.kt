package com.vikravch.poloniexproject.view_model.core

import androidx.lifecycle.ViewModel
import vikravch.com.howdoo.view_model.dagger_injections.NetworkModule
import vikravch.com.howdoo.view_model.dagger_injections.ViewModelInjector
import vikravch.com.howdoo.view_model.core.ItemViewModel
import vikravch.com.howdoo.view_model.core.MainActivityViewModel
import vikravch.com.howdoo.view_model.dagger_injections.DaggerViewModelInjector

abstract class BaseViewModel: ViewModel(){
    private val injector: ViewModelInjector = DaggerViewModelInjector
            .builder()
            .networkModule(NetworkModule)
            .build()

    init {
        inject()
    }

    private fun inject() {
        when (this) {
            is MainActivityViewModel -> injector.inject(this)
            is ItemViewModel -> injector.inject(this)
        }
    }
}