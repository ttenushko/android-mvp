package com.ttenushko.androidmvp.demo.domain.application.usecase

import com.ttenushko.androidmvp.demo.domain.application.repository.ApplicationSettings
import com.ttenushko.androidmvp.demo.domain.usecase.CoroutineSingleResultUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetSavedPlacesUseCaseImpl(private val applicationSettings: ApplicationSettings) :
    CoroutineSingleResultUseCase<GetSavedPlacesUseCase.Param, GetSavedPlacesUseCase.Result>(),
    GetSavedPlacesUseCase {

    override suspend fun run(param: GetSavedPlacesUseCase.Param): GetSavedPlacesUseCase.Result =
        withContext(Dispatchers.IO) {
            GetSavedPlacesUseCase.Result(applicationSettings.getPlaces())
        }
}