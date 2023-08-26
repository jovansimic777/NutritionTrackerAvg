package com.example.nutritiontrackeravg.data.repositories

import com.example.nutritiontrackeravg.data.models.responses.ParameterResponse
import com.example.nutritiontrackeravg.util.ParameterType
import io.reactivex.Observable

interface ParameterRepository {
    fun fetchAll(type: ParameterType): Observable<ParameterResponse>

}