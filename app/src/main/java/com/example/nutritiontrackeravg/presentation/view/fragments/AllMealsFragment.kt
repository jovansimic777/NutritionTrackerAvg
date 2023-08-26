package com.example.nutritiontrackeravg.presentation.view.fragments

import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import com.example.nutritiontrackeravg.R
import com.example.nutritiontrackeravg.databinding.FragmentAllMealsBinding
import com.example.nutritiontrackeravg.presentation.contract.MealsContract
import com.example.nutritiontrackeravg.presentation.view.viewmodels.AllMealsViewModel
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nutritiontrackeravg.presentation.view.activities.MainActivity
import com.example.nutritiontrackeravg.presentation.view.activities.recycler.adapter.MealAdapter
import com.example.nutritiontrackeravg.presentation.view.activities.recycler.adapter.ParameterAdapter
import com.example.nutritiontrackeravg.presentation.view.states.MealsApiState
import com.example.nutritiontrackeravg.presentation.view.states.ParameterState
import com.example.nutritiontrackeravg.presentation.view.states.ParametersState
import com.example.nutritiontrackeravg.presentation.view.viewmodels.MealDetailedViewModel
import com.example.nutritiontrackeravg.presentation.view.viewmodels.ParameterViewModel
import com.example.nutritiontrackeravg.util.ParameterType
import com.example.nutritiontrackeravg.util.SortType
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import timber.log.Timber
import kotlin.math.min
import kotlin.math.roundToInt

class AllMealsFragment : Fragment(R.layout.fragment_all_meals) {

    private var _binding: FragmentAllMealsBinding? = null
    private val mealsViewModel: MealsContract.ViewModel by activityViewModel<AllMealsViewModel>()
    private val parameterViewModel: ParameterViewModel by activityViewModel<ParameterViewModel>()
    private val mealDetailedViewModel: MealDetailedViewModel by activityViewModel<MealDetailedViewModel>()
    private val sortList = arrayOf(SortType.NONE, SortType.ABC, SortType.CALORIES)
    private val paginationList = arrayOf(10, 10, 20, 50);
    private val binding get() = _binding!!

    private lateinit var adapter: MealAdapter
    private lateinit var categoryAdapter: ParameterAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAllMealsBinding.inflate(inflater, container, false)
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

        binding.nameSearch.text = mealsViewModel.queryString.value!!.toEditable()
        binding.tagSearch.text = mealsViewModel.tagQuery.value!!.toEditable()

        if (mealsViewModel.initialRender.value == true) {
            mealsViewModel.setInitialRender(false)
            binding.listRvCategories.visibility = View.VISIBLE
            binding.listRv.visibility = View.GONE
        } else {
            if(mealsViewModel.loadedMealsData.value == true) {
                binding.listRvCategories.visibility = View.GONE
                binding.listRv.visibility = View.VISIBLE
            } else {
                binding.listRvCategories.visibility = View.VISIBLE
                binding.listRv.visibility = View.GONE
            }
        }


        initRecyclers()
        initListeners()
    }

    fun String.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)

    private fun initRecyclers() {

//        binding.listRvCategories.visibility = View.VISIBLE
//        binding.listRv.visibility = View.GONE

        binding.listRv.layoutManager = LinearLayoutManager(context)

        adapter = MealAdapter { meal ->
            mealDetailedViewModel.fetchMealById(meal.id, meal.calValue.roundToInt().toString())
        }
        binding.listRv.adapter = adapter



        binding.listRvCategories.layoutManager= LinearLayoutManager(context)

        categoryAdapter = ParameterAdapter { parameter ->
            parameterViewModel.selectedParameterState.value = ParameterState.Selected(parameter)
            binding.listRvCategories.visibility = View.GONE
            binding.listRv.visibility = View.VISIBLE
            mealsViewModel.setLoadedMealsData(true)
        }

        binding.listRvCategories.adapter = categoryAdapter

        parameterViewModel.fetchAll(ParameterType.CATEGORY)
    }

    private var pageSize = 10
    private var currentPage = 0


    private fun initListeners() {
        binding.nameSearch.doAfterTextChanged {
            mealsViewModel.updateSearchQuery(it.toString())
        }

        binding.tagSearch.doAfterTextChanged {
            mealsViewModel.setTag(it.toString())
        }

        binding.filterBtn.setOnClickListener {
            (activity as MainActivity?)?.addFragmentHide(FilterFragment())
        }

        val sortArray = resources.getStringArray(R.array.sort_array)

        ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            sortArray
        ).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.sortSpinner.adapter = this
            binding.sortSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    val selectedItem = getItem(position)
                    mealsViewModel.setSort(sortList[position])
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    // Handle the case where no item is selected (optional)
                }
            }
        }

        val paginationArray = resources.getStringArray(R.array.pagination_array)

        ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            paginationArray
        ).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.pageSpinner.adapter = this
            binding.pageSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    currentPage = 0
                    pageSize = paginationList[position]
                    renderAdapter()
