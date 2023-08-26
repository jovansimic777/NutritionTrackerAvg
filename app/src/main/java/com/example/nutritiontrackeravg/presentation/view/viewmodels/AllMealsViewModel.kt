package com.example.nutritiontrackeravg.presentation.view.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.nutritiontrackeravg.data.models.Area
import com.example.nutritiontrackeravg.data.models.Category
import com.example.nutritiontrackeravg.data.models.Ingredient
import com.example.nutritiontrackeravg.data.models.MealSimple
import com.example.nutritiontrackeravg.data.models.Parameter
import com.example.nutritiontrackeravg.data.repositories.MealRepository
import com.example.nutritiontrackeravg.data.sources.remote.CalorieService
import com.example.nutritiontrackeravg.data.sources.remote.converters.MealSimpleConverter
import com.example.nutritiontrackeravg.presentation.contract.MealsContract
import com.example.nutritiontrackeravg.presentation.view.states.MealsApiState
import com.example.nutritiontrackeravg.util.SortType
import com.example.nutritiontrackeravg.util.Util
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import timber.log.Timber
import java.util.concurrent.locks.ReentrantLock

class AllMealsViewModel(
    private val calorieService: CalorieService,
    private val mealRepository: MealRepository
): ViewModel(), MealsContract.ViewModel {

    private val subscriptions = CompositeDisposable()
    override val mealsState: MutableLiveData<MealsApiState> = MutableLiveData()
    override val fullMealsState: MutableLiveData<List<MealSimple>> = MutableLiveData()
    override val initialRender: MutableLiveData<Boolean> = MutableLiveData(true)
    override val loadedMealsData: MutableLiveData<Boolean> = MutableLiveData(false)
    override var tagQuery: MutableLiveData<String> = MutableLiveData("")
    private var sortParameter: SortType? = null
    private var queryChar: Char? = null
    override var queryString: MutableLiveData<String> = MutableLiveData("")
    private var parameter: Parameter? = null
    private var brojac = 0
    var cnt = 0

    private val calMeal: MutableLiveData<MealSimple> = MutableLiveData()

    private val publishSubject: PublishSubject<String> = PublishSubject.create()

    init {
    }


    private fun fetchMeals() {
        Timber.e("fetchMeals " + queryChar + " " + queryString + " " + parameter)
        if(queryChar == null) {
//            Timber.e("uso")
            fetchMealsByParameter()
        }
        else {
//            Timber.e("uso2")
            fetchMealsByFirstLetter()
        }
    }

    private fun fetchMealsByFirstLetter() {
        val subscription = mealRepository
            .fetchMealsByFirstLetter(queryChar!!)
            .map<MealsApiState> { MealsApiState.Success(MealSimpleConverter.mapMealResponseToMealSimple(it)) }
            .startWith(MealsApiState.Loading)
            .onErrorReturn { MealsApiState.Error(it.message ?: "Unknown error occurred") }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { it ->
//                Timber.e("Response meals" + it.toString())
                when (it) {
                    is MealsApiState.Success -> {
                        fullMealsState.value = it.meals
//                        numberOfApiCalls()
                        brojac++
                        fetchCalories(brojac)
                        applyFilters()
                    }
                    is MealsApiState.Loading -> {
                        mealsState.value = MealsApiState.Loading
                    }
                    is MealsApiState.Error -> {
                        mealsState.value = MealsApiState.Error(it.message)
                    }
                }
            }
        subscriptions.add(subscription)
    }

    private fun numberOfApiCalls(){
        var cnt = 0
        for (meal in fullMealsState.value.orEmpty()) {
            for ((key, value) in meal.ingredients) {
                if (key == null || value == null) {
                    break
                }
                cnt++
            }
        }
        Timber.e("Broj poziva: $cnt")
    }

    // Perform synchronized access on sharedInt
    fun incrementSharedInt(index: Int) {
        try {
            synchronized(sharedInt) {
                // Perform modifications to mutableList
                sharedInt[index]++

            }
            // Critical section
//            sharedInt[index]++
        } finally {

        }
    }

    // Shared variable
    var sharedInt: MutableList<Int> = mutableListOf()
    var globalLock = ReentrantLock()
    var mealGain = 0
    private fun fetchCalories(initBrojac: Int) {
        Timber.e("Usao u fetchCalories")
        var fetchedList: MutableList<MealSimple> = mutableListOf()

        sharedInt = MutableList(fullMealsState.value!!.size) { 0 }

        for ((index, meal)  in fullMealsState.value.orEmpty().withIndex()) {
            // Shared variable
            Timber.e("Meal number of ingredients: ${meal.ingredients.size}")
            for ((key, value) in meal.ingredients) {

                incrementSharedInt(index)

                if (key == null || value == null) {
                    break
                }
//                Timber.e("Map iteration: Ingredient: $key, Measure: $value")
                val vFetch = Util.formatNameToSnakeLowerCase(value)
                val kFetch = Util.formatNameToSnakeLowerCase(key)
                val subscription= calorieService
                    .getNutritionContent("$vFetch $kFetch")
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        {
//                            Timber.e(""+it.toString())
                            var total= 0.0
                            for (food in it) {
                                total += food.calories
                            }
                            Timber.e("Total: $total")
                            Timber.e("Ingredient number: $sharedInt")
                            meal.calValue += total
                            Timber.e("acac ok ing")

                            // only when it is the last ingredient
//                            lock.get(index).lock()
                            Timber.e("meal.ingredients.size: ${meal.ingredients.size} + sharedInt: ${sharedInt[index]}")
                            if (meal.ingredients.size== sharedInt[index]){
                                Timber.e("DODAO SASTOJAK")
                                globalLock.lock()
                                fetchedList.remove(meal)
                                fetchedList.add(meal)
                                Timber.e("acac gain $mealGain " + fetchedList.size.toString())
                                mealGain++
                                globalLock.unlock()
                            }
                            globalLock.lock()
                            if(mealGain > 5) {
                                mealGain = 0
                                globalLock.unlock()
                                fullMealsState.value = fetchedList
                                applyFilters()
                            } else {
                                globalLock.unlock()
                            }
                            if (fetchedList.size == fullMealsState.value?.size) {
                                Timber.e("UPDEJTOVAO JE LISTU")
                                fullMealsState.value = fetchedList
                                applyFilters()
                            }
//                            lock.get(index).unlock()
                            },
                        {
                            Timber.e(it)
                        }
                    )
                subscriptions.add(subscription)
            }
        }


//        Thread{
//            while (true){
//                if (fetchedList.size == fullMealsState.value?.size /*&& initBrojac == brojac*/) {
//                    fullMealsState.value = fetchedList
//                    break
//                }/*else if (initBrojac != brojac){
//                    break
//                }*/
//            }
//        }.start()

        ///
//        fullMealsStateCalories = listOf()

//        if(initBrojac == brojac) {
//            fullMealsState.value = //
//            applyFilters()
//        }
    }

    private fun fetchMealsByParameter() {
        if(parameter == null) {
            fullMealsState.value = listOf()
            mealsState.value = MealsApiState.Success(listOf())
            return
        }
        val subscription = mealRepository
            .fetchMealsByParameter(parameter!!)
            .map<MealsApiState> { MealsApiState.Success(MealSimpleConverter.mapMealResponseToMealSimple(it)) }
            .startWith(MealsApiState.Loading)
            .onErrorReturn { MealsApiState.Error(it.message ?: "Unknown error occurred") }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { it ->
                Timber.e("Response meals" + it.toString());
                when (it) {
                    is MealsApiState.Success -> {
                        fullMealsState.value = it.meals
                        applyFilters()
                    }
                    is MealsApiState.Loading -> {
                        mealsState.value = MealsApiState.Loading
                    }
                    is MealsApiState.Error -> {
                        mealsState.value = MealsApiState.Error(it.message)
                    }
                }
            }
        subscriptions.add(subscription)
    }

    // just filters without fetching
    private fun applyFilters() {
        if(fullMealsState.value == null) {
            mealsState.value = MealsApiState.Success(listOf())
            return
        }
        val filteredMeals = mutableListOf<MealSimple>()
        if(queryChar != null) {
            for (meal in fullMealsState.value!!) {
                if (!meal.name!!.startsWith(queryString.value!!, true))
                    continue

//                Timber.e("Meal: " + meal.name + " " + meal.getIngredients());

                val strCategoryExists = meal.javaClass.declaredFields.any { it.name == "strCategory" }
                val strTagsExists = meal.javaClass.declaredFields.any { it.name == "strTags" }

                if(parameter != null && strCategoryExists) {
                    when (parameter) {
                        is Category -> {
                            if (meal.strCategory != (parameter as Category).strCategory)
                                continue
                        }

                        is Area -> {
                            if (meal.strArea != (parameter as Area).strArea)
                                continue
                        }

                        is Ingredient -> {
                            if (!meal.ingredients.containsKey((parameter!! as Ingredient).strIngredient))
                                continue
                        }
                    }
                }
                if(strTagsExists && tagQuery != null && tagQuery.value != " " && !tagQuery.value!!.isEmpty()) {
                    if(!meal.strTags!!.contains(tagQuery.value!!, true))
                        continue
                }
                filteredMeals.add(meal)
            }
        } else {
            filteredMeals.addAll(fullMealsState.value!!)
        }
        setMealList(filteredMeals)
    }

    override fun updateSearchQuery(query: String) {

        Timber.e("updateSearchQuery " + query)

        var changedString = false

        if (query != queryString.value) {
            queryString.value = query
            changedString = true
        }

        val updatedFirstLetter = setQueryChar(query)
        Timber.e("timber " + queryChar + " " + queryString + " " + parameter)


        if(updatedFirstLetter)
            fetchMeals()

        else if(changedString && query != null && query.isNotEmpty())
            applyFilters()

    }

    private fun setQueryChar(query: String): Boolean {
        if(query.isEmpty()) {
            if (queryChar == null)
                return false
            queryChar = null
            return true
        }
        if(queryChar == query[0])
            return false

        queryChar = query[0]
        return true
    }

    override fun fetchMealsByFilter(filter: String) {
        TODO("Not yet implemented")
    }

    override fun fetchMealsByName(name: String) {
        TODO("Not yet implemented")
    }

    override fun setFilter(parameter: Parameter?) {
        this.parameter = parameter

        if(queryChar != null) {
            applyFilters()
        }
        else
            fetchMealsByParameter()
    }

    override fun setTag(tag: String) {
        this.tagQuery.value = tag
        applyFilters()
    }

    override fun setInitialRender(value: Boolean) {
        this.initialRender.value = value
    }

    override fun setLoadedMealsData(value: Boolean) {
        this.loadedMealsData.value = value
    }

    override fun setSort(sortType: SortType) {
        this.sortParameter = sortType
        applyFilters()
    }

    private fun setMealList(unsortedList: List<MealSimple>) {
        var meals : List<MealSimple>  = unsortedList
        if(sortParameter != null) {
            when(sortParameter) {
                SortType.NONE -> {}
                SortType.ABC -> {
                    meals = meals.sortedBy { it.name }
                }
                SortType.CALORIES -> {
                    meals = meals.sortedBy { -it.calValue }
                }
                else -> {}
            }
        }
        mealsState.value = MealsApiState.Success(meals)
    }
}