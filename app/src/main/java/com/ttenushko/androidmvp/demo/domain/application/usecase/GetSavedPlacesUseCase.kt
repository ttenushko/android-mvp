package com.ttenushko.androidmvp.demo.domain.application.usecase

import com.ttenushko.androidmvp.demo.domain.usecase.SingleResultUseCase
import com.ttenushko.androidmvp.demo.domain.weather.model.Place

interface GetSavedPlacesUseCase :
    SingleResultUseCase<GetSavedPlacesUseCase.Param, GetSavedPlacesUseCase.Result> {

    class Param
    data class Result(val places: List<Place>)
}