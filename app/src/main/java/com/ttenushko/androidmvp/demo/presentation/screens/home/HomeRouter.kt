package com.ttenushko.androidmvp.demo.presentation.screens.home

interface HomeRouter {
    fun navigateTo(destination: Destination)

    sealed class Destination {
        object GoBack : Destination()
        data class AddPlace(val search: String) : Destination()
        data class PlaceDetails(val placeId: Long) : Destination()
    }
}