package com.example.nutritiontrackeravg.data.sources.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.nutritiontrackeravg.data.models.entities.MealEntity
import com.example.nutritiontrackeravg.data.models.entities.UserEntity
import com.example.nutritiontrackeravg.data.sources.local.converters.DateConverter

@Database(
    entities = [UserEntity::class,
                MealEntity::class],
    version = 7,
    exportSchema = false)
@TypeConverters(DateConverter::class)
abstract class MealDataBase : RoomDatabase() {
    abstract fun getUserDao(): UserDao
    // TODO figure out what does this do
    abstract fun getMealDao(): MealDao
}