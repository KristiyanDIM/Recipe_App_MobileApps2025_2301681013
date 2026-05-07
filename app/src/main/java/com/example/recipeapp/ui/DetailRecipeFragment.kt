package com.example.recipeapp.ui

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.recipeapp.R
import com.example.recipeapp.data.Recipe
import com.example.recipeapp.viewmodel.RecipeViewModel

class DetailRecipeFragment : Fragment(R.layout.fragment_detail_recipe) {

    private lateinit var viewModel: RecipeViewModel
    private var currentRecipe: Recipe? = null
    private var isEditMode = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[RecipeViewModel::class.java]

        // Вземане на ID на рецептата от аргументите
        val recipeId = arguments?.getInt("recipe_id") ?: 0

        // Зареждане на рецептата
        loadRecipe(recipeId)

        setupButtons()
    }

    private fun loadRecipe(recipeId: Int) {
        viewModel.getRecipeById(recipeId).observe(viewLifecycleOwner) { recipe ->
            if (recipe != null) {
                currentRecipe = recipe
                displayRecipe(recipe)
            } else {
                Toast.makeText(requireContext(), "Рецептата не беше намерена", Toast.LENGTH_SHORT).show()
                findNavController().navigateUp()
            }
        }
    }

    private fun displayRecipe(recipe: Recipe) {
        val etTitle = requireView().findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.etDetailTitle)
        val etIngredients = requireView().findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.etDetailIngredients)
        val etInstructions = requireView().findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.etDetailInstructions)

        etTitle.setText(recipe.title)
        etIngredients.setText(recipe.ingredients)
        etInstructions.setText(recipe.instructions)

        // Първоначално полетата са само за четене
        setFieldsEditable(false)
    }

    private fun setupButtons() {
        val btnEdit = requireView().findViewById<Button>(R.id.btnEdit)
        val btnSaveEdit = requireView().findViewById<Button>(R.id.btnSaveEdit)
        val btnDelete = requireView().findViewById<Button>(R.id.btnDelete)

        btnEdit.setOnClickListener {
            enableEditMode(true)
        }

        btnSaveEdit.setOnClickListener {
            saveChanges()
        }

        btnDelete.setOnClickListener {
            confirmDelete()
        }
    }

    private fun enableEditMode(enabled: Boolean) {
        isEditMode = enabled
        setFieldsEditable(enabled)

        val btnEdit = requireView().findViewById<Button>(R.id.btnEdit)
        val btnSaveEdit = requireView().findViewById<Button>(R.id.btnSaveEdit)

        btnEdit.visibility = if (enabled) View.GONE else View.VISIBLE
        btnSaveEdit.visibility = if (enabled) View.VISIBLE else View.GONE
    }

    private fun setFieldsEditable(editable: Boolean) {
        val etTitle = requireView().findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.etDetailTitle)
        val etIngredients = requireView().findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.etDetailIngredients)
        val etInstructions = requireView().findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.etDetailInstructions)

        etTitle.isEnabled = editable
        etIngredients.isEnabled = editable
        etInstructions.isEnabled = editable
    }

    private fun saveChanges() {
        val etTitle = requireView().findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.etDetailTitle)
        val etIngredients = requireView().findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.etDetailIngredients)
        val etInstructions = requireView().findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.etDetailInstructions)

        currentRecipe?.let { original ->
            val updatedRecipe = original.copy(
                title = etTitle.text.toString().trim(),
                ingredients = etIngredients.text.toString().trim(),
                instructions = etInstructions.text.toString().trim()
            )

            // Валидация
            if (updatedRecipe.title.isEmpty()) {
                etTitle.error = "Заглавието е задължително"
                return
            }

            if (updatedRecipe.ingredients.isEmpty()) {
                etIngredients.error = "Съставките са задължителни"
                return
            }

            if (updatedRecipe.instructions.isEmpty()) {
                etInstructions.error = "Инструкциите са задължителни"
                return
            }

            try {
                viewModel.updateRecipe(updatedRecipe)
                Toast.makeText(requireContext(), "Рецептата е обновена", Toast.LENGTH_SHORT).show()
                currentRecipe = updatedRecipe
                enableEditMode(false)
                displayRecipe(updatedRecipe)
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Грешка при обновяване", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun confirmDelete() {
        AlertDialog.Builder(requireContext())
            .setTitle("Изтриване на рецепта")
            .setMessage("Сигурни ли сте, че искате да изтриете \"${currentRecipe?.title}\"?")
            .setPositiveButton("Да") { _, _ ->
                deleteRecipe()
            }
            .setNegativeButton("Не", null)
            .show()
    }

    private fun deleteRecipe() {
        currentRecipe?.let {
            try {
                viewModel.deleteRecipe(it)
                Toast.makeText(requireContext(), "Рецептата е изтрита", Toast.LENGTH_SHORT).show()
                findNavController().navigateUp()
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Грешка при изтриване", Toast.LENGTH_SHORT).show()
            }
        }
    }
}