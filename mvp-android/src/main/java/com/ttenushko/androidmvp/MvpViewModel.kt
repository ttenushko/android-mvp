package com.ttenushko.androidmvp

import android.os.Bundle
import androidx.lifecycle.ViewModel
import java.util.concurrent.atomic.AtomicInteger


abstract class MvpViewModel<V : MvpView, S : MvpPresenter.State, P : MvpPresenter<V, S>> :
    ViewModel() {

    companion object {
        private const val STATUS_IDLE = 0
        private const val STATUS_RUNNING = 1
        private const val STATUS_CLEARED = 2
    }

    private val status = AtomicInteger(STATUS_IDLE)
    @Volatile
    private lateinit var _presenter: P
    val presenter: P
        get() {
            checkRunning()
            return _presenter
        }

    fun run(savedState: Bundle?) {
        if (status.compareAndSet(STATUS_IDLE, STATUS_RUNNING)) {
            val state = savedState?.let { onRestoreState(it) }
            _presenter = onCreatePresenter()
            _presenter.start(state)
        }
    }

    override fun onCleared() {
        super.onCleared()
        if (STATUS_RUNNING == status.getAndSet(STATUS_CLEARED)) {
            _presenter.stop()
        }
    }

    fun saveState(outState: Bundle) {
        checkRunning()
        onSaveState(_presenter.saveState(), outState)
    }

    protected abstract fun onCreatePresenter(): P

    abstract fun onSaveState(state: S?, outState: Bundle)

    abstract fun onRestoreState(savedState: Bundle): S?

    private fun checkRunning() {
        when (status.get()) {
            STATUS_IDLE -> throw IllegalStateException("This instance is not running.")
            STATUS_CLEARED -> throw IllegalStateException("This instance is cleared.")
        }
    }
}