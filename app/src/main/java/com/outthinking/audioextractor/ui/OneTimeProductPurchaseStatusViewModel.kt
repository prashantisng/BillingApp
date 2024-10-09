package com.outthinking.audioextractor.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.outthinking.audioextractor.BillingApp
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class OneTimeProductPurchaseStatusViewModel (
    application: Application
): AndroidViewModel(application)  {
    private val _currentOneTimeProductPurchase =
        MutableStateFlow(CurrentOneTimeProductPurchase.NONE)
    val currentOneTimeProductPurchase = _currentOneTimeProductPurchase.asStateFlow()

    private val repository = (application as BillingApp).repository

    private val userCurrentOneTimeProduct = repository.hasOneTimeProduct

    val content = repository.otpContent

    // TODO show status in UI
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage.asStateFlow()

    init {
        viewModelScope.launch {
            userCurrentOneTimeProduct.collectLatest { hasCurrentOneTimeProduct ->
                when {
                    hasCurrentOneTimeProduct -> {
                        _currentOneTimeProductPurchase.value = CurrentOneTimeProductPurchase.OTP
                    }

                    !hasCurrentOneTimeProduct -> {
                        _currentOneTimeProductPurchase.value = CurrentOneTimeProductPurchase.NONE
                    }
                }
            }
        }
    }

    /**
     * Refresh the status of one-time product purchases.
     */
    fun manualRefresh() {

        viewModelScope.launch {
            repository.queryProducts()
        }

        viewModelScope.launch {
            val result = repository.fetchOneTimeProductPurchases()
            if (result.isFailure) {
                _errorMessage.emit(result.exceptionOrNull()?.localizedMessage)
            }
        }
    }

    enum class CurrentOneTimeProductPurchase {
        OTP,
        NONE;
    }

    companion object {
        private const val TAG = "OneTimeProductPurchaseViewModel"
    }
}