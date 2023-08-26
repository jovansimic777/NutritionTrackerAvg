package com.example.nutritiontrackeravg.data.sources.remote.converters

import com.example.nutritiontrackeravg.data.models.MealSimple
import com.example.nutritiontrackeravg.data.models.entities.MealEntity
import com.example.nutritiontrackeravg.data.models.responses.Meal
import com.example.nutritiontrackeravg.data.models.responses.MealResponse

class MealSimpleConverter {
    companion object {

        fun mapMealEntityToMealSimple(mealEntity: MealEntity): MealSimple {
            return MealSimple(
                id = mealEntity.id,
                name = mealEntity.name,
                link = mealEntity.img,
                strCategory = mealEntity.category,
                strArea = null,
                strTags = null,
                ingredients = hashMapOf()
            )
        }

        fun mapListMealEntityToMealSimple(mealEntityList: List<MealEntity>): List<MealSimple> {
            val returnList: MutableList<MealSimple> = mutableListOf()
            for(mealEntity: MealEntity in mealEntityList) {
                returnList += mapMealEntityToMealSimple(mealEntity)
            }
            return returnList
        }

        fun mapMealResponseToMealSimple(mealResponse: MealResponse): List<MealSimple> {
            val returnList: MutableList<MealSimple> = mutableListOf()

            for(meal: Meal in mealResponse.meals) {
                returnList += MealSimple(
                    id = meal.idMeal,
                    name = meal.strMeal,
                    link = meal.strMealThumb,
                    strCategory = meal.strCategory?:"",
                    strArea = meal.strArea?:"",
                    strTags = meal.strTags?:"",
                    ingredients = mapIngredientsToHashMap(meal)
                )
            }
            return returnList
        }

        private fun mapIngredientsToHashMap(meal: Meal): HashMap<String, String> {
            val returnMap: HashMap<String, String> = hashMapOf()
            if(meal.strIngredient1 != null) {
                returnMap[meal.strIngredient1] = meal.strMeasure1?:""
            }
            if(meal.strIngredient2 != null) {
                returnMap[meal.strIngredient2] = meal.strMeasure2?:""
            }
            if(meal.strIngredient3 != null) {
                returnMap[meal.strIngredient3] = meal.strMeasure3?:""
            }
            if(meal.strIngredient4 != null) {
                returnMap[meal.strIngredient4] = meal.strMeasure4?:""
            }
            if(meal.strIngredient5 != null) {
                returnMap[meal.strIngredient5] = meal.strMeasure5?:""
            }
            if(meal.strIngredient6 != null) {
                returnMap[meal.strIngredient6] = meal.strMeasure6?:""
            }
            if(meal.strIngredient7 != null) {
                returnMap[meal.strIngredient7] = meal.strMeasure7?:""
            }
            if(meal.strIngredient8 != null) {
                returnMap[meal.strIngredient8] = meal.strMeasure8?:""
            }
            if(meal.strIngredient9 != null) {
                returnMap[meal.strIngredient9] = meal.strMeasure9?:""
            }
            if(meal.strIngredient10 != null) {
                returnMap[meal.strIngredient10] = meal.strMeasure10?:""
            }
            if(meal.strIngredient11 != null) {
                returnMap[meal.strIngredient11] = meal.strMeasure11?:""
            }
            if(meal.strIngredient12 != null) {
                returnMap[meal.strIngredient12] = meal.strMeasure12?:""
            }
            if(meal.strIngredient13 != null) {
                returnMap[meal.strIngredient13] = meal.strMeasure13?:""
            }
            if(meal.strIngredient14 != null) {
                returnMap[meal.strIngredient14] = meal.strMeasure14?:""
            }
            if(meal.strIngredient15 != null) {
                returnMap[meal.strIngredient15] = meal.strMeasure15?:""
            }
            if(meal.strIngredient16 != null) {
                returnMap[meal.strIngredient16] = meal.strMeasure16?:""
            }
            if(meal.strIngredient17 != null) {
                returnMap[meal.strIngredient17] = meal.strMeasure17?:""
            }
            if(meal.strIngredient18 != null) {
                returnMap[meal.strIngredient18] = meal.strMeasure18?:""
            }
            if(meal.strIngredient19 != null) {
                returnMap[meal.strIngredient19] = meal.strMeasure19?:""
            }
            if(meal.strIngredient20 != null) {
                returnMap[meal.strIngredient20] = meal.strMeasure20?:""
            }
            return returnMap
        }
    }
}