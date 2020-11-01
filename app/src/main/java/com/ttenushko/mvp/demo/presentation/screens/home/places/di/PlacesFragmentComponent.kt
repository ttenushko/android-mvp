package com.ttenushko.mvp.demo.presentation.screens.home.places.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ttenushko.mvp.demo.di.dependency.ComponentDependencies
import com.ttenushko.mvp.demo.domain.application.usecase.TrackSavedPlacesUseCase
import com.ttenushko.mvp.demo.presentation.di.annotation.ViewModelKey
import com.ttenushko.mvp.demo.presentation.screens.home.HomeRouter
import com.ttenushko.mvp.demo.presentation.screens.home.places.PlacesFragment
import com.ttenushko.mvp.demo.presentation.screens.home.places.PlacesViewModel
import com.ttenushko.mvp.demo.presentation.utils.ViewModelFactory
import dagger.Binds
import dagger.Component
import dagger.Provides
import dagger.multibindings.IntoMap

interface PlacesFragmentDependencies : ComponentDependencies {
    fun router(): HomeRouter
    fun trackSavedPlacesUseCase(): TrackSavedPlacesUseCase
}

@dagger.Module
internal class PlacesFragmentModule {
    @Suppress("UNCHECKED_CAST")
    @Provides
    fun provideViewModel(
        router: HomeRouter,
        trackSavedPlacesUseCase: TrackSavedPlacesUseCase
    ): PlacesViewModel =
        PlacesViewModel(
            trackSavedPlacesUseCase,
            router
        )
}

@dagger.Module
internal abstract class ViewModelBindingModule {
    @Binds
    @IntoMap
    @ViewModelKey(PlacesViewModel::class)
    internal abstract fun bindViewModel(viewModel: PlacesViewModel): ViewModel

    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}

@Component(
    dependencies = [PlacesFragmentDependencies::class],
    modules = [PlacesFragmentModule::class, ViewModelBindingModule::class]
)
internal interface PlacesFragmentComponent {
    fun inject(fragment: PlacesFragment)
}

