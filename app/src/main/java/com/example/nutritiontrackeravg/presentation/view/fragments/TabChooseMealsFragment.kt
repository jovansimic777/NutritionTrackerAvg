package com.example.nutritiontrackeravg.presentation.view.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nutritiontrackeravg.R
import com.example.nutritiontrackeravg.databinding.FragmentChooseMealsBinding
import com.example.nutritiontrackeravg.presentation.contract.MealPlanContract
import com.example.nutritiontrackeravg.presentation.contract.PlanOverViewContract
import com.example.nutritiontrackeravg.presentation.view.activities.recycler.adapter.PickMealAdapter
import com.example.nutritiontrackeravg.presentation.view.states.MealsState
import com.example.nutritiontrackeravg.presentation.view.viewmodels.MealPlanViewModel
import com.example.nutritiontrackeravg.presentation.view.viewmodels.PlanOverviewViewModel
import com.example.nutritiontrackeravg.util.MealType
import com.example.nutritiontrackeravg.util.Weekday
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import timber.log.Timber

class TabChooseMealsFragment : Fragment(R.layout.fragment_choose_meals) {

    private val mealPlanViewModel: MealPlanContract.ViewModel by activityViewModel<MealPlanViewModel>()
    private val planOverviewViewModel: PlanOverViewContract.ViewModel by activityViewModel<PlanOverviewViewModel>()

    private lateinit var adapterOnline: PickMealAdapter
    private lateinit var adapterLocal: PickMealAdapter

