package com.example.nutritiontrackeravg.data.models

import com.example.nutritiontrackeravg.util.ParameterType

class Category(
    val idCategory: String,
    val strCategory: String,
    val strCategoryThumb: String,
    val strCategoryDescription: String,
    override val type: ParameterType = ParameterType.CATEGORY
): Parameter{
    override fun toString(): String {
        return strCategory
    }

    override fun areItemsTheSame(newItem: Parameter): Boolean {
        return newItem is Category && idCategory == newItem.idCategory
    }

    override fun areContentsTheSame(newItem: Parameter): Boolean {
        return newItem is Category && strCategory == newItem.strCategory
    }
}