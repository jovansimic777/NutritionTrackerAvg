package com.example.nutritiontrackeravg.presentation.contract

import androidx.lifecycle.LiveData
import com.example.nutritiontrackeravg.presentation.view.states.MealsState
import com.example.nutritiontrackeravg.util.MealType

interface MealPlanContract {
    interface ViewModel {

        val queryString: LiveData<String>
//        val queryChar: LiveData<Char>
        val mealTypeSelected: LiveData<MealType>
        val allMeals: LiveData<MealsState>
        val allMealsFiltered: LiveData<MealsState>
        val localMeals: LiveData<MealsState>
        val localMealsFiltered: LiveData<MealsState>

        fun updateQuery(query: String)
        fun updateMealType(mealType: MealType)
    }
}