package com.ttenushko.androidmvp.demo.di.module

import android.content.Context
import com.ttenushko.androidmvp.demo.Config
import com.ttenushko.androidmvp.demo.data.application.repository.ApplicationSettingsImpl
import com.ttenushko.androidmvp.demo.data.weather.repository.WeatherForecastRepositoryImpl
import com.ttenushko.androidmvp.demo.di.annotation.ApplicationScope
import com.ttenushko.androidmvp.demo.domain.application.repository.ApplicationSettings
import com.ttenushko.androidmvp.demo.domain.weather.repository.WeatherForecastRepository
import com.ttenushko.androidmvp.demo.platform.android.weather.datasource.OpenWeatherMapDataSource
import com.ttenushko.androidmvp.demo.platform.android.weather.datasource.rest.OpenWeatherMapApi
import com.ttenushko.androidmvp.demo.platform.android.weather.datasource.rest.OpenWeatherMapApiFactory
import dagger.Module
import dagger.Provides

@Module
class DataModule(
    private val isDebug: Boolean
) {
    @Provides
    @ApplicationScope
    fun provideApplicationSettings(context: Context): ApplicationSettings =
        ApplicationSettingsImpl(context)

    @Provides
    @ApplicationScope
    fun provideOpenWeatherMapApi(context: Context): OpenWeatherMapApi =
        OpenWeatherMapApiFactory.create(
            context,
            Config.OPEN_WEATHER_MAP_API_BASE_URL,
            Config.OPEN_WEATHER_MAP_API_KEY,
            isDebug
        )

    @Provides
    @ApplicationScope
    fun provideWeatherForecastRepository(dataSource: WeatherForecastRepositoryImpl.DataSource): WeatherForecastRepository =
        WeatherForecastRepositoryImpl(dataSource)

    @Provides
    @ApplicationScope
    fun provideWeatherForecastDataSource(openWeatherMapApi: OpenWeatherMapApi): WeatherForecastRepositoryImpl.DataSource =
        OpenWeatherMapDataSource(openWeatherMapApi)
}