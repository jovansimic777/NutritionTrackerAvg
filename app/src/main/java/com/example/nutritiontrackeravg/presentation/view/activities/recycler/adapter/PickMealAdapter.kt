package com.example.nutritiontrackeravg.presentation.view.activities.recycler.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.nutritiontrackeravg.data.models.MealDto
import com.example.nutritiontrackeravg.databinding.LayoutItemPickMealBinding
import com.example.nutritiontrackeravg.presentation.view.activities.recycler.diff.MealDtoDiffCallback
import com.example.nutritiontrackeravg.presentation.view.activities.recycler.viewholder.PickMealViewHolder

class PickMealAdapter(private val onItemClick: (MealDto) -> Unit, private val onPickClick: (MealDto) -> Unit) : ListAdapter<MealDto, PickMealViewHolder>(
    MealDtoDiffCallback()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PickMealViewHolder {
        val itemBinding = LayoutItemPickMealBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PickMealViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: PickMealViewHolder, position: Int) {
        val meal = getItem(position)
        holder.bind(meal)

        holder.itemView.setOnClickListener {
            onItemClick(meal)
        }

        holder.bindPickButton(meal, onPickClick)
    }

}