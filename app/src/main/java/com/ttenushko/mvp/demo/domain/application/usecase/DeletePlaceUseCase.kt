package com.ttenushko.mvp.demo.domain.application.usecase

import com.ttenushko.mvp.demo.domain.usecase.SingleResultUseCase

interface DeletePlaceUseCase :
    SingleResultUseCase<DeletePlaceUseCase.Param, DeletePlaceUseCase.Result> {

    data class Param(
        val placeId: Long
    )

    data class Result(
        val isDeleted: Boolean
    )
}