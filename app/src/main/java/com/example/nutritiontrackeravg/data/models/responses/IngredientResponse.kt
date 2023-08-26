package com.example.nutritiontrackeravg.data.models.responses

import com.squareup.moshi.JsonClass
@JsonClass(generateAdapter = true)
data class IngredientResponse(
    val meals: List<Ingredient>,
): ParameterResponse
@JsonClass(generateAdapter = true)
data class Ingredient(
    val idIngredient: String,
    val strIngredient: String,
    val strDescription: String?,
)