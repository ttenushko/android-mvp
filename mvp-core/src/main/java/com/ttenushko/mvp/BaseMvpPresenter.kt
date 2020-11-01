package com.ttenushko.mvp

import java.util.concurrent.atomic.AtomicInteger

abstract class BaseMvpPresenter<V : MvpView, S : MvpPresenter.State> : MvpPresenter<V, S> {

    companion object {
        private const val STATUS_IDLE = 0
        private const val STATUS_STARTED = 1
        private const val STATUS_STOPPED = 2
    }

    private val status = AtomicInteger(STATUS_IDLE)
    private var _view: V? = null
    protected val viewOrNull: V?
        get() {
            checkStarted()
            return _view
        }
    protected val view: V
        get() {
            checkStarted()
            return requireNotNull(this._view) { "View is not attached" }
        }
    protected val isViewAttached: Boolean
        get() {
            checkStarted()
            return (null != _view)
        }

    override fun start(savedState: S?) {
        when (status.getAndSet(STATUS_STARTED)) {
            STATUS_IDLE -> {
                onStart(savedState)
            }
            STATUS_STARTED -> {
                // do nothing
            }
            STATUS_STOPPED -> {
                throw IllegalStateException("This instance is stopped.")
            }
        }
    }

    override fun stop() {
        when (status.getAndSet(STATUS_STOPPED)) {
            STATUS_IDLE -> {
                throw IllegalStateException("This instance is not started.")
            }
            STATUS_STARTED -> {
                onStop()
            }
            STATUS_STOPPED -> {
                // do nothing
            }
        }
    }

    override fun attachView(view: V) {
        checkStarted()
        require(null == this._view) { "View is already attached" }
        this._view = view
        onViewAttached()
    }

    override fun detachView(view: V) {
        checkStarted()
        require(this._view == view) { "Incorrect View is trying to be detached" }
        onViewDetached()
        this._view = null
    }

    abstract fun onStart(savedState: S?)
    abstract fun onStop()
    abstract fun onViewAttached()

    protected open fun onViewDetached() {

    }

    protected fun checkStarted() {
        when (status.get()) {
            STATUS_IDLE -> throw IllegalStateException("This instance is not started.")
            STATUS_STOPPED -> throw java.lang.IllegalStateException("This instance is stopped")
        }
    }
}