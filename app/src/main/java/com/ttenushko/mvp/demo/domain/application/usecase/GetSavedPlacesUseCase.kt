package com.ttenushko.mvp.demo.domain.application.usecase

import com.ttenushko.mvp.demo.domain.usecase.SingleResultUseCase
import com.ttenushko.mvp.demo.domain.weather.model.Place

interface GetSavedPlacesUseCase :
    SingleResultUseCase<GetSavedPlacesUseCase.Param, GetSavedPlacesUseCase.Result> {

    class Param
    data class Result(val places: List<Place>)
}