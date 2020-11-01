package com.ttenushko.mvp.demo.domain.application.usecase

import com.ttenushko.mvp.demo.domain.usecase.SingleResultUseCase
import com.ttenushko.mvp.demo.domain.weather.model.Place

interface SavePlaceUseCase :
    SingleResultUseCase<SavePlaceUseCase.Param, SavePlaceUseCase.Result> {

    data class Param(
        val place: Place
    )

    data class Result(
        val place: Place,
        val isAdded: Boolean
    )
}