package com.ttenushko.androidmvp

interface MvpPresenter<V : MvpView, S : MvpPresenter.State> : Runnable, Releasable {
    fun attachView(view: V)
    fun detachView(view: V)
    fun saveState(): S

    interface State
}