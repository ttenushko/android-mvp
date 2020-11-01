package com.ttenushko.mvp.demo.domain.exception.api

import com.ttenushko.mvp.demo.domain.exception.AppException


class OpenWeatherMapApiException(
    val code: Int,
    val apiMessage: String? = null,
    exception: Exception? = null
) : AppException(apiMessage, exception)