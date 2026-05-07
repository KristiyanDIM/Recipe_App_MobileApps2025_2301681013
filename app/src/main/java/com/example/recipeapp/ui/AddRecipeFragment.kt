package com.example.recipeapp.ui

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.recipeapp.R
import com.example.recipeapp.data.Recipe
import com.example.recipeapp.viewmodel.RecipeViewModel

class AddRecipeFragment : Fragment(R.layout.fragment_add_recipe) {

    private lateinit var viewModel: RecipeViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val etTitle = view.findViewById<EditText>(R.id.etTitle)
        val etIngredients = view.findViewById<EditText>(R.id.etIngredients)
        val etInstructions = view.findViewById<EditText>(R.id.etInstructions)
        val btnSave = view.findViewById<Button>(R.id.btnSave)

        viewModel = ViewModelProvider(this)[RecipeViewModel::class.java]

        btnSave.setOnClickListener {

            val recipe = Recipe(
                title = etTitle.text.toString(),
                ingredients = etIngredients.text.toString(),
                instructions = etInstructions.text.toString(),
                photoPath = null
            )

            viewModel.addRecipe(recipe)
        }
    }
}