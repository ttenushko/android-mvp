package com.ttenushko.mvp

interface MvpPresenter<V : MvpView, S : MvpPresenter.State> {
    fun start(savedState: S?)
    fun stop()
    fun attachView(view: V)
    fun detachView(view: V)
    fun saveState(): S?

    interface State
}