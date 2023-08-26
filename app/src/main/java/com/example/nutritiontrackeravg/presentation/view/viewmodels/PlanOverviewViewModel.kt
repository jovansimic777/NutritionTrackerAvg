package com.example.nutritiontrackeravg.presentation.view.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.nutritiontrackeravg.data.models.MealDto
import com.example.nutritiontrackeravg.presentation.contract.PlanOverViewContract
import com.example.nutritiontrackeravg.util.Weekday

class PlanOverviewViewModel(
): ViewModel(), PlanOverViewContract.ViewModel {
    override val mondayState: MutableLiveData<List<MealDto>> = MutableLiveData(listOf())
    override val tuesdayState: MutableLiveData<List<MealDto>> = MutableLiveData(listOf())
    override val fridayState: MutableLiveData<List<MealDto>> = MutableLiveData(listOf())

    override fun addMealToDay(meal: MealDto, day: Weekday) {
        when(day) {
            Weekday.MONDAY -> {
                val currentList = mondayState.value
                val newList = currentList!!.toMutableList()
                newList.add(meal)
                mondayState.value = newList
            }
            Weekday.TUESDAY -> {
                val currentList = tuesdayState.value
                val newList = currentList!!.toMutableList()
                newList.add(meal)
                tuesdayState.value = newList
            }
            Weekday.FRIDAY -> {
                val currentList = fridayState.value
                val newList = currentList!!.toMutableList()
                newList.add(meal)
                fridayState.value = newList
            }

            else -> {}
        }
    }

    override fun removeMealFromDay(mealId: String, day: Weekday) {
        when(day) {
            Weekday.MONDAY -> {
                val currentList = mondayState.value
                val newList = currentList!!.toMutableList()
                newList.removeIf { it.id == mealId }
                mondayState.value = newList
            }
            Weekday.TUESDAY -> {
                val currentList = tuesdayState.value
                val newList = currentList!!.toMutableList()
                newList.removeIf { it.id == mealId }
                tuesdayState.value = newList
            }
            Weekday.FRIDAY -> {
                val currentList = fridayState.value
                val newList = currentList!!.toMutableList()
                newList.removeIf { it.id == mealId }
                fridayState.value = newList
            }

            else -> {}
        }
    }


}