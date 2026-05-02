package com.example.recipeapp.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.recipeapp.R
import com.example.recipeapp.data.Recipe

class RecipeAdapter : ListAdapter<Recipe, RecipeAdapter.RecipeViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_recipe, parent, false)
        return RecipeViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class RecipeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvTitle: TextView = itemView.findViewById(R.id.tvRecipeTitle)
        fun bind(recipe: Recipe) {
            tvTitle.text = recipe.title
        }
    }

    object DiffCallback : DiffUtil.ItemCallback<Recipe>() {
        override fun areItemsTheSame(old: Recipe, new: Recipe) = old.id == new.id
        override fun areContentsTheSame(old: Recipe, new: Recipe) = old == new
    }
}