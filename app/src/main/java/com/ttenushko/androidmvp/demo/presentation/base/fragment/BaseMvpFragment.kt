package com.ttenushko.androidmvp.demo.presentation.base.fragment

import android.os.Bundle
import com.ttenushko.androidmvp.MvpPresenter
import com.ttenushko.androidmvp.MvpView
import com.ttenushko.androidmvp.MvpViewModel

abstract class BaseMvpFragment<V : MvpView, S : MvpPresenter.State, P : MvpPresenter<V, S>> :
    BaseFragment() {

    private lateinit var mvpViewModel: MvpViewModel<V, S, P>
    protected val presenter: P
        get() = mvpViewModel.presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mvpViewModel = getMvpViewModel().apply { run(savedInstanceState) }
    }

    override fun onStart() {
        super.onStart()
        mvpViewModel.presenter.attachView(getMvpView())
    }

    override fun onStop() {
        super.onStop()
        mvpViewModel.presenter.detachView(getMvpView())
    }

    override fun onSaveInstanceState(outState: Bundle) {
        mvpViewModel.saveState(outState)
        super.onSaveInstanceState(outState)
    }

    protected abstract fun getMvpView(): V

    protected abstract fun getMvpViewModel(): MvpViewModel<V, S, P>
}