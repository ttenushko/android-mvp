package com.ttenushko.mvp.demo.presentation.screens.home.placedetails

import android.os.Bundle
import com.ttenushko.mvp.MvpViewModel
import com.ttenushko.mvp.demo.domain.application.usecase.DeletePlaceUseCase
import com.ttenushko.mvp.demo.domain.weather.usecase.GetCurrentWeatherConditionsUseCase
import com.ttenushko.mvp.demo.presentation.screens.home.HomeRouter

internal class PlaceDetailsViewModel(
    private val placeId: Long,
    private val getCurrentWeatherConditionsUseCase: GetCurrentWeatherConditionsUseCase,
    private val deletePlaceUseCase: DeletePlaceUseCase,
    private val router: HomeRouter
) : MvpViewModel<PlaceDetailsContract.View, PlaceDetailsContract.Presenter.State, PlaceDetailsContract.Presenter>() {

    override fun onCreatePresenter(): PlaceDetailsContract.Presenter =
        PlaceDetailsPresenter(
            placeId,
            getCurrentWeatherConditionsUseCase,
            deletePlaceUseCase,
            router
        )

    override fun onSaveState(state: PlaceDetailsContract.Presenter.State?, outState: Bundle) {
        // do nothing
    }

    override fun onRestoreState(savedState: Bundle): PlaceDetailsContract.Presenter.State? =
        null
}