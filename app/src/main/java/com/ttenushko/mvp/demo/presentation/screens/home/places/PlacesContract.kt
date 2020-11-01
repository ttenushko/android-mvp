package com.ttenushko.mvp.demo.presentation.screens.home.places

import com.ttenushko.mvp.demo.domain.weather.model.Place
import com.ttenushko.mvp.demo.presentation.base.mvp.BaseMvpContract

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