package com.example.recipeapp.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.recipeapp.data.AppDatabase
import com.example.recipeapp.data.Recipe
import com.example.recipeapp.repository.RecipeRepository
import kotlinx.coroutines.launch

// свързва UI (фрагментите) с данните (Repository)
class RecipeViewModel(app: Application) : AndroidViewModel(app) {
    private val repository: RecipeRepository
    val allRecipes: LiveData<List<Recipe>> // Списък за наблюдаване от HomeFragment

    // init – изпълнява се веднъж, когато ViewModel се създаде
    init {
        val dao = AppDatabase.get(app).recipeDao()
        repository = RecipeRepository(dao)
        allRecipes = repository.allRecipes.asLiveData()
    }

    // Добавя рецепта
    fun addRecipe(recipe: Recipe) = viewModelScope.launch {
        repository.insert(recipe)
    }

    // Обновява рецепта
    fun updateRecipe(recipe: Recipe) = viewModelScope.launch {
        repository.update(recipe)
    }

    // Изтрива рецепта
    fun deleteRecipe(recipe: Recipe) = viewModelScope.launch {
        repository.delete(recipe)
    }

    // Взима една рецепта по ID
    fun getRecipeById(id: Int): LiveData<Recipe?> = liveData {
        emit(repository.getRecipeById(id))
    }
}