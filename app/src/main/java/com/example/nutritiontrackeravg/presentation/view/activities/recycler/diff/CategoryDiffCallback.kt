package com.example.nutritiontrackeravg.presentation.view.activities.recycler.diff

import androidx.recyclerview.widget.DiffUtil
import com.example.nutritiontrackeravg.data.models.Category

class CategoryDiffCallback : DiffUtil.ItemCallback<Category>() {

    override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean {
        return oldItem.areItemsTheSame(newItem)
    }

    override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean {
        return false
//        return oldItem.name == newItem.name/* && oldItem.link == newItem.link*/
    }

}