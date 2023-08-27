package com.example.nutritiontrackeravg.presentation.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nutritiontrackeravg.R
import com.example.nutritiontrackeravg.databinding.FragmentFilterMealsBinding
import com.example.nutritiontrackeravg.presentation.view.activities.recycler.adapter.ParameterAdapter
import com.example.nutritiontrackeravg.presentation.view.states.ParameterState
import com.example.nutritiontrackeravg.presentation.view.states.ParametersState
import com.example.nutritiontrackeravg.presentation.view.viewmodels.ParameterViewModel
import com.example.nutritiontrackeravg.util.ParameterType
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import timber.log.Timber

class FilterFragment : Fragment(R.layout.fragment_filter_meals) {

    private val viewModel: ParameterViewModel by activityViewModel<ParameterViewModel>()
    private var _binding: FragmentFilterMealsBinding? = null

    private val binding get() = _binding!!

    private lateinit var adapter: ParameterAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFilterMealsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        initUi()
    }

    private fun initUi() {

        binding.removeFilterBtn.visibility = viewModel.selectedParameterState.value?.let {
            if (it is ParameterState.Empty) View.GONE else View.VISIBLE
        } ?: View.GONE

        binding.categoryFilterBtn.setOnClickListener {
            setupFetchView()
            Timber.e("fetching categories")
            viewModel.fetchAll(ParameterType.CATEGORY)
        }

        binding.ingredientFilterBtn.setOnClickListener {
            setupFetchView()
            Timber.e("fetching ingredients")
            viewModel.fetchAll(ParameterType.INGREDIENT)
        }

        binding.areaFilterBtn.setOnClickListener {
            setupFetchView()
            Timber.e("fetching areas")
            viewModel.fetchAll(ParameterType.AREA)
        }

        binding.removeFilterBtn.setOnClickListener {
            viewModel.selectedParameterState.value = ParameterState.Empty
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    private fun setupFetchView() {
        binding.filterBtns.visibility = View.GONE
        binding.listRv.visibility = View.VISIBLE
        initRecycler()
        initObservers()
    }

    private fun initObservers() {
        viewModel.parametersState.observe(viewLifecycleOwner, Observer {
            Timber.e("category state: $it")
            renderState(it);
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }



    private fun initRecycler() {
        binding.listRv.layoutManager = LinearLayoutManager(context)
        adapter = ParameterAdapter { parameter ->
            viewModel.selectedParameterState.value = ParameterState.Selected(parameter)
            requireActivity().supportFragmentManager.popBackStack()
        }
        binding.listRv.adapter = adapter
    }

    private fun renderState(state: ParametersState) {
        when (state) {
            is ParametersState.Success -> {
//                showLoadingState(false)
                adapter.submitList(state.parameters)
            }
            is ParametersState.Error -> {
//                showLoadingState(false)
                adapter.submitList(listOf())
            }
            is ParametersState.Loading -> {
//                showLoadingState(true)
            }
        }
    }

//    private fun showLoadingState(loading: Boolean) {
//        //TODO hide others
//
//        binding.loadingPb.isVisible = loading
//    }
}