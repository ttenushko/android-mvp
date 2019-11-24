package com.ttenushko.androidmvp.demo.domain.application.usecase

import com.ttenushko.androidmvp.demo.domain.usecase.MultiResultUseCase
import com.ttenushko.androidmvp.demo.domain.weather.model.Place

interface TrackSavedPlacesUseCase :
    MultiResultUseCase<TrackSavedPlacesUseCase.Param, TrackSavedPlacesUseCase.Result> {

    class Param
    data class Result(val places: List<Place>)
}