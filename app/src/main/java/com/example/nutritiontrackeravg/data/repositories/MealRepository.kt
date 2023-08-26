package com.example.nutritiontrackeravg.data.repositories

import com.example.nutritiontrackeravg.data.models.Parameter
import com.example.nutritiontrackeravg.data.models.entities.MealEntity
import com.example.nutritiontrackeravg.data.models.responses.MealResponse
import io.reactivex.Completable
import io.reactivex.Observable

interface MealRepository {
    fun fetchMealById(id: String): Observable<MealResponse>
    fun getMealById(id: String): Observable<MealEntity>
    fun getAllMeals(): Observable<List<MealEntity>>
    fun insertMeal(meal: MealEntity): Completable
    fun deleteMealById(id:String): Completable
    fun deleteAndInsertMeal(id: String, meal: MealEntity)
    fun fetchMealsByFirstLetter(letter: Char): Observable<MealResponse>
    fun fetchMealsByParameter(parameter: Parameter): Observable<MealResponse>
    fun editMeal(meal: MealEntity): Completable

//    fun fetchAll(): Observable<Resource<Unit>>
//    fun getAll(): Observable<List<Movie>>
//    fun getAllByName(name: String): Observable<List<Movie>>
//    fun insert(movie: Movie): Completable

}