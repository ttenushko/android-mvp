package com.ttenushko.androidmvp.demo.platform.android.weather.datasource

import com.ttenushko.androidmvp.demo.data.weather.repository.WeatherForecastRepositoryImpl
import com.ttenushko.androidmvp.demo.domain.weather.model.Place
import com.ttenushko.androidmvp.demo.domain.weather.model.Weather
import com.ttenushko.androidmvp.demo.platform.android.weather.datasource.rest.OpenWeatherMapApi
import com.ttenushko.androidmvp.demo.platform.android.weather.datasource.rest.model.process
import com.ttenushko.androidmvp.demo.platform.android.weather.datasource.rest.model.toDomainModel

class OpenWeatherMapDataSource(private val openWeatherMapApi: OpenWeatherMapApi) :
    WeatherForecastRepositoryImpl.DataSource {

    override fun findPlaces(query: String): List<Place> =
        openWeatherMapApi.find(query).process().let { response ->
            response.items.map { it.toDomainModel() }
        }

    override fun getWeatherById(placeId: Long): Weather =
        openWeatherMapApi.getWeather(placeId).process().toDomainModel()
}