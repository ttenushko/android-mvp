package com.ttenushko.androidmvp.demo.presentation.screens.home.placedetails

import com.ttenushko.androidmvp.demo.domain.weather.model.Weather
import com.ttenushko.androidmvp.demo.presentation.base.mvp.BaseMvpContract

interface PlaceDetailsContract {
    interface View : BaseMvpContract.View {
        fun setWeather(weather: Weather?, error: Throwable?)
        fun showDeleteConfirmation()
        fun setLoading(isLoading: Boolean)
        fun setDeleting(isDeleting: Boolean)
        fun enableDelete(enable: Boolean)
    }

    interface Presenter : BaseMvpContract.Presenter<View, Presenter.State> {
        fun deleteClicked()
        fun deleteConfirmed()
        fun refreshClicked()

        object State : BaseMvpContract.Presenter.State
    }
}