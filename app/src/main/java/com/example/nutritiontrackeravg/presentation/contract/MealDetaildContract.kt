package com.example.nutritiontrackeravg.presentation.contract

import androidx.lifecycle.LiveData
import com.example.nutritiontrackeravg.data.models.MealDetailed
import com.example.nutritiontrackeravg.data.models.entities.MealEntity

interface MealDetaildContract {

    interface ViewModel {
        val meal: LiveData<MealDetailed>

        fun fetchMealById(id:String, calValue: String)
        fun getMealById(id:String)
        fun saveMealToDB(meal: MealEntity)
    }
}