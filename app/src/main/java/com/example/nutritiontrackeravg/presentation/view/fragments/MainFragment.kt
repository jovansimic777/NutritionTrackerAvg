package com.example.nutritiontrackeravg.presentation.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.nutritiontrackeravg.R
import com.example.nutritiontrackeravg.databinding.FragmentMainBinding
import com.example.nutritiontrackeravg.presentation.contract.MealDetaildContract
import com.example.nutritiontrackeravg.presentation.view.viewmodels.MealDetailedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainFragment : Fragment(R.layout.fragment_main) {
    // Test - delete later
    private val mealDetailedVM: MealDetaildContract.ViewModel by viewModel<MealDetailedViewModel>()

    private var _binding: FragmentMainBinding? = null

    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    private val allMealsFragment by lazy { AllMealsFragment() }
    private val myMealsFragment by lazy { MyMealsFragment() }
    private val mealPlanFragment by lazy { MealPlanFragment() }
    private val statisticsFragment by lazy { StatisticsFragment() }
    private val profileFragment by lazy { ProfileFragment() }

//    private val allMealsFragment = AllMealsFragment()
//    private val myMealsFragment = MyMealsFragment()
//    private val mealPlanFragment = MealPlanFragment()
//    private val statisticsFragment = StatisticsFragment()
//    private val profileFragment = ProfileFragment()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainBinding.inflate(inflater, container, false)

        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            val fragment: Fragment = when (item.itemId) {
                R.id.navigation_1 -> allMealsFragment
                R.id.navigation_2 -> myMealsFragment
                R.id.navigation_3 -> MealPlanFragment()
                R.id.navigation_4 -> statisticsFragment
                R.id.navigation_5 -> profileFragment
                // Add more cases for additional fragments
                else -> allMealsFragment // Default fragment
            }

            replaceFragment(fragment)
            true
        }
        return binding.root
    }

    private fun replaceFragment(fragment: Fragment) {
        childFragmentManager.beginTransaction()
            .replace(binding.childFragmentContainer.id, fragment)
            .commit()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        replaceFragment(AllMealsFragment())
        init()
    }

    private fun init() {
        initUi()
        initObservers()
    }

    private fun initUi() {

    }

    private fun initObservers() {

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}