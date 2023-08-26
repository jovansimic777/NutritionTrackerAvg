package com.example.nutritiontrackeravg.presentation.view.activities.recycler.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.nutritiontrackeravg.data.models.Category
import com.example.nutritiontrackeravg.data.models.Parameter
import com.example.nutritiontrackeravg.databinding.LayoutItemParameterBinding

class ParameterViewHolder (private val itemBinding: LayoutItemParameterBinding) : RecyclerView.ViewHolder(itemBinding.root) {

    fun bind(parameter: Parameter) {
        itemBinding.titleTv.text = parameter.toString()
        if(parameter is Category) {
            Glide.with(itemView) // Use itemView as the context
                .load((parameter as Category).strCategoryThumb)
                .into(itemBinding.imgView)
        }
    }

}