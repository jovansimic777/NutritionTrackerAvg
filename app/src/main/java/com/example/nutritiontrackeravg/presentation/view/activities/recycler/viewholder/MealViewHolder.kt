package com.example.nutritiontrackeravg.presentation.view.activities.recycler.viewholder

import com.bumptech.glide.Glide
import androidx.recyclerview.widget.RecyclerView
import com.example.nutritiontrackeravg.data.models.MealSimple
import com.example.nutritiontrackeravg.databinding.LayoutItemMealBinding

class MealViewHolder(private val itemBinding: LayoutItemMealBinding) : RecyclerView.ViewHolder(itemBinding.root) {

    fun bind(meal: MealSimple) {
        itemBinding.titleTv.text = meal.name
        itemBinding.caloriesTv.text = meal.calValue.toString()

        Glide.with(itemView) // Use itemView as the context
            .load(meal.link)
            .into(itemBinding.imgView)

//        itemBinding.titleTv.setOnClickListener {
//            Toast.makeText(itemBinding.root.context, "Clicked on ${meal.name}", Toast.LENGTH_SHORT).show()
//        }
    }

}