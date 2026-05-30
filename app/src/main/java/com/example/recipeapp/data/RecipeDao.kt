package com.example.recipeapp.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipeDao {

    // @Insert – вмъква нова рецепта
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(recipe: Recipe)

    // @Update – обновява съществуваща рецепта
    @Update
    suspend fun update(recipe: Recipe)

    // @Delete – изтрива рецепта
    @Delete
    suspend fun delete(recipe: Recipe)

    @Query("SELECT * FROM recipes ORDER BY title ASC")
    fun getAll(): Flow<List<Recipe>> // ако добавим нова рецепта, списъкът се обновява

    // Еднократно извличане на рецепта
    @Query("SELECT * FROM recipes WHERE id = :id")
    suspend fun getRecipeById(id: Int): Recipe?

}