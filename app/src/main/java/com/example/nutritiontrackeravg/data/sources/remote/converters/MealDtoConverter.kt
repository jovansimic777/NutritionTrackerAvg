package com.example.nutritiontrackeravg.data.sources.remote.converters

import com.example.nutritiontrackeravg.data.models.MealDto
import com.example.nutritiontrackeravg.data.models.entities.MealEntity
import com.example.nutritiontrackeravg.data.models.responses.Meal
import com.example.nutritiontrackeravg.data.models.responses.MealResponse
import java.util.Date

class MealDtoConverter {
    companion object {
        fun mapMealEntityToMealDto(mealEntity: MealEntity): MealDto {
            return MealDto(
                id = mealEntity.id,
                name = mealEntity.name,
                img = mealEntity.img,
                instructions = mealEntity.instructions,
                link = mealEntity.link,
                category = mealEntity.category,
                date = mealEntity.date,
                type = mealEntity.type,
                calValue = mealEntity.calValue.toDouble(),
                ingredients = hashMapOf()
            )
        }

        fun mapMealDtoToMealEntity(mealDto: MealDto): MealEntity {
            return MealEntity(
                id = mealDto.id,
                name = mealDto.name,
                img = mealDto.img,
                instructions = mealDto.instructions,
                link = mealDto.link,
                category = mealDto.category,
                date = mealDto.date,
                type = mealDto.type,
                calValue = mealDto.calValue.toString()
            )
        }

        fun mapListMealEntityToListMealDto(listMealEntity: List<MealEntity>): List<MealDto> {
            val listMealDto = mutableListOf<MealDto>()
            for (mealEntity in listMealEntity) {
                listMealDto.add(mapMealEntityToMealDto(mealEntity))
            }
            return listMealDto
        }

        fun mapMealResponseToListMealDto(listMealResponse: MealResponse): List<MealDto> {
            val listMealDto = mutableListOf<MealDto>()
            for (mealResponse in listMealResponse.meals) {
                listMealDto.add(mapMealResponseToMealDto(mealResponse))
            }
            return listMealDto
        }

        fun mapMealResponseToMealDto(mealResponse: Meal): MealDto {
            return MealDto(
                id = mealResponse.idMeal,
                name = mealResponse.strMeal?:"",
                img = mealResponse.strMealThumb?:"",
                instructions = mealResponse.strInstructions?:"",
                link = mealResponse.strSource?:"",
                category = mealResponse.strCategory?:"",
                date = Date(),
//                date = mealResponse.dateModified?:"",
                type = mealResponse.strArea?:"",
                calValue = -1.0,
                ingredients = mapIngredientsToHashMap(mealResponse)
            )
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