//                    mealsViewModel.setSort(sortList[position])
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    // Handle the case where no item is selected (optional)
                }
            }
        }

        binding.backwardBtn.setOnClickListener {
            currentPage--
            renderAdapter()
        }

        binding.forwardBtn.setOnClickListener {
            currentPage++
            renderAdapter()
        }
    }

    private fun initObservers() {
        mealsViewModel.mealsState.observe(viewLifecycleOwner, Observer {
            if (it is MealsApiState.Success && !it.meals.isEmpty() && binding.listRvCategories.visibility == View.VISIBLE) {
                binding.listRvCategories.visibility = View.GONE
                binding.listRv.visibility = View.VISIBLE
                mealsViewModel.setLoadedMealsData(true)
            }
//            if(binding.listRvCategories.visibility == View.VISIBLE) {
//
//            }
            Timber.e("desilo se")
            currentPage = 0
            renderState(it)
        })

        // TODO possibly remove the observer when gone
        parameterViewModel.parametersState.observe(viewLifecycleOwner, Observer {
                if(binding.listRvCategories.visibility == View.VISIBLE) {
                    when (it) {
                        is ParametersState.Success -> {
                            categoryAdapter.submitList(it.parameters)
                        }
                        is ParametersState.Error -> {
                            categoryAdapter.submitList(listOf())
                        }
                        is ParametersState.Loading -> {
                            categoryAdapter.submitList(listOf())
                        }
                    }
                }
        })


        parameterViewModel.selectedParameterState.observe(viewLifecycleOwner, Observer {
            if(it is ParameterState.Selected) {
                mealsViewModel.setFilter(it.parameter)
            }
            else
                mealsViewModel.setFilter(null)
        })

    }

    private fun renderState(state: MealsApiState) {
        binding.pageNumberTV.text = (currentPage+1).toString()
        when (state) {
            is MealsApiState.Success -> {
                showLoadingState(false)
//                adapter.submitList(state.meals)
                renderAdapter()
            }
            is MealsApiState.Error -> {
                showLoadingState(false)
//                renderAdapter()
                adapter.submitList(listOf())
                binding.forwardBtn.isEnabled = false
                binding.backwardBtn.isEnabled = false
            }
            is MealsApiState.Loading -> {
                showLoadingState(true)
            }
        }
    }

    private fun renderAdapter() {
        binding.pageNumberTV.text = (currentPage+1).toString()
        var state: MealsApiState = mealsViewModel.mealsState.value!!

        if(state is MealsApiState.Success) {
            val totalNum = state.meals.size
            binding.forwardBtn.isEnabled = (currentPage+1)*pageSize < totalNum
            binding.backwardBtn.isEnabled = currentPage > 0
            if(state.meals.size == 0) {
                adapter.submitList(listOf())
                return
            }
            val minIndex = currentPage * pageSize
            val maxIndex = min((currentPage + 1) * pageSize, state.meals.size)
            adapter.submitList(
                state.meals.subList(
                    minIndex,
                    maxIndex
                )
            )
        }
        else
            adapter.submitList(listOf())
    }

    private fun showLoadingState(loading: Boolean) {
        //TODO hide others

        binding.loadingPb.isVisible = loading
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

//    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
//        mealsViewModel.setSort(sortList[position])
//    }
//
//    override fun onNothingSelected(p0: AdapterView<*>?) {
//        TODO("Not yet implemented")
//    }
}