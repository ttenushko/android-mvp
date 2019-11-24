package com.ttenushko.androidmvp.demo.domain.weather.usecase

import com.ttenushko.androidmvp.demo.domain.usecase.MultiResultUseCase
import com.ttenushko.androidmvp.demo.domain.utils.ObservableValue
import com.ttenushko.androidmvp.demo.domain.weather.model.Place

interface SearchPlaceUseCase :
    MultiResultUseCase<SearchPlaceUseCase.Param, SearchPlaceUseCase.Result> {

    data class Param(
        val search: ObservableValue<String>
    )

    sealed class Result {
        data class Success(val search: String, val places: List<Place>) : Result()
        data class Failure(val search: String, val error: Throwable) : Result()
    }
}