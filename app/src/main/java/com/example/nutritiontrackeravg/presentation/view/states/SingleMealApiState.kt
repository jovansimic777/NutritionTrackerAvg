package com.example.nutritiontrackeravg.presentation.view.states

import com.example.nutritiontrackeravg.data.models.MealSimple

sealed class SingleMealApiState {
    object Loading: SingleMealApiState()
    data class Success(val meal: MealSimple): SingleMealApiState()
    data class Error(val message: String): SingleMealApiState()
}