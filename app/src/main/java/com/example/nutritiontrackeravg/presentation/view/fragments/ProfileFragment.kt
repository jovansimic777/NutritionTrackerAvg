package com.example.nutritiontrackeravg.presentation.view.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.nutritiontrackeravg.R
import com.example.nutritiontrackeravg.databinding.FragmentProfileBinding
import com.example.nutritiontrackeravg.presentation.contract.MealDetaildContract
import com.example.nutritiontrackeravg.presentation.view.viewmodels.MealDetailedViewModel
import com.example.nutritiontrackeravg.util.Constants
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class ProfileFragment : Fragment(R.layout.fragment_profile) {
    private val mealDetailedVM: MealDetaildContract.ViewModel by activityViewModel<MealDetailedViewModel>()

    private var _binding: FragmentProfileBinding? = null

    private val binding get() = _binding!!

    private var testBT: Button? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
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

        val sharedPreferences = requireActivity().getSharedPreferences(
            requireActivity().packageName, Context.MODE_PRIVATE
        )

        binding.emailTv.text = sharedPreferences.getString(Constants.EMAIL, "")

        binding.logoutButton.setOnClickListener(View.OnClickListener { v: View? ->

            val editor = sharedPreferences.edit()
            editor.remove(Constants.EMAIL)
            editor.remove(Constants.IS_LOGGED_IN)
            editor.apply()
            requireActivity().finish()
        })

        binding.heightTv.visibility = View.GONE
        binding.weightTv.visibility = View.GONE
        binding.ageTv.visibility = View.GONE
        binding.changeStatsButton.visibility = View.VISIBLE
        binding.updateStatsButton.visibility = View.GONE
        binding.giveUpButton.visibility = View.GONE

        binding.changeStatsButton.setOnClickListener(View.OnClickListener { v: View? ->
            binding.heightTv.visibility = View.VISIBLE
            binding.weightTv.visibility = View.VISIBLE
            binding.ageTv.visibility = View.VISIBLE
            binding.changeStatsButton.visibility = View.GONE
            binding.updateStatsButton.visibility = View.VISIBLE
            binding.giveUpButton.visibility = View.VISIBLE
        })

        binding.giveUpButton.setOnClickListener(View.OnClickListener { v: View? ->
            binding.heightTv.visibility = View.GONE
            binding.weightTv.visibility = View.GONE
            binding.ageTv.visibility = View.GONE
            binding.changeStatsButton.visibility = View.VISIBLE
            binding.updateStatsButton.visibility = View.GONE
            binding.giveUpButton.visibility = View.GONE
        })

        binding.updateStatsButton.setOnClickListener(View.OnClickListener { v: View? ->
            val height = binding.heightTv.text.toString()
            val weight = binding.weightTv.text.toString()
            val age = binding.ageTv.text.toString()
            if(height.isEmpty() || weight.isEmpty() || age.isEmpty()) {
                return@OnClickListener
            }

//            val sharedPreferences = requireContext().getSharedPreferences(requireActivity().packageName, Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putFloat(Constants.DAILY_CALORIES, height.toFloat()*6.25f+10f*weight.toFloat()-5*age.toFloat()+5)
            editor.apply()

            binding.heightTv.visibility = View.GONE
            binding.weightTv.visibility = View.GONE
            binding.ageTv.visibility = View.GONE
            binding.changeStatsButton.visibility = View.VISIBLE
            binding.updateStatsButton.visibility = View.GONE
            binding.giveUpButton.visibility = View.GONE
        })

    }

    private fun initObservers() {

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}