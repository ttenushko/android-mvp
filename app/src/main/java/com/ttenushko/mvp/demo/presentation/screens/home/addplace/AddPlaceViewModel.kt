package com.ttenushko.mvp.demo.presentation.screens.home.addplace

import android.os.Bundle
import com.ttenushko.mvp.MvpViewModel
import com.ttenushko.mvp.demo.domain.application.usecase.SavePlaceUseCase
import com.ttenushko.mvp.demo.domain.weather.usecase.SearchPlaceUseCase
import com.ttenushko.mvp.demo.presentation.screens.home.HomeRouter

internal class AddPlaceViewModel(
    private val search: String,
    private val searchPlaceUseCase: SearchPlaceUseCase,
    private val savePlaceUseCase: SavePlaceUseCase,
    private val router: HomeRouter
) : MvpViewModel<AddPlaceContract.View, AddPlaceContract.Presenter.State, AddPlaceContract.Presenter>() {

    companion object {
        private const val SEARCH = "search"
    }

    override fun onCreatePresenter(): AddPlaceContract.Presenter =
        AddPlacePresenter(search, searchPlaceUseCase, savePlaceUseCase, router)

    override fun onSaveState(state: AddPlaceContract.Presenter.State?, outState: Bundle) {
        state?.let { outState.putString(SEARCH, it.search) }
    }

    override fun onRestoreState(savedState: Bundle): AddPlaceContract.Presenter.State? =
        AddPlaceContract.Presenter.State(savedState.getString(SEARCH) ?: "")
}