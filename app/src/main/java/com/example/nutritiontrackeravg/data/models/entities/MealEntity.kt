package com.example.nutritiontrackeravg.data.models.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "meal")
data class MealEntity(
    @PrimaryKey
    val id:String,
    val name: String,
    val img:String,
    val instructions:String,
    val link:String,
//  Category can be chicken, beef, desert...
    val category: String,
    val date:Date,
//  Type can be lunch, dinner...
    val type:String,
    val calValue: String
)
