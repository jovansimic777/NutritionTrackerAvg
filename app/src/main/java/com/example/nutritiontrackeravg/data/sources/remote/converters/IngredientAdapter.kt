package com.example.nutritiontrackeravg.data.sources.remote.converters

class IngredientAdapter {
//    @FromJson
//    fun fromJson(json: Map<String, String>): MealDetailed {
//        val ingredients = json.filterKeys { it.startsWith("strIngredient") && !it.endsWith("null") }
//        return MealDetailed(
//            id = json["idMeal"] ?: "",
//            name = json["strMeal"] ?: "",
//            category = json["strCategory"] ?: "",
//            area = json["strArea"] ?: "",
//            instructions = json["strInstructions"] ?: "",
//            mealThumb = json["strMealThumb"] ?: "",
//            tags = json["strTags"] ?: "",
//            link = json["strYoutube"] ?: "",
//            ingredients = ingredients
//        )
//    }

//    @ToJson
//    fun toJson(mealDetailed: MealDetailed): Map<String, String> {
//        val json = mutableMapOf<String, String>()
//        json["idMeal"] = mealDetailed.id
//        json["strMeal"] = mealDetailed.name
//        json["strCategory"] = mealDetailed.category
//        json["strArea"] = mealDetailed.area
//        json["strInstructions"] = mealDetailed.instructions
//        json["strMealThumb"] = mealDetailed.mealThumb
//        json["strTags"] = mealDetailed.tags
//        json["strYoutube"] = mealDetailed.link
//        json.putAll(mealDetailed.ingredients)
//        return json
//    }
}
