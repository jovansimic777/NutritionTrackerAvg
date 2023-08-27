package com.example.nutritiontrackeravg.presentation.view.fragments

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import com.example.nutritiontrackeravg.R
import com.example.nutritiontrackeravg.data.models.MealDto
import com.example.nutritiontrackeravg.databinding.FragmentStatisticsBinding
import com.example.nutritiontrackeravg.presentation.contract.MealEntityContract
import com.example.nutritiontrackeravg.presentation.view.states.MealsState
import com.example.nutritiontrackeravg.presentation.view.viewmodels.MealEntityViewModel
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import timber.log.Timber
import java.time.LocalDate
import kotlin.math.roundToInt

class StatisticsFragment: Fragment(R.layout.fragment_statistics) {
    private val mealEntityViewModel: MealEntityContract.ViewModel by activityViewModel<MealEntityViewModel>()

    private lateinit var chart: BarChart
    private lateinit var changeStatBT:Button
    private var bmi = 0f
    private lateinit var sharedPreferences: SharedPreferences


    private var _binding: FragmentStatisticsBinding? = null

    private val binding get() = _binding!!

    private var showCalStat= false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentStatisticsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        initUi()
        initListeners()
        initObservers()
        mealEntityViewModel.getAllMeals()
    }

    private fun initUi() {
        chart = binding.chart
        changeStatBT = binding.changeStatBT
    }

    private fun initListeners(){
        changeStatBT.setOnClickListener {
            showCalStat = !showCalStat
            mealEntityViewModel.getAllMeals()
        }
    }

    private fun initObservers() {
        mealEntityViewModel.allMeals.observe(viewLifecycleOwner, Observer {
            sharedPreferences = requireActivity().getSharedPreferences(requireActivity().packageName, Context.MODE_PRIVATE)
            bmi = sharedPreferences.getFloat(com.example.nutritiontrackeravg.util.Constants.DAILY_CALORIES, 0f)
            renderState(it)
        })
    }

    private fun renderState(state: MealsState) {
        when (state) {
            is MealsState.Success -> {
//                showLoadingState(false)
                renderData(state.meals)
            }
            is MealsState.Error -> {
//                showLoadingState(false)
                renderData(null)
                Toast.makeText(requireContext().applicationContext, state.message, Toast.LENGTH_SHORT).show()
            }
            is MealsState.Loading -> {
//                showLoadingState(true)
            }
        }
    }

    private fun renderData(meals: List<MealDto>?) {
        if (meals != null) {
            makeGraph(meals)
        }
    }


    private fun makeGraph(meals: List<MealDto>) {
        val days = mutableSetOf<String>()
        val mealsCountMap = mutableMapOf<String, Int>()
        val mealsCalorieMap= mutableMapOf<String, Int>()

        for(m in meals){
            val day = LocalDate.of(m.date.year,m.date.month, m.date.day).dayOfWeek.name
            days.add(day)
//            Timber.e("Day of meal ${m.name}"+day)

            val count = mealsCountMap.getOrDefault(day, 0)
            val calorie = mealsCalorieMap.getOrDefault(day, 0)

            mealsCountMap[day] = count + 1
            mealsCalorieMap[day] = calorie + m.calValue.roundToInt()
        }
//        Timber.e("Meals count map: $mealsCountMap")
//        Timber.e("Meals calorie map: $mealsCalorieMap")

        var entries= mutableListOf<BarEntry>()
        var entriesCal= mutableListOf<BarEntry>()

        days.sorted().toMutableList()
        Timber.e("Days:  $days")
        for (i in days.size-1 downTo 0){
            var count = mealsCountMap.getOrDefault(days.elementAt(i), 0)
            var calorie = mealsCalorieMap.getOrDefault(days.elementAt(i), 0)
            entries.add(BarEntry(i.toFloat(), count.toFloat()))
            entriesCal.add(BarEntry(i.toFloat(), calorie.toFloat()))
        }

        Timber.e("Entries: $entries")
        var dataSet: BarDataSet
        if (showCalStat){
            dataSet = BarDataSet(entriesCal, "Meals")
        }else {
            dataSet = BarDataSet(entries, "Meals")
        }
        dataSet.color = Color.BLUE

        val data = BarData(dataSet)
        chart?.data = data

        val xAxis = chart?.xAxis

        xAxis?.valueFormatter = IndexAxisValueFormatter (
            arrayOf("Pon", "Uto", "Sre", "Cet", "Pet", "Sub", "Ned"))
        xAxis?.setDrawGridLines (false)

        xAxis?.granularity = 1f

        val yAxis = chart?.axisLeft

        yAxis?.axisMinimum = 0f
        chart?.description?.isEnabled = false
        chart?.legend?.isEnabled = false
        chart?.setPinchZoom(false)
        chart?.invalidate()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

//    private fun showLoadingState(loading: Boolean) {
//        binding.loadingPb.isVisible = loading
//    }
}