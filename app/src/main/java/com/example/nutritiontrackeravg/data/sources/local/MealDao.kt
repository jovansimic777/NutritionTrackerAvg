package com.example.nutritiontrackeravg.data.sources.local

import androidx.room.*
import com.example.nutritiontrackeravg.data.models.entities.MealEntity
import io.reactivex.Completable
import io.reactivex.Observable

@Dao
abstract class MealDao {
    @Insert( onConflict = OnConflictStrategy.REPLACE )
    abstract fun insert(entity: MealEntity): Completable

    @Query("SELECT * FROM meal WHERE id = :id")
    abstract fun getMealById(id: String): Observable<MealEntity>

    @Query("SELECT * FROM meal")
    abstract fun getAllMeals(): Observable<List<MealEntity>>

    @Query("DELETE FROM meal WHERE id = :id")
    abstract fun deleteById(id: String): Completable

    @Transaction
    open fun deleteAndInsertMeal(id:String, meal:MealEntity) {
        deleteById(id)
        insert(meal)
    }
}