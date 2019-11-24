package com.ttenushko.androidmvp.demo.presentation.screens.home.placedetails

import com.ttenushko.androidmvp.BaseMvpPresenter
import com.ttenushko.androidmvp.demo.domain.application.usecase.DeletePlaceUseCase
import com.ttenushko.androidmvp.demo.domain.weather.model.Weather
import com.ttenushko.androidmvp.demo.domain.weather.usecase.GetCurrentWeatherConditionsUseCase
import com.ttenushko.androidmvp.demo.presentation.screens.home.HomeRouter
import com.ttenushko.androidmvp.demo.presentation.utils.task.SingleResultTask
import com.ttenushko.androidmvp.demo.presentation.utils.task.asSingleResultJob
import kotlinx.coroutines.Dispatchers

class PlaceDetailsPresenter(
    private val placeId: Long,
    private val getCurrentWeatherConditionsUseCase: GetCurrentWeatherConditionsUseCase,
    private val deletePlaceUseCase: DeletePlaceUseCase,
    private val router: HomeRouter
) : BaseMvpPresenter<PlaceDetailsContract.View, PlaceDetailsContract.Presenter.State>(),
    PlaceDetailsContract.Presenter {

    companion object {
        private const val UPDATE_WEATHER = 0x0001
        private const val UPDATE_LOADING = 0x0002
        private const val UPDATE_DELETE = 0x0004
        private const val UPDATE_ALL = (UPDATE_WEATHER or UPDATE_LOADING or UPDATE_DELETE)
    }

    private var weather: Weather? = null
    private var error: Throwable? = null
    private val taskGetCurrentWeather = createGetCurrentWeatherTask()
    private val taskDeletePlace = createDeletePlaceTask()

    override fun onStart(savedState: PlaceDetailsContract.Presenter.State?) {
        if (null == weather && null == error) {
            taskGetCurrentWeather.start(GetCurrentWeatherConditionsUseCase.Param(placeId), Unit)
        }
        updateView(UPDATE_ALL)
    }

    override fun onStop() {
        taskGetCurrentWeather.stop()
        taskGetCurrentWeather.stop()
    }

    override fun onViewAttached() {
        updateView(UPDATE_ALL)
    }

    override fun saveState(): PlaceDetailsContract.Presenter.State? =
        null

    override fun deleteClicked() {
        view.showDeleteConfirmation()
    }

    override fun deleteConfirmed() {
        if (!taskDeletePlace.isRunning) {
            taskDeletePlace.start(DeletePlaceUseCase.Param(placeId), Unit)
            updateView(UPDATE_LOADING)
        }
    }

    override fun refreshClicked() {
        if (!taskGetCurrentWeather.isRunning) {
            taskGetCurrentWeather.start(GetCurrentWeatherConditionsUseCase.Param(placeId), Unit)
            updateView(UPDATE_LOADING)
        }
    }

    private fun updateView(flags: Int) {
        if (isViewAttached) {
            if (0 != (flags and UPDATE_WEATHER)) {
                view.setWeather(weather, error)
            }
            if (0 != (flags and UPDATE_LOADING)) {
                view.setLoading(taskGetCurrentWeather.isRunning)
                view.setDeleting(taskDeletePlace.isRunning)
            }
            if (0 != (flags and UPDATE_DELETE)) {
                view.enableDelete(null != weather)
            }
        }
    }

    private fun handleCurrentWeatherLoaded(
        result: GetCurrentWeatherConditionsUseCase.Result?,
        error: Throwable?
    ) {
        if (null != result) {
            this.weather = result.weather
            this.error = null
        } else if (null != error) {
            this.weather = null
            this.error = error
        }
        updateView(UPDATE_ALL)
    }

    private fun handlePlaceDeleted(error: Throwable?) {
        if (null == error) {
            router.navigateTo(HomeRouter.Destination.GoBack)
        } else {
            view.showErrorPopup(error)
        }
        updateView(UPDATE_LOADING)
    }

    private fun createGetCurrentWeatherTask() =
        SingleResultTask<GetCurrentWeatherConditionsUseCase.Param, GetCurrentWeatherConditionsUseCase.Result, Unit>(
            "getCurrentWeatherConditions",
            Dispatchers.Main,
            { param, tag ->
                getCurrentWeatherConditionsUseCase.asSingleResultJob(param, tag)
            },
            { result, _ ->
                handleCurrentWeatherLoaded(result, null)
            },
            { error, _ ->
                handleCurrentWeatherLoaded(null, error)
            }
        )

    private fun createDeletePlaceTask() =
        SingleResultTask<DeletePlaceUseCase.Param, DeletePlaceUseCase.Result, Unit>(
            "deletePlace",
            Dispatchers.Main,
            { param, tag ->
                deletePlaceUseCase.asSingleResultJob(param, tag)
            },
            { _, _ ->
                handlePlaceDeleted(null)
            },
            { error, _ ->
                handlePlaceDeleted(error)
            }
        )
}