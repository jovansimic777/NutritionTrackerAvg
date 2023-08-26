package com.example.nutritiontrackeravg.presentation.contract

import androidx.lifecycle.LiveData
import com.example.nutritiontrackeravg.data.models.MealDto
import com.example.nutritiontrackeravg.util.Weekday

interface PlanOverViewContract {
    interface ViewModel {

        val mondayState: LiveData<List<MealDto>>
        val tuesdayState: LiveData<List<MealDto>>
        val fridayState: LiveData<List<MealDto>>

        fun addMealToDay(meal: MealDto, day: Weekday)

        fun removeMealFromDay(mealId: String, day: Weekday)

    }
}