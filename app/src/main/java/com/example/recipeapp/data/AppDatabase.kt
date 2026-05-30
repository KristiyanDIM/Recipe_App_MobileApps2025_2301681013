package com.example.recipeapp.data

import android.content.Context
import androidx.room.*

@Database(entities = [Recipe::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun recipeDao(): RecipeDao
    // Връща DAO обект, който изпълнява заявките към базата
    companion object {  // Единствената инстанция на базата (сингълтон)
        @Volatile private var INSTANCE: AppDatabase? = null

        // Връща инстанцията на базата (създава я само веднъж)
      fun get(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java, "recipes_database"
                ).build().also { INSTANCE = it }
            }
    }
}
