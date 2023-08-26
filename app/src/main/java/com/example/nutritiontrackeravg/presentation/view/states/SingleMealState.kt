package com.example.nutritiontrackeravg.presentation.view.states

import com.example.nutritiontrackeravg.data.models.MealDto

sealed class SingleMealState {
    object Loading: SingleMealState()
    data class Success(val meal: MealDto): SingleMealState()
    data class Error(val message: String): SingleMealState()
}