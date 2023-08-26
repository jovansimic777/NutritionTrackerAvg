package com.example.nutritiontrackeravg.data.sources.remote

import com.example.nutritiontrackeravg.data.models.responses.Food
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface CalorieService {

    @Headers("X-Api-Key: eHzI8Fos1M6+kEfJweWdCQ==OIOKis4v1bRadYP1")
    @GET("https://api.api-ninjas.com/v1/nutrition")
    fun getNutritionContent (@Query("query") title: String): Observable<List<Food>>

}