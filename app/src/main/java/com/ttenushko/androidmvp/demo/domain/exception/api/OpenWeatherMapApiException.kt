package com.ttenushko.androidmvp.demo.domain.exception.api

import com.ttenushko.androidmvp.demo.domain.exception.AppException


class OpenWeatherMapApiException(
    val code: Int,
    val apiMessage: String? = null,
    exception: Exception? = null
) : AppException(apiMessage, exception)