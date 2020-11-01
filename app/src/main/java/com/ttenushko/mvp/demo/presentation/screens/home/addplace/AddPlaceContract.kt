package com.ttenushko.mvp.demo.presentation.screens.home.addplace

import com.ttenushko.mvp.demo.domain.weather.model.Place
import com.ttenushko.mvp.demo.presentation.base.mvp.BaseMvpContract

interface AddPlaceContract {
    interface View : BaseMvpContract.View {
        fun setSearch(search: String)
        fun setPlaces(places: List<Place>)
        fun setSearching(isSearching: Boolean)
        fun showSearchPrompt(prompt: SearchPrompt)
    }

    interface Presenter : BaseMvpContract.Presenter<View, Presenter.State> {
        fun searchChanged(search: String)
        fun placeClicked(place: Place)

        data class State(val search: String) : BaseMvpContract.Presenter.State
    }

    enum class SearchPrompt {
        NO,
        BLANK,
        NO_RESULTS,
        ERROR
    }
}