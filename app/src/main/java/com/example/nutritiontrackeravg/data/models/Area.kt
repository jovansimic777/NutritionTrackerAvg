package com.example.nutritiontrackeravg.data.models

import com.example.nutritiontrackeravg.util.ParameterType

class Area(
    val strArea: String,
    override val type: ParameterType = ParameterType.AREA
): Parameter{
    override fun toString(): String {
        return strArea
    }

    override fun areItemsTheSame(newItem: Parameter): Boolean {
        return newItem is Area && strArea == newItem.strArea
    }

    override fun areContentsTheSame(newItem: Parameter): Boolean {
        return newItem is Area && strArea == newItem.strArea
    }
}