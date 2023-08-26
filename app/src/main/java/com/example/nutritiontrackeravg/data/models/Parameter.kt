package com.example.nutritiontrackeravg.data.models

import com.example.nutritiontrackeravg.util.ParameterType

interface Parameter{
    val type: ParameterType
    override fun toString(): String
    fun areItemsTheSame(newItem: Parameter): Boolean
    fun areContentsTheSame(newItem: Parameter): Boolean
}