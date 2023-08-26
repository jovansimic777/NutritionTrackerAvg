package com.example.nutritiontrackeravg.presentation.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.example.nutritiontrackeravg.R
import com.example.nutritiontrackeravg.databinding.FragmentMealPlanBinding
import com.example.nutritiontrackeravg.presentation.view.adapters.PagerAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MealPlanFragment : Fragment(R.layout.fragment_meal_plan) {

    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager2

    private var _binding: FragmentMealPlanBinding? = null

    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMealPlanBinding.inflate(inflater, container, false)

        tabLayout = binding.tabLayout
        viewPager = binding.viewPager

        viewPager.offscreenPageLimit = ViewPager2.OFFSCREEN_PAGE_LIMIT_DEFAULT
//
//        // Initialize the ViewPager2 adapter
        if (viewPager.adapter == null) {
            val adapter = PagerAdapter(this)
            viewPager.adapter = adapter

            // Connect the TabLayout with the ViewPager2
            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                tab.text = when (position) {
                    0 -> "Tab 1"
                    1 -> "Tab 2"
                    else -> ""
                }
            }.attach()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

//    class PagerAdapter2(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
//        override fun getItem(position: Int): Fragment {
//            return when (position) {
//                0 -> TabChooseMealsFragment()
//                1 -> TabPlanOverviewFragment()
//                else -> throw IllegalArgumentException("Invalid tab position")
//            }
//        }
//
//        override fun getCount(): Int {
//            return 2
//        }
//    }
}