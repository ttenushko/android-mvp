package com.ttenushko.mvp.demo.domain.weather.model

data class Weather(
    val place: Place,
    val conditions: WeatherConditions,
    val descriptions: List<WeatherDescription>
)