package com.example.nutritiontrackeravg.data.sources.remote

import com.example.nutritiontrackeravg.data.models.responses.AreaResponse
import com.example.nutritiontrackeravg.data.models.responses.CategoryResponse
import com.example.nutritiontrackeravg.data.models.responses.IngredientResponse
import io.reactivex.Observable
import retrofit2.http.GET

interface ParametersService {

    @GET("categories.php?")
    fun getAllCategories(): Observable<CategoryResponse>

    @GET("list.php?a=list")
    fun getAllAreas(): Observable<AreaResponse>

    @GET("list.php?i=list")
    fun getAllIngredients(): Observable<IngredientResponse>

}