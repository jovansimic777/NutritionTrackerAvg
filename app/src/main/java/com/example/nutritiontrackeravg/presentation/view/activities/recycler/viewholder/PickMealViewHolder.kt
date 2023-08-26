package com.example.nutritiontrackeravg.presentation.view.activities.recycler.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.nutritiontrackeravg.data.models.MealDto
import com.example.nutritiontrackeravg.databinding.LayoutItemPickMealBinding
import java.io.File

class PickMealViewHolder(private val itemBinding: LayoutItemPickMealBinding) : RecyclerView.ViewHolder(itemBinding.root) {

    fun bind(meal: MealDto) {
        itemBinding.titleTv.text = meal.name
        if(meal.calValue == -1.0) {
            itemBinding.caloriesTv.text = "Not available"
        }
        else
            itemBinding.caloriesTv.text = meal.calValue.toString()
//        Timber.e("link do slike:" + meal.link)

        if(meal.img!!.contains("storage")) {
            Glide.with(itemView)
                .load(File(meal.img))
                .into(itemBinding.imgView)
        }
        else {
            Glide.with(itemView) // Use itemView as the context
                .load(meal.img)
                .into(itemBinding.imgView)
        }
        itemBinding.btnPick.setOnClickListener {
//            Timber.e("pick button clicked")
        }
    }
    fun bindPickButton(meal: MealDto, onPickClick: (MealDto) -> Unit) {
        itemBinding.btnPick.setOnClickListener {
            onPickClick(meal)
        }
    }
}