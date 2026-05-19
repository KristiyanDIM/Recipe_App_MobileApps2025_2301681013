package com.example.recipeapp.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.recipeapp.data.Recipe
import com.example.recipeapp.databinding.ItemRecipeBinding
import com.example.recipeapp.R
import coil.load

class RecipeAdapter(
    private var recipes: List<Recipe> = emptyList(),   //Списък с рецептите
    private val onItemClick: (Recipe) -> Unit
) : RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder>() {

    class RecipeViewHolder(
        private val binding: ItemRecipeBinding,
        private val onItemClick: (Recipe) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(recipe: Recipe) {
            binding.tvTitle.text = recipe.title
            binding.tvIngredientsPreview.text = recipe.ingredients.take(50) + if (recipe.ingredients.length > 50) "..." else ""
            binding.tvInstructionsPreview.text = recipe.instructions.take(50) + if (recipe.instructions.length > 50) "..." else ""
            binding.ivRecipeImage.load(recipe.photoPath) {
                placeholder(R.drawable.ic_add_photo)
                error(R.drawable.ic_add_photo)
                crossfade(true)
            }

            binding.root.setOnClickListener {
                onItemClick(recipe)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val binding = ItemRecipeBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return RecipeViewHolder(binding, onItemClick)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        holder.bind(recipes[position])
    }

    override fun getItemCount() = recipes.size

    fun updateRecipes(newRecipes: List<Recipe>) {
        recipes = newRecipes
        notifyDataSetChanged()
    }
}
