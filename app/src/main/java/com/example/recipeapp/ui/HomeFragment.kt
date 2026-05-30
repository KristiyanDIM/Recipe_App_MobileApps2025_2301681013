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

// HomeFragment – началният екран, показва списък с всички рецепти
class HomeFragment : Fragment(R.layout.fragment_home) {

    // Променливи (ще се използват по-късно в onViewCreated)
    private lateinit var viewModel: RecipeViewModel //Данни
    private lateinit var rvRecipes: RecyclerView // Списък с рецепти
    private lateinit var tvEmpty: TextView // Ако нямаме рецепти, покажи това
    private lateinit var adapter: RecipeAdapter // Рецепта


    // Когато екранът се създаде
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Свързва променливите с елементите от екрана
        rvRecipes = view.findViewById(R.id.rvRecipes)
        tvEmpty = view.findViewById(R.id.tvEmpty)

        // Дава се достъп до данните (ViewModel)
        viewModel = ViewModelProvider(requireActivity())[RecipeViewModel::class.java]

        // Създава се адаптер
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