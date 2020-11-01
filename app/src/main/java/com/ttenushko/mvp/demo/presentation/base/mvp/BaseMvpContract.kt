package com.ttenushko.mvp.demo.presentation.base.mvp

import com.ttenushko.mvp.MvpPresenter
import com.ttenushko.mvp.MvpView

interface BaseMvpContract {

    interface View : MvpView {
        fun showErrorPopup(error: Throwable)
    }

    interface Presenter<V : View, S : Presenter.State> : MvpPresenter<V, S> {
        interface State : MvpPresenter.State
    }
}