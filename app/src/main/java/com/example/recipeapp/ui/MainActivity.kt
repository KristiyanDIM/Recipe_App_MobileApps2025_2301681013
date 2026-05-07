package com.example.recipeapp.ui

import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.example.recipeapp.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment)
                    as NavHostFragment

        val navController = navHostFragment.navController

        findViewById<ImageButton>(R.id.btnHome).setOnClickListener {
            navController.navigate(R.id.homeFragment)
        }

        findViewById<ImageButton>(R.id.btnFavorite).setOnClickListener {
            navController.navigate(R.id.favoriteFragment)
        }

        findViewById<ImageButton>(R.id.btnAdd).setOnClickListener {
            navController.navigate(R.id.addRecipeFragment)
        }

        findViewById<ImageButton>(R.id.btnProfile).setOnClickListener {
            navController.navigate(R.id.profileFragment)
        }
    }
}