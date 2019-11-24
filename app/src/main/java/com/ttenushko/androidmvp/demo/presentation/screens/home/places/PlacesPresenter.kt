package com.ttenushko.androidmvp.demo.presentation.screens.home.places

import com.ttenushko.androidmvp.BaseMvpPresenter
import com.ttenushko.androidmvp.demo.domain.application.usecase.TrackSavedPlacesUseCase
import com.ttenushko.androidmvp.demo.domain.weather.model.Place
import com.ttenushko.androidmvp.demo.presentation.screens.home.HomeRouter
import com.ttenushko.androidmvp.demo.presentation.utils.task.MultiResultTask
import com.ttenushko.androidmvp.demo.presentation.utils.task.asMultiResultJob
import kotlinx.coroutines.Dispatchers

class PlacesPresenter(
    private val trackSavedPlacesUseCase: TrackSavedPlacesUseCase,
    private val router: HomeRouter
) : BaseMvpPresenter<PlacesContract.View, PlacesContract.Presenter.State>(),
    PlacesContract.Presenter {

    companion object {
        private const val UPDATE_PLACES = 0x0001
        private const val UPDATE_LOADING = 0x0002
        private const val UPDATE_ALL = (UPDATE_PLACES or UPDATE_LOADING)
    }

    private var places: List<Place>? = null
    private var error: Throwable? = null
    private val taskTrackSavedPlaces = createTrackSavedPlacesTask()

    override fun onStart(savedState: PlacesContract.Presenter.State?) {
        taskTrackSavedPlaces.start(TrackSavedPlacesUseCase.Param(), Unit)
        updateView(UPDATE_ALL)
    }

    override fun onStop() {
        taskTrackSavedPlaces.stop()
    }

    override fun onViewAttached() {
        updateView(UPDATE_ALL)
    }

    override fun saveState(): PlacesContract.Presenter.State? =
        null

    override fun addPlaceButtonClicked() {
        router.navigateTo(HomeRouter.Destination.AddPlace(""))
    }

    override fun placeClicked(place: Place) {
        router.navigateTo(HomeRouter.Destination.PlaceDetails(placeId = place.id))
    }

    private fun updateView(flags: Int) {
        if (isViewAttached) {
            if (0 != (flags and UPDATE_PLACES)) {
                view.setPlaces(places, error)
            }
            if (0 != (flags and UPDATE_LOADING)) {
                view.setLoading(null == places && taskTrackSavedPlaces.isRunning)
            }
        }
    }

    private fun handleSavedPlacesUpdated(
        result: TrackSavedPlacesUseCase.Result?,
        error: Throwable?
    ) {
        if (null != result) {
            this.places = result.places
            this.error = null
        } else if (null != error) {
            this.places = null
            this.error = error
        }
        updateView(UPDATE_ALL)
    }

    private fun createTrackSavedPlacesTask(): MultiResultTask<TrackSavedPlacesUseCase.Param, TrackSavedPlacesUseCase.Result, Unit> =
        MultiResultTask(
            "trackSavedPlaces",
            Dispatchers.Main,
            { param, tag -> trackSavedPlacesUseCase.asMultiResultJob(param, tag) },
            { result, _ -> handleSavedPlacesUpdated(result, null) },
            { error, _ -> handleSavedPlacesUpdated(null, error) }
        )
}