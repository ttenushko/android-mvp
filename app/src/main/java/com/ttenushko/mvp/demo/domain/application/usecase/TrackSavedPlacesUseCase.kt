package com.ttenushko.mvp.demo.domain.application.usecase

import com.ttenushko.mvp.demo.domain.usecase.MultiResultUseCase
import com.ttenushko.mvp.demo.domain.weather.model.Place

interface TrackSavedPlacesUseCase :
    MultiResultUseCase<TrackSavedPlacesUseCase.Param, TrackSavedPlacesUseCase.Result> {

    class Param
    data class Result(val places: List<Place>)
}