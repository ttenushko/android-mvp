package com.ttenushko.androidmvp.demo.presentation.screens.home.placedetails.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.squareup.picasso.Picasso
import com.ttenushko.androidmvp.demo.di.dependency.ComponentDependencies
import com.ttenushko.androidmvp.demo.domain.application.usecase.DeletePlaceUseCase
import com.ttenushko.androidmvp.demo.domain.weather.usecase.GetCurrentWeatherConditionsUseCase
import com.ttenushko.androidmvp.demo.presentation.di.annotation.ViewModelKey
import com.ttenushko.androidmvp.demo.presentation.screens.home.HomeRouter
import com.ttenushko.androidmvp.demo.presentation.screens.home.placedetails.PlaceDetailsFragment
import com.ttenushko.androidmvp.demo.presentation.screens.home.placedetails.PlaceDetailsViewModel
import com.ttenushko.androidmvp.demo.presentation.utils.ViewModelFactory
import dagger.Binds
import dagger.Component
import dagger.Provides
import dagger.multibindings.IntoMap

interface PlaceDetailsFragmentDependencies : ComponentDependencies {
    fun getCurrentWeatherConditionsUseCase(): GetCurrentWeatherConditionsUseCase
    fun deletePlaceUseCase(): DeletePlaceUseCase
    fun picasso(): Picasso
    fun router(): HomeRouter
}

@dagger.Module
internal class PlaceDetailsFragmentModule(private val placeId: Long) {
    @Suppress("UNCHECKED_CAST")
    @Provides
    fun provideViewModel(
        getCurrentWeatherConditionsUseCase: GetCurrentWeatherConditionsUseCase,
        deletePlaceUseCase: DeletePlaceUseCase,
        router: HomeRouter
    ): PlaceDetailsViewModel =
        PlaceDetailsViewModel(
            placeId,
            getCurrentWeatherConditionsUseCase,
            deletePlaceUseCase,
            router
        )
}

@dagger.Module
internal abstract class ViewModelBindingModule {
    @Binds
    @IntoMap
    @ViewModelKey(PlaceDetailsViewModel::class)
    internal abstract fun bindViewModel(viewModel: PlaceDetailsViewModel): ViewModel

    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}


@Component(
    dependencies = [PlaceDetailsFragmentDependencies::class],
    modules = [PlaceDetailsFragmentModule::class, ViewModelBindingModule::class]
)
internal interface PlaceDetailsFragmentComponent {

    @Component.Builder
    interface Builder {
        fun placeDetailsFragmentDependencies(placeDetailsFragmentDependencies: PlaceDetailsFragmentDependencies): Builder
        fun placeDetailsFragmentModule(placeDetailsFragmentModule: PlaceDetailsFragmentModule): Builder
        fun build(): PlaceDetailsFragmentComponent
    }

    fun inject(fragment: PlaceDetailsFragment)
}
