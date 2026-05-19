package com.example.recipeapp.ui

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.recipeapp.R
import com.example.recipeapp.data.Recipe
import com.example.recipeapp.viewmodel.RecipeViewModel
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AddRecipeFragment : Fragment(R.layout.fragment_add_recipe) {

    private lateinit var viewModel: RecipeViewModel
    private lateinit var etTitle: EditText
    private lateinit var etIngredients: EditText
    private lateinit var etInstructions: EditText
    private lateinit var btnSave: Button
    private lateinit var btnTakePhoto: Button
    private lateinit var ivPhotoPreview: ImageView

    private var currentPhotoPath: String? = null
    private var photoUri: Uri? = null

    // Регистратор за резултат от камера
    private val takePictureLauncher = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            ivPhotoPreview.setImageURI(photoUri)
            Toast.makeText(requireContext(), "Снимката е добавена", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(requireContext(), "Снимката не беше направена", Toast.LENGTH_SHORT).show()
        }
    }

    // Регистратор за разрешение за камера
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            dispatchTakePictureIntent()
        } else {
            Toast.makeText(requireContext(), "Няма разрешение за камера", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        etTitle = view.findViewById(R.id.etTitle)
        etIngredients = view.findViewById(R.id.etIngredients)
        etInstructions = view.findViewById(R.id.etInstructions)
        btnSave = view.findViewById(R.id.btnSave)
        btnTakePhoto = view.findViewById(R.id.btnTakePhoto)
        ivPhotoPreview = view.findViewById(R.id.ivPhotoPreview)

        viewModel = ViewModelProvider(this)[RecipeViewModel::class.java]

        btnTakePhoto.setOnClickListener {
            checkCameraPermission()
        }

        btnSave.setOnClickListener {
            saveRecipe()
        }
    }

    private fun checkCameraPermission() {
        when {
            ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED -> {
                dispatchTakePictureIntent()
            }
            else -> {
                requestPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }
    }

    private fun dispatchTakePictureIntent() {
        try {
            val photoFile = createImageFile()
            currentPhotoPath = photoFile.absolutePath

            photoUri = FileProvider.getUriForFile(
                requireContext(),
                "${requireContext().packageName}.fileprovider",
                photoFile
            )

            takePictureLauncher.launch(photoUri)
        } catch (e: Exception) {
            Log.e("CameraDebug", "Error starting camera: ${e.message}")
            Toast.makeText(requireContext(), "Грешка при стартиране на камерата", Toast.LENGTH_SHORT).show()
        }
    }

    private fun createImageFile(): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val imageFileName = "JPEG_${timeStamp}_"
        val storageDir = requireContext().getExternalFilesDir("Pictures")
        if (storageDir != null && !storageDir.exists()) {
            storageDir.mkdirs()
        }
        return File.createTempFile(imageFileName, ".jpg", storageDir)
    }

    private fun saveRecipe() {
        val title = etTitle.text.toString().trim()
        val ingredients = etIngredients.text.toString().trim()
        val instructions = etInstructions.text.toString().trim()

        when {
            title.isEmpty() -> {
                etTitle.error = "Моля, въведете заглавие"
                etTitle.requestFocus()
                return
            }
            ingredients.isEmpty() -> {
                etIngredients.error = "Моля, въведете съставки"
                etIngredients.requestFocus()
                return
            }
            instructions.isEmpty() -> {
                etInstructions.error = "Моля, въведете инструкции"
                etInstructions.requestFocus()
                return
            }
        }

        btnSave.isEnabled = false

        val recipe = Recipe(
            title = title,
            ingredients = ingredients,
            instructions = instructions,
            photoPath = currentPhotoPath
        )

        try {
            viewModel.addRecipe(recipe)
            Toast.makeText(requireContext(), "Рецептата е добавена успешно!", Toast.LENGTH_SHORT).show()
            clearFields()
            findNavController().navigateUp()
        } catch (e: Exception) {
            Toast.makeText(requireContext(), "Грешка: ${e.message}", Toast.LENGTH_SHORT).show()
            btnSave.isEnabled = true
        }
    }

    private fun clearFields() {
        etTitle.text.clear()
        etIngredients.text.clear()
        etInstructions.text.clear()
        currentPhotoPath = null
        ivPhotoPreview.setImageResource(R.drawable.ic_add_photo)
    }
}
