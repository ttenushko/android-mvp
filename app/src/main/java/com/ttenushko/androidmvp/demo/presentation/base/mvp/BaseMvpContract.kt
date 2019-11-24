package com.ttenushko.androidmvp.demo.presentation.base.mvp

import com.ttenushko.androidmvp.MvpPresenter
import com.ttenushko.androidmvp.MvpView

interface BaseMvpContract {

    interface View : MvpView {
        fun showErrorPopup(error: Throwable)
    }

    interface Presenter<V : View, S : Presenter.State> : MvpPresenter<V, S> {
        interface State : MvpPresenter.State
    }
}