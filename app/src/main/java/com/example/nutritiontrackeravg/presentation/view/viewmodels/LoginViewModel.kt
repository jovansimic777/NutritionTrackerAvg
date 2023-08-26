package com.example.nutritiontrackeravg.presentation.view.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.nutritiontrackeravg.data.models.User
import com.example.nutritiontrackeravg.data.repositories.UserRepository
import com.example.nutritiontrackeravg.presentation.contract.UserContract
import com.example.nutritiontrackeravg.presentation.view.states.AddUserState
import com.example.nutritiontrackeravg.presentation.view.states.CheckCredentialsState
import com.example.nutritiontrackeravg.presentation.view.states.UsersState
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import timber.log.Timber

class LoginViewModel(
    private val userRepository: UserRepository
): ViewModel(), UserContract.ViewModel {

    private val subscriptions = CompositeDisposable()
    override val usersState: MutableLiveData<UsersState> = MutableLiveData()
    override val addDone: MutableLiveData<AddUserState> = MutableLiveData()
    override val checkCredentialsState: MutableLiveData<CheckCredentialsState> = MutableLiveData()

    private val publishSubject: PublishSubject<String> = PublishSubject.create()

    init {
//        val subscription = publishSubject
//            .debounce(200, TimeUnit.MILLISECONDS)
//            .distinctUntilChanged()
//            .switchMap {
//                userRepository
//                    .getAllByEmail(it)
//                    .subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .doOnError {
//                        Timber.e("Error in publish subject")
//                        Timber.e(it)
//                    }
//            }
//            .subscribe(
//                {
//                    usersState.value = UsersState.Success(it)
//                },
//                {
//                    usersState.value = UsersState.Error("Error happened while fetching data from db")
//                    Timber.e(it)
//                }
//            )
//        subscriptions.add(subscription)
    }

    override fun getAllUsers() {
        val subscription = userRepository
            .getAll()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    usersState.value = UsersState.Success(it)
                },
                {
                    usersState.value = UsersState.Error("Error happened while fetching data from db")
                    Timber.e(it)
                }
            )
        subscriptions.add(subscription)
    }

    override fun checkCredentials(email: String, password: String) {
        //TODO moze sinhrono da se odradi
        val subscription = userRepository
            .getAllByEmail(email)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    if (it.isEmpty()) {
                        checkCredentialsState.value = CheckCredentialsState.Error("No user with such email")
                    } else {
                        if (it[0].password != password) {
                            checkCredentialsState.value = CheckCredentialsState.Error("Wrong password")
                        } else {
                            checkCredentialsState.value = CheckCredentialsState.Success
                        }
                    }
                },
                {
                    checkCredentialsState.value = CheckCredentialsState.Error("Error happened while fetching data from db")
                    Timber.e(it)
                }
            )
        subscriptions.add(subscription)
    }

    override fun addUser(user: User) {
        val subscription = userRepository
            .insert(user)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    addDone.value = AddUserState.Success
                },
                {
                    addDone.value = AddUserState.Error("Error happened while adding movie")
                    Timber.e(it)
                }
            )
        subscriptions.add(subscription)
    }

    override fun onCleared() {
        super.onCleared()
        subscriptions.dispose()
    }
}