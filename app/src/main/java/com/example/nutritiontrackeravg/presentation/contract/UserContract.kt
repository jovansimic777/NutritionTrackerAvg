package com.example.nutritiontrackeravg.presentation.contract

import androidx.lifecycle.LiveData
import com.example.nutritiontrackeravg.data.models.User
import com.example.nutritiontrackeravg.presentation.view.states.AddUserState
import com.example.nutritiontrackeravg.presentation.view.states.CheckCredentialsState
import com.example.nutritiontrackeravg.presentation.view.states.UsersState

class UserContract {
    interface ViewModel {
        val usersState: LiveData<UsersState>
        val addDone: LiveData<AddUserState>
        val checkCredentialsState: LiveData<CheckCredentialsState>
        fun getAllUsers()
        fun checkCredentials(email: String, password: String)
        fun addUser(user: User)
    }
}