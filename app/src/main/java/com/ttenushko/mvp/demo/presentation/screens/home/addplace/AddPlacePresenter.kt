package com.ttenushko.mvp.demo.presentation.screens.home.addplace

import com.ttenushko.mvp.BaseMvpPresenter
import com.ttenushko.mvp.demo.domain.application.usecase.SavePlaceUseCase
import com.ttenushko.mvp.demo.domain.utils.ObservableValue
import com.ttenushko.mvp.demo.domain.weather.model.Place
import com.ttenushko.mvp.demo.domain.weather.usecase.SearchPlaceUseCase
import com.ttenushko.mvp.demo.presentation.screens.home.HomeRouter
import com.ttenushko.mvp.demo.presentation.utils.task.MultiResultTask
import com.ttenushko.mvp.demo.presentation.utils.task.SingleResultTask
import com.ttenushko.mvp.demo.presentation.utils.task.asMultiResultJob
import com.ttenushko.mvp.demo.presentation.utils.task.asSingleResultJob
import kotlinx.coroutines.Dispatchers

class AddPlacePresenter(
    private val search: String,
    private val searchPlaceUseCase: SearchPlaceUseCase,
    private val savePlaceUseCase: SavePlaceUseCase,
    private val router: HomeRouter
) : BaseMvpPresenter<AddPlaceContract.View, AddPlaceContract.Presenter.State>(),
    AddPlaceContract.Presenter {

    companion object {
        private const val UPDATE_SEARCH_VALUE = 0x0001
        private const val UPDATE_SEARCH_PROMPT = 0x0002
        private const val UPDATE_SEARCHING = 0x0004
        private const val UPDATE_PLACES = 0x0008
        private const val UPDATE_ALL =
            (UPDATE_SEARCH_VALUE or UPDATE_SEARCH_PROMPT or UPDATE_SEARCHING or UPDATE_PLACES)
        private val EMPTY_PLACES = listOf<Place>()
    }

    private val searchValue = ObservableValue(search)
    private val taskSearchPlace = createSearchPlaceTask()
    private val taskSavePlace = createSavePlaceTask()
    private var searchResult: SearchPlaceUseCase.Result? = null

    override fun onStart(savedState: AddPlaceContract.Presenter.State?) {
        searchValue.set(savedState?.search ?: search)
        taskSearchPlace.start(SearchPlaceUseCase.Param(searchValue), Unit)
        updateView(UPDATE_ALL)
    }

    override fun onStop() {
        taskSearchPlace.stop()
        taskSavePlace.stop()
    }

    override fun onViewAttached() {
        updateView(UPDATE_ALL)
    }

    override fun saveState(): AddPlaceContract.Presenter.State? =
        AddPlaceContract.Presenter.State(searchValue.get())

    override fun searchChanged(search: String) {
        searchValue.set(search)
        updateView(UPDATE_SEARCHING or UPDATE_SEARCH_PROMPT)
    }

    override fun placeClicked(place: Place) {
        if (!taskSavePlace.isRunning) {
            taskSavePlace.start(SavePlaceUseCase.Param(place), Unit)
        }
    }

    private fun updateView(flags: Int) {
        if (isViewAttached) {
            val searchResult = this.searchResult
            val isSearching = null != searchResult && searchResult.search != searchValue.get()
            if (0 != (flags and UPDATE_SEARCH_VALUE)) {
                view.setSearch(searchValue.get())
            }
            if (0 != (flags and UPDATE_SEARCHING)) {
                view.setSearching(isSearching)
            }
            if (0 != (flags and UPDATE_SEARCH_PROMPT)) {
                view.showSearchPrompt(
                    when {
                        isSearching -> {
                            AddPlaceContract.SearchPrompt.NO
                        }
                        searchValue.get().isEmpty() -> {
                            AddPlaceContract.SearchPrompt.BLANK
                        }
                        searchResult is SearchPlaceUseCase.Result.Success && searchResult.places.isEmpty() -> {
                            AddPlaceContract.SearchPrompt.NO_RESULTS
                        }
                        searchResult is SearchPlaceUseCase.Result.Failure -> {
                            AddPlaceContract.SearchPrompt.ERROR
                        }
                        else -> AddPlaceContract.SearchPrompt.NO
                    }
                )
            }
            if (0 != (flags and UPDATE_PLACES)) {
                view.setPlaces(
                    when (searchResult) {
                        is SearchPlaceUseCase.Result.Success -> searchResult.places
                        else -> EMPTY_PLACES
                    }
                )
            }
        }
    }

    private fun handleSearchResult(result: SearchPlaceUseCase.Result?, error: Throwable?) {
        if (null != result) {
            if (result.search == searchValue.get()) {
                searchResult = result
            }
        } else if (null != error) {
            searchResult = SearchPlaceUseCase.Result.Failure(searchValue.get(), error)
        }
        updateView(UPDATE_SEARCH_PROMPT or UPDATE_SEARCHING or UPDATE_PLACES)
    }

    private fun handlePlaceSaved(result: SavePlaceUseCase.Result?, error: Throwable?) {
        if (null != result) {
            router.navigateTo(HomeRouter.Destination.GoBack)
        } else if (null != error) {
            view.showErrorPopup(error)
        }
    }

    private fun createSearchPlaceTask(): MultiResultTask<SearchPlaceUseCase.Param, SearchPlaceUseCase.Result, Unit> =
        MultiResultTask(
            "searchPlace",
            Dispatchers.Main,
            { param, tag -> searchPlaceUseCase.asMultiResultJob(param, tag) },
            { result, _ -> handleSearchResult(result, null) },
            { error, _ -> handleSearchResult(null, error) }
        )

    private fun createSavePlaceTask(): SingleResultTask<SavePlaceUseCase.Param, SavePlaceUseCase.Result, Unit> =
        SingleResultTask(
            "savePlace",
            Dispatchers.Main,
            { param, tag -> savePlaceUseCase.asSingleResultJob(param, tag) },
            { result, _ -> handlePlaceSaved(result, null) },
            { error, _ -> handlePlaceSaved(null, error) }
        )

    private val SearchPlaceUseCase.Result.search: String
        get() = when (this) {
            is SearchPlaceUseCase.Result.Success -> this.search
            is SearchPlaceUseCase.Result.Failure -> this.search
        }
}