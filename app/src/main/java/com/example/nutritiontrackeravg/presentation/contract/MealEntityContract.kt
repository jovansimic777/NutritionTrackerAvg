package com.example.nutritiontrackeravg.presentation.contract

import androidx.lifecycle.LiveData
import com.example.nutritiontrackeravg.data.models.entities.MealEntity
import com.example.nutritiontrackeravg.presentation.view.states.MealsState
import com.example.nutritiontrackeravg.presentation.view.states.SingleMealState

interface MealEntityContract {

    interface ViewModel {
        val meal: LiveData<SingleMealState>
        val allMeals: LiveData<MealsState>

        fun getMealById(id: String)
        fun getAllMeals()
        fun deleteMealFromDB(id: String)
        fun editMealInDB(meal: MealEntity)
    }
}