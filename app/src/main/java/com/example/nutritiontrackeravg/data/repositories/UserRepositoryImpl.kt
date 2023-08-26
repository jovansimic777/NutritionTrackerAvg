package com.example.nutritiontrackeravg.data.repositories

import com.example.nutritiontrackeravg.data.models.User
import com.example.nutritiontrackeravg.data.models.entities.UserEntity
import com.example.nutritiontrackeravg.data.sources.local.UserDao
import io.reactivex.Completable
import io.reactivex.Observable

class UserRepositoryImpl(
    private val localDataSource: UserDao,
//    private val remoteDataSource: UserDao
) : UserRepository {
    override fun getAll(): Observable<List<User>> {
        return localDataSource
            .getAll()
            .map {
                it.map { userEntity ->
                    User(
                        userEntity.id,
                        userEntity.username,
                        userEntity.email,
                        userEntity.password,
                        userEntity.fullName
                    )
                }
            }
    }

    override fun getAllByEmail(email: String): Observable<List<User>> {
        return localDataSource
            .getByEmail(email)
            .map {
                it.map { userEntity ->
                    User(
                        userEntity.id,
                        userEntity.username,
                        userEntity.email,
                        userEntity.password,
                        userEntity.fullName
                    )
                }
            }
    }

    override fun insert(user: User): Completable {
        val userEntity = UserEntity(
            user.id,
            user.email,
            user.username,
            user.password,
            user.fullName
        )
        return localDataSource.insert(userEntity)
    }
}