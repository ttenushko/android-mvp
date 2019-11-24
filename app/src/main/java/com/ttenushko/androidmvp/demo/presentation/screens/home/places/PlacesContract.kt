package com.ttenushko.androidmvp.demo.presentation.screens.home.places

import com.ttenushko.androidmvp.demo.domain.weather.model.Place
import com.ttenushko.androidmvp.demo.presentation.base.mvp.BaseMvpContract

interface PlacesContract {
    interface View : BaseMvpContract.View {
        fun setPlaces(places: List<Place>?, error: Throwable?)
        fun setLoading(isLoading: Boolean)
    }

    interface Presenter : BaseMvpContract.Presenter<View, Presenter.State> {
        fun addPlaceButtonClicked()
        fun placeClicked(place: Place)

        object State : BaseMvpContract.Presenter.State
    }
}