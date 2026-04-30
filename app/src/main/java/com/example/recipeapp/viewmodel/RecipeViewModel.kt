package com.example.recipeapp.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.recipeapp.data.AppDatabase
import com.example.recipeapp.data.Recipe
import kotlinx.coroutines.launch

class RecipeViewModel(app: Application) : AndroidViewModel(app) {
    private val dao = AppDatabase.get(app).recipeDao()
    val allRecipes: LiveData<List<Recipe>> = dao.getAll().asLiveData()

    fun addRecipe(recipe: Recipe) = viewModelScope.launch {
        dao.insert(recipe)
    }

    fun deleteRecipe(recipe: Recipe) = viewModelScope.launch {
        dao.delete(recipe)
    }
}