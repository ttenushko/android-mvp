package com.ttenushko.androidmvp.demo.domain.application.repository

import com.ttenushko.androidmvp.demo.domain.weather.model.Place

interface ApplicationSettings {
    fun setPlaces(places: List<Place>)
    fun getPlaces(): List<Place>
    fun addPlacesUpdatedListener(listener: PlacesUpdatedListener)
    fun removePlacesUpdatedListener(listener: PlacesUpdatedListener)

    interface PlacesUpdatedListener {
        fun onPlacesUpdated(places: List<Place>)
    }
}