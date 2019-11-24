package com.ttenushko.androidmvp.demo.presentation.screens.home.addplace.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ttenushko.androidmvp.demo.di.dependency.ComponentDependencies
import com.ttenushko.androidmvp.demo.domain.application.usecase.SavePlaceUseCase
import com.ttenushko.androidmvp.demo.domain.weather.usecase.SearchPlaceUseCase
import com.ttenushko.androidmvp.demo.presentation.di.annotation.ViewModelKey
import com.ttenushko.androidmvp.demo.presentation.screens.home.HomeRouter
import com.ttenushko.androidmvp.demo.presentation.screens.home.addplace.AddPlaceFragment
import com.ttenushko.androidmvp.demo.presentation.screens.home.addplace.AddPlaceViewModel
import com.ttenushko.androidmvp.demo.presentation.utils.ViewModelFactory
import dagger.Binds
import dagger.Component
import dagger.Provides
import dagger.multibindings.IntoMap

interface AddPlaceFragmentDependencies : ComponentDependencies {
    fun searchPlaceUseCase(): SearchPlaceUseCase
    fun savePlaceUseCase(): SavePlaceUseCase
    fun router(): HomeRouter
}

@dagger.Module
internal class AddPlaceFragmentModule(private val search: String) {
    @Suppress("UNCHECKED_CAST")
    @Provides
    fun provideViewModel(
        searchPlaceUseCase: SearchPlaceUseCase,
        savePlaceUseCase: SavePlaceUseCase,
        router: HomeRouter
    ): AddPlaceViewModel =
        AddPlaceViewModel(
            search,
            searchPlaceUseCase,
            savePlaceUseCase,
            router
        )
}

@dagger.Module
internal abstract class ViewModelBindingModule {
    @Binds
    @IntoMap
    @ViewModelKey(AddPlaceViewModel::class)
    internal abstract fun bindViewModel(viewModel: AddPlaceViewModel): ViewModel

    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}

@Component(
    dependencies = [AddPlaceFragmentDependencies::class],
    modules = [AddPlaceFragmentModule::class, ViewModelBindingModule::class]
)
internal interface AddPlaceFragmentComponent {

    @Component.Builder
    interface Builder {
        fun addPlaceFragmentDependencies(addPlaceFragmentDependencies: AddPlaceFragmentDependencies): Builder
        fun addPlaceFragmentModule(addPlaceFragmentModule: AddPlaceFragmentModule): Builder
        fun build(): AddPlaceFragmentComponent
    }

    fun inject(fragment: AddPlaceFragment)
}
