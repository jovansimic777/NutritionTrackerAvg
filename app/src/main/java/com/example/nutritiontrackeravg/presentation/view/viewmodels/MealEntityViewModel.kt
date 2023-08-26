package com.example.nutritiontrackeravg.presentation.view.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.nutritiontrackeravg.data.models.entities.MealEntity
import com.example.nutritiontrackeravg.data.repositories.MealRepository
import com.example.nutritiontrackeravg.data.sources.remote.converters.MealDtoConverter
import com.example.nutritiontrackeravg.presentation.contract.MealEntityContract
import com.example.nutritiontrackeravg.presentation.view.states.MealsState
import com.example.nutritiontrackeravg.presentation.view.states.SingleMealState
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class MealEntityViewModel(
    private val mealRepository: MealRepository
): ViewModel(), MealEntityContract.ViewModel {

    override val meal: MutableLiveData<SingleMealState> = MutableLiveData()
    override val allMeals: MutableLiveData<MealsState> = MutableLiveData()

    private val subscriptions = CompositeDisposable()

    override fun getMealById(id: String) {
        val subscription = mealRepository
            .getMealById(id)
            .map<SingleMealState> { SingleMealState.Success(MealDtoConverter.mapMealEntityToMealDto(it)) }
            .startWith(SingleMealState.Loading)
            .onErrorReturn { SingleMealState.Error(it.message ?: "Unknown error occurred") }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { it ->
//                Timber.e("Response meals" + it.toString())
                when (it) {
                    is SingleMealState.Success -> {
                        meal.value = it
                    }
                    is SingleMealState.Loading -> {
                        meal.value = SingleMealState.Loading
                    }
                    is SingleMealState.Error -> {
                        meal.value = SingleMealState.Error(it.message)
                    }
                }
            }
        subscriptions.add(subscription)
    }

    override fun getAllMeals() {
        Timber.e("init gas gas")
        val subscription = mealRepository
            .getAllMeals()
            .map<MealsState> { MealsState.Success(MealDtoConverter.mapListMealEntityToListMealDto(it)) }
            .startWith(MealsState.Loading)
            .onErrorReturn { MealsState.Error(it.message ?: "Unknown error occurred") }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { it ->
                Timber.e("Response meals" + it.toString());
                when (it) {
                    is MealsState.Success -> {
                        Timber.e("gas gas" + it.meals.size)
//                        Toast.makeText(null, "Success" + it.meals.size, Toast.LENGTH_SHORT).show()
                        allMeals.value = MealsState.Success(it.meals)
                    }
                    is MealsState.Loading -> {
                        allMeals.value = MealsState.Loading
                    }
                    is MealsState.Error -> {
                        Timber.e("ne gas gas")
                        allMeals.value = MealsState.Error(it.message)
                    }
                }
            }
        subscriptions.add(subscription)
    }

    override fun deleteMealFromDB(id: String) {
        val subscription = mealRepository
            .deleteMealById(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    deleteMealFromList(id)
                    Timber.e("Meal deleted")
                },
                {
                    Timber.e("error deleting meal")
                }
            )
        subscriptions.add(subscription)
    }

    override fun editMealInDB(meal: MealEntity) {
        val subscription = mealRepository
            .editMeal(meal)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    Timber.e("Meal edited")
                },
                {
                    Timber.e("error editing meal")
                }
            )
        subscriptions.add(subscription)
    }

    private fun deleteMealFromList(mealId: String) {
        if(allMeals.value is MealsState.Success) {
            val list = (allMeals.value as MealsState.Success).meals.toMutableList()
            list.removeIf { it.id == mealId }
            allMeals.value = MealsState.Success(list)
        }
    }

    override fun onCleared() {
        super.onCleared()
        subscriptions.clear()
    }
}