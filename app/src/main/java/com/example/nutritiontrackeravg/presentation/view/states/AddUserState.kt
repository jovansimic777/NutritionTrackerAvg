package com.example.nutritiontrackeravg.presentation.view.states

sealed class AddUserState {
    object Success: AddUserState()
    data class Error(val message: String): AddUserState()
}