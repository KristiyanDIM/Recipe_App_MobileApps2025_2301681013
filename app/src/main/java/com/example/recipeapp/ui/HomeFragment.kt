package com.example.recipeapp.ui

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.recipeapp.R
import com.example.recipeapp.viewmodel.RecipeViewModel
import androidx.navigation.fragment.findNavController

class HomeFragment : Fragment(R.layout.fragment_home) {

    private lateinit var viewModel: RecipeViewModel
    private lateinit var rvRecipes: RecyclerView
    private lateinit var tvEmpty: TextView
    private lateinit var adapter: RecipeAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvRecipes = view.findViewById(R.id.rvRecipes)
        tvEmpty = view.findViewById(R.id.tvEmpty)

        viewModel = ViewModelProvider(requireActivity())[RecipeViewModel::class.java]

        adapter = RecipeAdapter { recipe ->
            // Навигация до DetailFragment с ID на рецептата
            val bundle = Bundle().apply {
                putInt("recipe_id", recipe.id)
            }
            findNavController().navigate(R.id.detailRecipeFragment, bundle)
        }

        rvRecipes.adapter = adapter

        // Наблюдаване на промените в базата данни
        viewModel.allRecipes.observe(viewLifecycleOwner) { recipes ->
            adapter.updateRecipes(recipes)

            // Ако няма рецепти, покажи празно съобщение
            if (recipes.isEmpty()) {
                rvRecipes.visibility = View.GONE
                tvEmpty.visibility = View.VISIBLE
            } else {
                rvRecipes.visibility = View.VISIBLE
                tvEmpty.visibility = View.GONE
            }
        }
    }
}