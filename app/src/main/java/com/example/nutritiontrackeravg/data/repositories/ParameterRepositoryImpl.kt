package com.example.nutritiontrackeravg.data.repositories

import com.example.nutritiontrackeravg.data.models.responses.ParameterResponse
import com.example.nutritiontrackeravg.data.sources.remote.ParametersService
import com.example.nutritiontrackeravg.util.ParameterType
import io.reactivex.Observable

class ParameterRepositoryImpl(
    private val remoteDataSource: ParametersService
) : ParameterRepository {

    override fun fetchAll(type: ParameterType): Observable<ParameterResponse> {

        return when (type) {
            ParameterType.CATEGORY -> fetchAllCategories()
            ParameterType.AREA -> fetchAllAreas()
            ParameterType.INGREDIENT -> fetchAllIngredients()
        }
    }

    private fun fetchAllCategories(): Observable<ParameterResponse> {
        return remoteDataSource.getAllCategories()
            .map { it as ParameterResponse }
    }

    private fun fetchAllAreas(): Observable<ParameterResponse> {
        return remoteDataSource.getAllAreas()
            .map { it as ParameterResponse }
    }

    private fun fetchAllIngredients(): Observable<ParameterResponse> {
        return remoteDataSource.getAllIngredients()
            .map { it as ParameterResponse }
    }
}