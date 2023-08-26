package com.example.nutritiontrackeravg.data.models.responses

import com.squareup.moshi.JsonClass
@JsonClass(generateAdapter = true)
data class CategoryResponse(
    val categories: List<Category>,
): ParameterResponse
@JsonClass(generateAdapter = true)
data class Category(
    val idCategory: String,
    val strCategory: String,
    val strCategoryThumb: String,
    val strCategoryDescription: String
)