package com.example.nutritiontrackeravg.data.models

class MealSimple(
    val id:String,
    val name: String?,
    val link: String?,
    val strCategory: String?,
    val strArea: String?,
    val strTags: String?,
    val ingredients: HashMap<String, String>
){
    var calValue = 0.0


    fun getIngredients(): String {
        var returnString = ""
        for(ingredient in ingredients) {
            returnString += "${ingredient.key} - ${ingredient.value}\n"
        }
        return returnString
    }

    override fun toString(): String {
        return "MealSimple(name=$name, calValue=$calValue)"
    }

}