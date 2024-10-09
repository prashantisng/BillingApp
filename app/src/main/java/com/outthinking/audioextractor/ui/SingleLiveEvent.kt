package com.outthinking.audioextractor.ui

import androidx.annotation.MainThread
import androidx.lifecycle.MutableLiveData
import java.util.concurrent.atomic.AtomicBoolean

class SingleLiveEvent <T> :MutableLiveData<T>() {

    private val pending = AtomicBoolean(false)

    @MainThread
    override fun setValue(t: T?) {
        pending.set(true)
        super.setValue(t)
    }

    /**
     * Used for cases where T is Void, to make calls cleaner.
     */
    @MainThread
    fun call() {
        value = null
    }

    companion object {
        private const val TAG = "SingleLiveEvent"
    }

}