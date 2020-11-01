package com.ttenushko.mvp.demo.domain.utils

interface Cancellable {
    val isCancelled: Boolean
    fun cancel()
}