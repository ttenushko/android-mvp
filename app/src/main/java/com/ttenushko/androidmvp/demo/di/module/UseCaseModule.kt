package com.ttenushko.androidmvp.demo.di.module

import com.ttenushko.androidmvp.demo.domain.application.repository.ApplicationSettings
import com.ttenushko.androidmvp.demo.domain.application.usecase.*
import com.ttenushko.androidmvp.demo.domain.weather.repository.WeatherForecastRepository
import com.ttenushko.androidmvp.demo.domain.weather.usecase.GetCurrentWeatherConditionsUseCase
import com.ttenushko.androidmvp.demo.domain.weather.usecase.GetCurrentWeatherConditionsUseCaseImpl
import com.ttenushko.androidmvp.demo.domain.weather.usecase.SearchPlaceUseCase
import com.ttenushko.androidmvp.demo.domain.weather.usecase.SearchPlaceUseCaseImpl
import dagger.Module
import dagger.Provides

@Module
class UseCaseModule {

    @Provides
    fun provideDeletePlaceUseCase(applicationSettings: ApplicationSettings): DeletePlaceUseCase =
        DeletePlaceUseCaseImpl(applicationSettings)

    @Provides
    fun provideGetSavedPlacesUseCase(applicationSettings: ApplicationSettings): GetSavedPlacesUseCase =
        GetSavedPlacesUseCaseImpl(applicationSettings)

    @Provides
    fun provideSavePlaceUseCase(applicationSettings: ApplicationSettings): SavePlaceUseCase =
        SavePlaceUseCaseImpl(applicationSettings)

    @Provides
    fun provideTrackSavedPlacesUseCase(applicationSettings: ApplicationSettings): TrackSavedPlacesUseCase =
        TrackSavedPlacesUseCaseImpl(applicationSettings)

    @Provides
    fun provideGetCurrentWeatherConditionsUseCase(weatherForecastRepository: WeatherForecastRepository): GetCurrentWeatherConditionsUseCase =
        GetCurrentWeatherConditionsUseCaseImpl(weatherForecastRepository)

    @Provides
    fun provideSearchPlaceUseCase(weatherForecastRepository: WeatherForecastRepository): SearchPlaceUseCase =
        SearchPlaceUseCaseImpl(weatherForecastRepository, 300)
}