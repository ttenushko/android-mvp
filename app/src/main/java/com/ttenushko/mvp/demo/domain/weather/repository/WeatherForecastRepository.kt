package com.ttenushko.mvp.demo.domain.weather.repository

import com.ttenushko.mvp.demo.domain.weather.model.Place
import com.ttenushko.mvp.demo.domain.weather.model.Weather

interface WeatherForecastRepository {
    fun findPlace(query: String): List<Place>
    fun getWeather(placeId: Long): Weather
}