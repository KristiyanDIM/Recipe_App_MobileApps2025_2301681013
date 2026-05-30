package com.example.recipeapp.repository

import com.example.recipeapp.data.Recipe
import com.example.recipeapp.data.RecipeDao
import kotlinx.coroutines.flow.Flow

class RecipeRepository(private val dao: RecipeDao) {

    // Всички рецепти - автоматично се обновява при промяна
    val allRecipes: Flow<List<Recipe>> = dao.getAll()

    // Добавя рецепта (асинхронно)
    suspend fun insert(recipe: Recipe) {
        dao.insert(recipe)
    }

    // Изтрива рецепта
    suspend fun delete(recipe: Recipe) {
        dao.delete(recipe)
    }

    // Обновява рецепта
    suspend fun update(recipe: Recipe) {
        dao.update(recipe)
    }

    // Взима една рецепта по ID
    suspend fun getRecipeById(id: Int): Recipe? {
        return dao.getRecipeById(id)
    }
}