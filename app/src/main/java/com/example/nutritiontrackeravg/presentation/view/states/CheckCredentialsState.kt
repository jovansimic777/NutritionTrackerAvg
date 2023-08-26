package com.example.nutritiontrackeravg.presentation.view.states

sealed class CheckCredentialsState {
    object Loading: CheckCredentialsState()
    object Success: CheckCredentialsState()
    data class Error(val message: String): CheckCredentialsState()
}