    private var _binding: FragmentChooseMealsBinding? = null

    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentChooseMealsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        initUi()
        initObservers()
    }

    private fun initUi() {
        if(mealPlanViewModel.mealTypeSelected.value == MealType.ALLMEALS) {
            binding.switchButton.isChecked = true
            binding.listRvLocal.visibility = View.GONE
            binding.listRvOnline.visibility = View.VISIBLE
        } else {
            binding.switchButton.isChecked = false
            binding.listRvLocal.visibility = View.VISIBLE
            binding.listRvOnline.visibility = View.GONE
        }
        initRecycler()
    }

    private fun initRecycler() {

        binding.listRvOnline.layoutManager = LinearLayoutManager(context)
        binding.listRvLocal.layoutManager = LinearLayoutManager(context)

        adapterOnline = PickMealAdapter ({ meal -> // on click
        },{ meal -> // on pick
            val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_weekday_selection, null)
            val builder = AlertDialog.Builder(context)
            builder.setView(dialogView)
                .setPositiveButton("OK") { dialog, which ->
                    // Handle positive button click
                    // Get the selected weekdays from the checkboxes
                    val mondayChecked = dialogView.findViewById<CheckBox>(R.id.checkbox_monday).isChecked
                    if(mondayChecked)
                        planOverviewViewModel.addMealToDay(meal, Weekday.MONDAY)
                    val tuesdayChecked = dialogView.findViewById<CheckBox>(R.id.checkbox_tuesday).isChecked
                    if (tuesdayChecked)
                        planOverviewViewModel.addMealToDay(meal, Weekday.TUESDAY)
                    val wednesdayChecked = dialogView.findViewById<CheckBox>(R.id.checkbox_wednesday).isChecked
                    val thursdayChecked = dialogView.findViewById<CheckBox>(R.id.checkbox_thursday).isChecked
                    val fridayChecked = dialogView.findViewById<CheckBox>(R.id.checkbox_friday).isChecked
                    if (fridayChecked)
                        planOverviewViewModel.addMealToDay(meal, Weekday.FRIDAY)
                    val saturdayChecked = dialogView.findViewById<CheckBox>(R.id.checkbox_saturday).isChecked
                    val sundayChecked = dialogView.findViewById<CheckBox>(R.id.checkbox_sunday).isChecked
                }
                .setNegativeButton("Cancel") { dialog, which ->
                    // Handle negative button click or simply dismiss the dialog
                    dialog.dismiss()
                }
            builder.create().show()
        })

        adapterLocal = PickMealAdapter ({ meal -> // on click
        },{ meal -> // on pick
            val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_weekday_selection, null)
            val builder = AlertDialog.Builder(context)
            builder.setView(dialogView)
                .setPositiveButton("OK") { dialog, which ->
                    // Handle positive button click
                    // Get the selected weekdays from the checkboxes
                    val mondayChecked = dialogView.findViewById<CheckBox>(R.id.checkbox_monday).isChecked
                    if(mondayChecked)
                        planOverviewViewModel.addMealToDay(meal, Weekday.MONDAY)
                    val tuesdayChecked = dialogView.findViewById<CheckBox>(R.id.checkbox_tuesday).isChecked
                    if (tuesdayChecked)
                        planOverviewViewModel.addMealToDay(meal, Weekday.TUESDAY)
                    val wednesdayChecked = dialogView.findViewById<CheckBox>(R.id.checkbox_wednesday).isChecked
                    val thursdayChecked = dialogView.findViewById<CheckBox>(R.id.checkbox_thursday).isChecked
                    val fridayChecked = dialogView.findViewById<CheckBox>(R.id.checkbox_friday).isChecked
                    if (fridayChecked)
                        planOverviewViewModel.addMealToDay(meal, Weekday.FRIDAY)
                    val saturdayChecked = dialogView.findViewById<CheckBox>(R.id.checkbox_saturday).isChecked
                    val sundayChecked = dialogView.findViewById<CheckBox>(R.id.checkbox_sunday).isChecked
                }
                .setNegativeButton("Cancel") { dialog, which ->
                    // Handle negative button click or simply dismiss the dialog
                    dialog.dismiss()
                }
            builder.create().show()
        })

        binding.listRvOnline.adapter = adapterOnline
        binding.listRvLocal.adapter = adapterLocal

        if(mealPlanViewModel.localMealsFiltered.value is MealsState.Success) {
            adapterLocal.submitList((mealPlanViewModel.localMealsFiltered.value as MealsState.Success).meals)
        }

    }

    private fun initObservers() {

        binding.nameSearch.doAfterTextChanged {
            mealPlanViewModel.updateQuery(it.toString())
        }

        binding.switchButton.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                binding.switchButton.text = "Remote"
                binding.listRvLocal.visibility = View.GONE
                binding.listRvOnline.visibility = View.VISIBLE
            } else {
                binding.switchButton.text = "Local"
                binding.listRvLocal.visibility = View.VISIBLE
                binding.listRvOnline.visibility = View.GONE
            }
            if(isChecked) {
                mealPlanViewModel.updateMealType(MealType.ALLMEALS)
            } else {
                mealPlanViewModel.updateMealType(MealType.LOCALMEALS)
            }
        }

        mealPlanViewModel.allMealsFiltered.observe(viewLifecycleOwner, Observer {
            when(it) {
                is MealsState.Success -> {
                    Timber.e("acac event online success" + it.meals.size)
//                    showLoadingState(false)
                    adapterOnline.submitList(it.meals)
                }
                is MealsState.Error -> {
//                    showLoadingState(false)
                    adapterOnline.submitList(emptyList())
                }
                is MealsState.Loading -> {
//                    showLoadingState(true)
                }
            }
        })

        mealPlanViewModel.localMealsFiltered.observe(viewLifecycleOwner, Observer {
            when(it) {
                is MealsState.Success -> {
//                    showLoadingState(false)
                    adapterLocal.submitList(it.meals)
                }
                is MealsState.Error -> {
//                    showLoadingState(false)
                    adapterLocal.submitList(emptyList())
                }
                is MealsState.Loading -> {
//                    showLoadingState(true)
                }
            }
        })
    }

//    private fun showLoadingState(loading: Boolean) {
//        //TODO hide others
//
//        binding.loadingPb.isVisible = loading
//    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}