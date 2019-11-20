package com.ttenushko.androidmvp

import java.util.concurrent.atomic.AtomicInteger

abstract class BaseMvpPresenter<V : MvpView, S : MvpPresenter.State> : MvpPresenter<V, S> {

    companion object {
        private const val STATUS_IDLE = 0
        private const val STATUS_RUNNING = 1
        private const val STATUS_RELEASED = 2
    }

    private val status = AtomicInteger(STATUS_IDLE)
    private var _view: V? = null
    protected val view: V
        get() {
            checkNotReleased()
            return requireNotNull(this._view) { "View is not attached" }
        }
    protected val isViewAttached: Boolean
        get() {
            checkNotReleased()
            return (null != _view)
        }

    override fun attachView(view: V) {
        checkNotReleased()
        require(null == this._view) { "View is already attached" }
        this._view = view
        onViewAttached()
    }

    override fun detachView(view: V) {
        checkNotReleased()
        require(this._view == view) { "Incorrect View is trying to be detached" }
        this._view = null
        onViewDetached()
    }

    override fun run() {
        checkNotReleased()
        if (status.compareAndSet(STATUS_IDLE, STATUS_RUNNING)) {
            onStart()
        }
    }

    override fun release() {
        if (STATUS_RUNNING == status.getAndSet(STATUS_RELEASED)) {
            onStop()
        }
    }

    abstract fun onStart()
    abstract fun onStop()
    abstract fun onViewAttached()

    protected open fun onViewDetached() {

    }

    protected fun checkNotReleased() {
        check(STATUS_RELEASED != status.get()) { "This instance is released." }
    }
}