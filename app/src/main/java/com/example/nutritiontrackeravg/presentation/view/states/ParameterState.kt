package com.example.nutritiontrackeravg.presentation.view.states

import com.example.nutritiontrackeravg.data.models.Parameter


sealed class ParameterState {
    object Empty: ParameterState()

    data class Selected(val parameter: Parameter): ParameterState() {
        override fun toString(): String {
            return parameter.toString()
        }
    }
}