package com.ttenushko.androidmvp.demo.domain.weather.usecase

import com.ttenushko.androidmvp.demo.domain.usecase.SingleResultUseCase
import com.ttenushko.androidmvp.demo.domain.weather.model.Weather

interface GetCurrentWeatherConditionsUseCase :
    SingleResultUseCase<GetCurrentWeatherConditionsUseCase.Param, GetCurrentWeatherConditionsUseCase.Result> {

    data class Param(
        val placeId: Long
    )

    data class Result(val weather: Weather)
}