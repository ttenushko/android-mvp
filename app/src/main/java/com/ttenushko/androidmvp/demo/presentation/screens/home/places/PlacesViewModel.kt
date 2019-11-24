package com.ttenushko.androidmvp.demo.presentation.screens.home.places

import android.os.Bundle
import com.ttenushko.androidmvp.MvpViewModel
import com.ttenushko.androidmvp.demo.domain.application.usecase.TrackSavedPlacesUseCase
import com.ttenushko.androidmvp.demo.presentation.screens.home.HomeRouter

internal class PlacesViewModel(
    private val trackSavedPlacesUseCase: TrackSavedPlacesUseCase,
    private val router: HomeRouter
) : MvpViewModel<PlacesContract.View, PlacesContract.Presenter.State, PlacesContract.Presenter>() {

    override fun onCreatePresenter(): PlacesContract.Presenter =
        PlacesPresenter(trackSavedPlacesUseCase, router)

    override fun onSaveState(state: PlacesContract.Presenter.State?, outState: Bundle) {
        // do nothing
    }

    override fun onRestoreState(savedState: Bundle): PlacesContract.Presenter.State? =
        null
}