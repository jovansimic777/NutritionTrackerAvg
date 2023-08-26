package com.example.nutritiontrackeravg.presentation.contract

import androidx.lifecycle.LiveData
import com.example.nutritiontrackeravg.presentation.view.states.ParameterState
import com.example.nutritiontrackeravg.presentation.view.states.ParametersState
import com.example.nutritiontrackeravg.util.ParameterType

interface ParametersContract {
    interface ViewModel {

        val parametersState: LiveData<ParametersState>
        val selectedParameterState: LiveData<ParameterState>
        fun fetchAll(type: ParameterType)

    }
}