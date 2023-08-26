package com.example.nutritiontrackeravg.data.sources.remote

import com.example.nutritiontrackeravg.data.models.responses.MealResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface MealService {

    @GET("meals")
    fun getAll(@Query("limit") limit: Int = 1000): Observable<List<MealResponse>>

    @GET("lookup.php?")
    fun getMealById(@Query("i") mealId: String): Observable<MealResponse>

    @GET("search.php?")
    fun getMealsByFirstLetter(@Query("f") letter: Char): Observable<MealResponse>

    @GET("filter.php?")
    fun getMealsByCategory(@Query("c") category: String): Observable<MealResponse>

    @GET("filter.php?")
    fun getMealsByArea(@Query("a") area: String): Observable<MealResponse>

    @GET("filter.php?")
    fun getMealsByIngredient(@Query("i") ingredient: String): Observable<MealResponse>
}