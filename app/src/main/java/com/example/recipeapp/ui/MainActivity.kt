package com.example.recipeapp.ui

import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.example.recipeapp.R

class MainActivity : AppCompatActivity() {

    // onCreate – извиква се веднъж, когато приложението стартира
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Намираме NavHostFragment – контейнера, който държи навигацията
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment)
                    as NavHostFragment

        // Управлява кой фрагмент да се покаже
        val navController = navHostFragment.navController

        // Бутон "Home" – показва HomeFragment
        findViewById<ImageButton>(R.id.btnHome).setOnClickListener {
            navController.navigate(R.id.homeFragment)
        }

        // Бутон "Favorite" – показва FavoriteFragment
        findViewById<ImageButton>(R.id.btnFavorite).setOnClickListener {
            navController.navigate(R.id.favoriteFragment)
        }

        // Бутон "Add" – показва AddRecipeFragment
        findViewById<ImageButton>(R.id.btnAdd).setOnClickListener {
            navController.navigate(R.id.addRecipeFragment)
        }

        // Бутон "Profile" – показва ProfileFragment
        findViewById<ImageButton>(R.id.btnProfile).setOnClickListener {
            navController.navigate(R.id.profileFragment)
        }
    }
}