package com.ttenushko.androidmvp.demo.domain.usecase

import com.ttenushko.androidmvp.demo.domain.utils.Cancellable

interface SingleResultUseCase<P : Any, R : Any> {

    fun execute(param: P, callback: Callback<R>): Cancellable

    interface Callback<R : Any> {
        fun onComplete(result: R)
        fun onError(error: Throwable)
    }
}