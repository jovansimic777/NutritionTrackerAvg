package com.example.nutritiontrackeravg.presentation.view.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.nutritiontrackeravg.data.repositories.ParameterRepository
import com.example.nutritiontrackeravg.data.sources.remote.converters.ParameterConverter
import com.example.nutritiontrackeravg.presentation.contract.ParametersContract
import com.example.nutritiontrackeravg.presentation.view.states.ParameterState
import com.example.nutritiontrackeravg.presentation.view.states.ParametersState
import com.example.nutritiontrackeravg.util.ParameterType
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class ParameterViewModel(
    private val parameterRepository: ParameterRepository
) : ViewModel(), ParametersContract.ViewModel {
    override val parametersState: MutableLiveData<ParametersState> = MutableLiveData()
    override val selectedParameterState: MutableLiveData<ParameterState> = MutableLiveData(ParameterState.Empty)

    private val subscriptions = CompositeDisposable()

    override fun fetchAll(type: ParameterType) {
        val subscription = parameterRepository
            .fetchAll(type)
            .map<ParametersState> { ParametersState.Success(ParameterConverter.mapParameterResponseToParameter(it)) }
            .startWith(ParametersState.Loading)
            .onErrorReturn { ParametersState.Error(it.message ?: "Unknown error occurred") }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { it ->
                Timber.e("Response categories" + it.toString());
                when (it) {
                    is ParametersState.Success -> {
                        parametersState.value = it
                    }
                    is ParametersState.Loading -> {
                        parametersState.value = ParametersState.Loading
                    }
                    is ParametersState.Error -> {
                        parametersState.value = ParametersState.Error(it.message)
                    }
                }
            }
        subscriptions.add(subscription)
    }
}
