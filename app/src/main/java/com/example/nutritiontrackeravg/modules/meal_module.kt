package com.example.nutritiontrackeravg.modules

import com.example.nutritiontrackeravg.data.repositories.ParameterRepository
import com.example.nutritiontrackeravg.data.repositories.ParameterRepositoryImpl
import com.example.nutritiontrackeravg.data.repositories.MealRepository
import com.example.nutritiontrackeravg.data.repositories.MealRepositoryImpl
import com.example.nutritiontrackeravg.data.repositories.UserRepository
import com.example.nutritiontrackeravg.data.repositories.UserRepositoryImpl
import com.example.nutritiontrackeravg.data.sources.local.MealDataBase
import com.example.nutritiontrackeravg.data.sources.remote.CalorieService
import com.example.nutritiontrackeravg.data.sources.remote.MealService
import com.example.nutritiontrackeravg.data.sources.remote.ParametersService
import com.example.nutritiontrackeravg.presentation.view.viewmodels.AllMealsViewModel
import com.example.nutritiontrackeravg.presentation.view.viewmodels.ParameterViewModel
import com.example.nutritiontrackeravg.presentation.view.viewmodels.LoginViewModel
import com.example.nutritiontrackeravg.presentation.view.viewmodels.MealDetailedViewModel
import com.example.nutritiontrackeravg.presentation.view.viewmodels.MealEntityViewModel
import com.example.nutritiontrackeravg.presentation.view.viewmodels.MealPlanViewModel
import com.example.nutritiontrackeravg.presentation.view.viewmodels.PlanOverviewViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val mealModule = module {

    viewModel { LoginViewModel(userRepository = get()) }

    viewModel { MealDetailedViewModel(mealRepository = get()) }

    viewModel { MealEntityViewModel(mealRepository = get()) }

    viewModel { AllMealsViewModel(mealRepository = get(), calorieService = get()) }

    viewModel { ParameterViewModel(parameterRepository = get()) }

    viewModel { MealPlanViewModel(calorieService = get(), mealRepository = get()) }

    viewModel { PlanOverviewViewModel() }

    single<MealRepository> { MealRepositoryImpl(localDataSource = get(), remoteDataSource = get ()) }

    single<CalorieService> { create(retrofit = get()) }

    single<UserRepository> { UserRepositoryImpl(localDataSource = get()) }

    single<ParameterRepository> { ParameterRepositoryImpl(remoteDataSource = get ()) }

    single { get<MealDataBase>().getUserDao() }

    single { get<MealDataBase>().getMealDao() }

    single<MealService> { create(retrofit = get()) }

    single<ParametersService> { create(retrofit = get()) }

}