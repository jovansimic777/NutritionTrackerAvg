package com.example.nutritiontrackeravg.presentation.view.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.nutritiontrackeravg.data.models.MealDetailed
import com.example.nutritiontrackeravg.data.models.entities.MealEntity
import com.example.nutritiontrackeravg.data.repositories.MealRepository
import com.example.nutritiontrackeravg.data.sources.remote.converters.MealDetailedConverter
import com.example.nutritiontrackeravg.presentation.contract.MealDetaildContract
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class MealDetailedViewModel(
    private val mealRepository: MealRepository
): ViewModel(), MealDetaildContract.ViewModel {

    private val subscriptions = CompositeDisposable()
    override val meal: MutableLiveData<MealDetailed> = MutableLiveData()

    //For remote data source
    // Used for testing of calorie service for now
    override fun fetchMealById(id: String, calValue: String) {
//        Timber.e("GAAAS"+ meal.value)
        val subscription = mealRepository
            .fetchMealById(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    meal.value = MealDetailedConverter.convertToMealDetailed(it.meals[0])
                    meal.value?.calValue = calValue
                },
                {
                    Timber.e(it)
                }
            )

        subscriptions.add(subscription)
    }


    //For local data source
    override fun getMealById(id: String) {
        val subscription = mealRepository
            .getMealById(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    meal.value = MealDetailed(
                        it.id,
                        it.name,
                        it.category,
                        it.date.toString(),
                        it.instructions,
                        it.img,
                        it.type,
                        it.link,
                        null)
                },
                {
                    Timber.e(it)
                }
            )
        subscriptions.add(subscription)
    }

    override fun saveMealToDB(meal: MealEntity) {
        val subscription = mealRepository
            .insertMeal(meal)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    Timber.e("Meal inserted")
                },
                {
                    Timber.e("error inserting meal")
                }
            )
        subscriptions.add(subscription)
    }

    fun getMealDetailed(): MealDetailed? {
        return meal.value
    }

    fun setMealDetailed(mealDetailed: MealDetailed) {
        meal.value = mealDetailed
    }

//    override fun fetchAll(): Observable<Resource<Unit>> {
//        return remoteDataSource
//            .getAll()
//            .doOnNext {
//                Timber.e("Upis u bazu")
//                val entities = it.map {
//                    MovieEntity(
//                        it.id,
//                        it.title,
//                        it.description
//                    )
//                }
//                localDataSource.deleteAndInsertAll(entities)
//            }
//            .map {
//                Resource.Success(Unit)
//            }
//    }
    override fun onCleared() {
        super.onCleared()
        subscriptions.dispose()
    }

}