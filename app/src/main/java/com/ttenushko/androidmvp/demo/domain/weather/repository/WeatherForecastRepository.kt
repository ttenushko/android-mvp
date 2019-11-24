package com.ttenushko.androidmvp.demo.domain.weather.repository

import com.ttenushko.androidmvp.demo.domain.weather.model.Place
import com.ttenushko.androidmvp.demo.domain.weather.model.Weather

interface WeatherForecastRepository {
    fun findPlace(query: String): List<Place>
    fun getWeather(placeId: Long): Weather
}