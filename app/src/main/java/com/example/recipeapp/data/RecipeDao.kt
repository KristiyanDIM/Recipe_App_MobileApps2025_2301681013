package com.example.recipeapp.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(recipe: Recipe)
    @Update
    suspend fun update(recipe: Recipe)
    @Delete
    suspend fun delete(recipe: Recipe)

    @Query("SELECT * FROM recipes ORDER BY title ASC")
    fun getAll(): Flow<List<Recipe>>
}