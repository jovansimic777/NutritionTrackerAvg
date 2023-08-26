package com.example.nutritiontrackeravg.data.repositories

import io.reactivex.Completable
import io.reactivex.Observable
import com.example.nutritiontrackeravg.data.models.User

interface UserRepository {

    fun getAll(): Observable<List<User>>
    fun getAllByEmail(email: String): Observable<List<User>>
    fun insert(user: User): Completable
}