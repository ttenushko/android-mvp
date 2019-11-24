package com.ttenushko.androidmvp.demo.domain.utils

interface Cancellable {
    val isCancelled: Boolean
    fun cancel()
}