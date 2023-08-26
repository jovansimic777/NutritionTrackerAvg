package com.example.nutritiontrackeravg.data.repositories

import com.example.nutritiontrackeravg.data.models.Area
import com.example.nutritiontrackeravg.data.models.Category
import com.example.nutritiontrackeravg.data.models.Ingredient
import com.example.nutritiontrackeravg.data.models.Parameter
import com.example.nutritiontrackeravg.data.models.entities.MealEntity
import com.example.nutritiontrackeravg.data.models.responses.MealResponse
import com.example.nutritiontrackeravg.data.sources.local.MealDao
import com.example.nutritiontrackeravg.data.sources.remote.MealService
import com.example.nutritiontrackeravg.util.Util
import io.reactivex.Completable
import io.reactivex.Observable
import timber.log.Timber

class MealRepositoryImpl(
    private val localDataSource: MealDao,
    private val remoteDataSource: MealService
) : MealRepository {

    override fun fetchMealById(id: String): Observable<MealResponse> {
//        Timber.e("1 Fetching meal with id: $id")
        return remoteDataSource.getMealById(id)
    }

    override fun getMealById(id: String): Observable<MealEntity> {
        return localDataSource.getMealById(id)
    }

    override fun getAllMeals(): Observable<List<MealEntity>> {
        return localDataSource.getAllMeals()
    }

    override fun insertMeal(meal: MealEntity): Completable {
        return localDataSource.insert(meal)
    }

    override fun deleteMealById(id: String): Completable {
        return localDataSource.deleteById(id)
    }

    override fun deleteAndInsertMeal(id: String, meal: MealEntity) {
        localDataSource.deleteAndInsertMeal(id,meal)
    }

    override fun fetchMealsByFirstLetter(letter: Char): Observable<MealResponse> {
//        Timber.e("1 Fetching meals with first letter: $letter")
        return remoteDataSource.getMealsByFirstLetter(letter)
    }

    override fun fetchMealsByParameter(parameter: Parameter): Observable<MealResponse> {

        return when (parameter) {
            is Category -> fetchMealsByCategory(parameter)
            is Area -> fetchMealsByArea(parameter)
            is Ingredient -> fetchMealsByIngredient(parameter)
            else -> {
                Timber.e("Parameter type not supported")
                Observable.empty()}
        }
    }

    override fun editMeal(meal: MealEntity): Completable {
        localDataSource.deleteById(meal.id)
        return localDataSource.insert(meal)
    }

    private fun fetchMealsByCategory(category: Category ): Observable<MealResponse> {
        return remoteDataSource.getMealsByCategory(category.strCategory)
    }

    private fun fetchMealsByArea(area: Area): Observable<MealResponse> {
        return remoteDataSource.getMealsByArea(area.strArea)
    }

    private fun fetchMealsByIngredient(ingredient: Ingredient): Observable<MealResponse> {
        return remoteDataSource.getMealsByIngredient(Util.formatNameToSnakeLowerCase(ingredient.strIngredient))
    }
}