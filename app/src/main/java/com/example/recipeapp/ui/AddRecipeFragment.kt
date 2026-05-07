package com.example.recipeapp.ui

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.recipeapp.R
import com.example.recipeapp.data.Recipe
import com.example.recipeapp.viewmodel.RecipeViewModel

class AddRecipeFragment : Fragment(R.layout.fragment_add_recipe) {

    private lateinit var viewModel: RecipeViewModel
    private lateinit var etTitle: EditText
    private lateinit var etIngredients: EditText
    private lateinit var etInstructions: EditText
    private lateinit var btnSave: Button

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        etTitle = view.findViewById(R.id.etTitle)
        etIngredients = view.findViewById(R.id.etIngredients)
        etInstructions = view.findViewById(R.id.etInstructions)
        btnSave = view.findViewById(R.id.btnSave)

        viewModel = ViewModelProvider(this)[RecipeViewModel::class.java]

        btnSave.setOnClickListener {
            saveRecipe()
        }
    }

    private fun saveRecipe() {
        val title = etTitle.text.toString().trim()
        val ingredients = etIngredients.text.toString().trim()
        val instructions = etInstructions.text.toString().trim()

        // Валидация - проверка дали полетата не са празни
        when {
            title.isEmpty() -> {
                etTitle.error = "Моля, въведете заглавие"
                etTitle.requestFocus()
                return
            }
            ingredients.isEmpty() -> {
                etIngredients.error = "Моля, въведете съставки"
                etIngredients.requestFocus()
                return
            }
            instructions.isEmpty() -> {
                etInstructions.error = "Моля, въведете инструкции"
                etInstructions.requestFocus()
                return
            }
        }

        // Деактивиране на бутона за предотвратяване на множество кликвания
        btnSave.isEnabled = false

        val recipe = Recipe(
            title = title,
            ingredients = ingredients,
            instructions = instructions,
            photoPath = null
        )

        try {
            viewModel.addRecipe(recipe)
            Toast.makeText(requireContext(), "Рецептата е добавена успешно!", Toast.LENGTH_SHORT).show()

            // Изчистване на полетата (опционално)
            clearFields()

            // Навигация обратно към Home
            findNavController().navigateUp()

        } catch (e: Exception) {
            Toast.makeText(requireContext(), "Грешка: ${e.message}", Toast.LENGTH_SHORT).show()
            btnSave.isEnabled = true
        }
    }

    private fun clearFields() {
        etTitle.text.clear()
        etIngredients.text.clear()
        etInstructions.text.clear()
    }
}