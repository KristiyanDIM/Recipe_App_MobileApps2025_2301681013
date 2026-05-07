package com.example.recipeapp.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.recipeapp.data.AppDatabase
import com.example.recipeapp.data.Recipe
import com.example.recipeapp.repository.RecipeRepository
import kotlinx.coroutines.launch

class RecipeViewModel(app: Application) : AndroidViewModel(app) {
    private val repository: RecipeRepository
    val allRecipes: LiveData<List<Recipe>>

    init {
        val dao = AppDatabase.get(app).recipeDao()
        repository = RecipeRepository(dao)
        allRecipes = repository.allRecipes.asLiveData()
    }

    fun addRecipe(recipe: Recipe) = viewModelScope.launch {
        repository.insert(recipe)
    }
    fun updateRecipe(recipe: Recipe) = viewModelScope.launch {
        repository.update(recipe)
    }
    fun deleteRecipe(recipe: Recipe) = viewModelScope.launch {
        repository.delete(recipe)
    }
}