package com.ttenushko.androidmvp

interface Releasable {
    @Throws(Exception::class)
    fun release()
}