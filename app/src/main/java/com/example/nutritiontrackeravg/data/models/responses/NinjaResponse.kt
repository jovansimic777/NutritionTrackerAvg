package com.example.nutritiontrackeravg.data.models.responses

import com.squareup.moshi.JsonClass

//@JsonClass(generateAdapter = true)
//data class IngredientsResponse(
//    val meals: List<Ingredient>
//)

@JsonClass(generateAdapter = true)
data class Food(
    val name: String,
    val calories: Double,
    val serving_size_g: Double,
    val fat_total_g: Double,
    val fat_saturated_g: Double,
    val protein_g: Double,
    val sodium_mg: Double,
    val potassium_mg: Double,
    val cholesterol_mg: Double,
    val carbohydrates_total_g: Double,
    val fiber_g: Double,
    val sugar_g: Double
)
//{
//    "name": "brisket",
//    "calories": 1312.3,
//    "serving_size_g": 453.592,
//    "fat_total_g": 82.9,
//    "fat_saturated_g": 33.2,
//    "protein_g": 132,
//    "sodium_mg": 217,
//    "potassium_mg": 781,
//    "cholesterol_mg": 487,
//    "carbohydrates_total_g": 0,
//    "fiber_g": 0,
//    "sugar_g": 0
//}
