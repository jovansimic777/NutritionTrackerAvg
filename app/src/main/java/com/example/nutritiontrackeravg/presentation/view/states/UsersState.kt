package com.example.nutritiontrackeravg.presentation.view.states

import com.example.nutritiontrackeravg.data.models.User

sealed class UsersState {
    object Loading: UsersState()
    data class Success(val users: List<User>): UsersState()
    data class Error(val message: String): UsersState()
}