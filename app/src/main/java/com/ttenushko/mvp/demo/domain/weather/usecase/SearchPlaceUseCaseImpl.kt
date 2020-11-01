package com.ttenushko.mvp.demo.domain.weather.usecase

import com.ttenushko.mvp.demo.domain.usecase.RxMultiResultUseCase
import com.ttenushko.mvp.demo.domain.utils.asRxObservable
import com.ttenushko.mvp.demo.domain.weather.repository.WeatherForecastRepository
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class SearchPlaceUseCaseImpl(
    private val weatherForecastRepository: WeatherForecastRepository,
    private val debounceMs: Long
) :
    RxMultiResultUseCase<SearchPlaceUseCase.Param, SearchPlaceUseCase.Result>(),
    SearchPlaceUseCase {

    override fun createObservable(param: SearchPlaceUseCase.Param): Observable<SearchPlaceUseCase.Result> =
        Observable.merge(
            Observable.just(param.search.get()),
            param.search.asRxObservable()
        ).observeOn(Schedulers.io())
            .debounce(debounceMs, TimeUnit.MILLISECONDS)
            .switchMap { search ->
                Observable.defer {
                    try {
                        val places =
                            if (search.isNotBlank()) weatherForecastRepository.findPlace(search)
                            else listOf()
                        Observable.just(SearchPlaceUseCase.Result.Success(search, places))
                    } catch (error: Throwable) {
                        Observable.just(SearchPlaceUseCase.Result.Failure(search, error))
                    }
                }.subscribeOn(Schedulers.io())
            }
}