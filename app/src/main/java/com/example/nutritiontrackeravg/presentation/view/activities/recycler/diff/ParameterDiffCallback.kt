package com.example.nutritiontrackeravg.presentation.view.activities.recycler.diff

import androidx.recyclerview.widget.DiffUtil
import com.example.nutritiontrackeravg.data.models.Parameter

class ParameterDiffCallback: DiffUtil.ItemCallback<Parameter>() {

    override fun areItemsTheSame(oldItem: Parameter, newItem: Parameter): Boolean {
        return oldItem.areItemsTheSame(newItem)
    }

    override fun areContentsTheSame(oldItem: Parameter, newItem: Parameter): Boolean {
        return oldItem.areContentsTheSame(newItem)
    }

}