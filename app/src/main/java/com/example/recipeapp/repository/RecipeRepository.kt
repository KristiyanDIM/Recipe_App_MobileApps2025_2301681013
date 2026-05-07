package com.example.recipeapp.repository

import com.example.recipeapp.data.Recipe
import com.example.recipeapp.data.RecipeDao
import kotlinx.coroutines.flow.Flow

class RecipeRepository(private val dao: RecipeDao) {

    val allRecipes: Flow<List<Recipe>> = dao.getAll()

    suspend fun insert(recipe: Recipe) {
        dao.insert(recipe)
    }

    suspend fun delete(recipe: Recipe) {
        dao.delete(recipe)
    }

    suspend fun update(recipe: Recipe) {
        dao.update(recipe)
    }
}