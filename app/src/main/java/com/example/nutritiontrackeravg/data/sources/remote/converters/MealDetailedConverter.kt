package com.example.nutritiontrackeravg.data.sources.remote.converters

import com.example.nutritiontrackeravg.data.models.MealDetailed
import com.example.nutritiontrackeravg.data.models.responses.Meal

class MealDetailedConverter {
    companion object {

        fun convertToMealDetailed(meal: Meal): MealDetailed {
            val ingredients = mapIngredients(meal)
            return MealDetailed(
                id = meal.idMeal,
                name = meal.strMeal,
                category = meal.strCategory,
                area = meal.strArea,
                instructions = meal.strInstructions,
                mealThumb = meal.strMealThumb,
                tags = meal.strTags ?: "Not available",
                link = meal.strYoutube,
                ingredients = ingredients
            )
        }

        private fun mapIngredients(meal: Meal): Map<String, String> {
            val ingredients = mutableMapOf<String, String>()
            for (i in 1..20) {
                val ingredientKey = meal.getIngredientKey(i)
                val ingredientValue = meal.getIngredientValue(i)
                if (ingredientKey.isNotEmpty() && ingredientValue.isNotEmpty()) {
                    ingredients[ingredientKey] = ingredientValue
                } else {

                }
            }
            return ingredients
        }

        private fun Meal.getIngredientKey(index: Int): String {
            return getFieldValue("strIngredient", index)
        }

        private fun Meal.getIngredientValue(index: Int): String {
            return getFieldValue("strMeasure", index)
        }

        private fun Meal.getFieldValue(fieldPrefix: String, index: Int): String {
            val field = javaClass.getDeclaredField("$fieldPrefix$index")
            field.isAccessible = true
            val value = field.get(this)
            return value?.toString() ?: ""
        }
    }
}
