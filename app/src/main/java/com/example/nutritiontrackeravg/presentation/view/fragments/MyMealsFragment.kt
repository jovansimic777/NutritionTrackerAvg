package com.example.nutritiontrackeravg.presentation.view.fragments

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nutritiontrackeravg.R
import com.example.nutritiontrackeravg.databinding.FragmentMyMealsBinding
import com.example.nutritiontrackeravg.presentation.contract.MealEntityContract
import com.example.nutritiontrackeravg.presentation.view.activities.recycler.adapter.SavedMealAdapter
import com.example.nutritiontrackeravg.presentation.view.states.MealsState
import com.example.nutritiontrackeravg.presentation.view.viewmodels.MealEntityViewModel
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import timber.log.Timber

class MyMealsFragment : Fragment(R.layout.fragment_my_meals) {


    private val mealEntityViewModel: MealEntityContract.ViewModel by activityViewModel<MealEntityViewModel>()

    private var _binding: FragmentMyMealsBinding? = null

    private val binding get() = _binding!!

    private lateinit var adapter: SavedMealAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMyMealsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        initUi()
        initObservers()

        mealEntityViewModel.getAllMeals()
    }

    private fun initUi() {
        initRecycler()
    }

    private fun initRecycler() {
        binding.listRvSaved.layoutManager = LinearLayoutManager(context)

        adapter = SavedMealAdapter ({ meal -> // on click
            mealEntityViewModel.getMealById(meal.id)
        },{ meal -> // on delete
            val dialogClickListener =
                DialogInterface.OnClickListener { dialog, which ->
                    when (which) {
                        DialogInterface.BUTTON_POSITIVE -> {
                            mealEntityViewModel.deleteMealFromDB(meal.id)
//                            recyclerViewModel.deleteTask(task.getId())
//                            refreshView()
                        }

                        DialogInterface.BUTTON_NEGATIVE -> {}
                    }
                }

            val builder = AlertDialog.Builder(
                context
            )
            builder.setMessage(requireContext().getString(R.string.are_you_sure)).setPositiveButton(
                requireContext().getString(R.string.yes), dialogClickListener
            )
                .setNegativeButton(requireContext().getString(R.string.no), dialogClickListener).show()
        })

        binding.listRvSaved.adapter = adapter
    }

    private fun initObservers() {
        mealEntityViewModel.allMeals.observe(viewLifecycleOwner, Observer {
            Timber.d("observed all meals: $it")
            when(it) {
                is MealsState.Success -> {
//                    showLoadingState(false)
                    adapter.submitList(it.meals)
                }
                is MealsState.Error -> {
//                    showLoadingState(false)
                    adapter.submitList(emptyList())
                }
                is MealsState.Loading -> {
//                    showLoadingState(true)
//                    adapter.submitList(emptyList())
                }
            }
        })
    }

//    private fun showLoadingState(loading: Boolean) {
//        //TODO hide others
//
//        binding.loadingPbSaved.isVisible = loading
//    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}