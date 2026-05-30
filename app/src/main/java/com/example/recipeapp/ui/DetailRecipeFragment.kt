package com.example.recipeapp.ui

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.recipeapp.R
import com.example.recipeapp.data.Recipe
import com.example.recipeapp.viewmodel.RecipeViewModel
import com.google.android.material.textfield.TextInputEditText
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DetailRecipeFragment : Fragment(R.layout.fragment_detail_recipe) {

    private lateinit var viewModel: RecipeViewModel
    private var currentRecipe: Recipe? = null
    private var isEditMode = false
    private var currentPhotoPath: String? = null

    // UI Елементи
    private lateinit var etTitle: TextInputEditText
    private lateinit var etIngredients: TextInputEditText
    private lateinit var etInstructions: TextInputEditText
    private lateinit var ivRecipeImage: androidx.appcompat.widget.AppCompatImageView

    // Контейнери за бутони
    private lateinit var llNormalButtons: LinearLayout
    private lateinit var llEditButtons: LinearLayout
    private lateinit var llPhotoButtons: LinearLayout

    // Бутони
    private lateinit var btnEdit: Button
    private lateinit var btnDelete: Button
    private lateinit var btnSaveEdit: Button
    private lateinit var btnCancelEdit: Button
    private lateinit var btnChangePhoto: Button
    private lateinit var btnRemovePhoto: Button

    // Бутони
    private fun setupButtons() {
        // Режим преглед
        btnEdit.setOnClickListener { enableEditMode(true) }
        btnDelete.setOnClickListener { confirmDelete() }

        //Режим на редактиране
        btnSaveEdit.setOnClickListener { saveChanges() }
        btnCancelEdit.setOnClickListener { cancelEdit() }

        // Управление на снимката
        btnChangePhoto.setOnClickListener { checkCameraPermission() }
        btnRemovePhoto.setOnClickListener {
            currentPhotoPath = null
            ivRecipeImage.setImageResource(R.drawable.ic_add_photo)
            Toast.makeText(requireContext(), "Снимката е премахната", Toast.LENGTH_SHORT).show()
            btnSaveEdit.isEnabled = true
        }
    }

    // Това е основният метод, който настройва целия екран
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
        setupButtons()
        setupTextWatchers()
        viewModel = ViewModelProvider(requireActivity())[RecipeViewModel::class.java]
        val recipeId = arguments?.getInt("recipe_id") ?: 0
        loadRecipe(recipeId)
    }

    // Свързва променливите с UI елементите от layout файла
    private fun initViews(view: View) {
        etTitle = view.findViewById(R.id.etDetailTitle)
        etIngredients = view.findViewById(R.id.etDetailIngredients)
        etInstructions = view.findViewById(R.id.etDetailInstructions)
        ivRecipeImage = view.findViewById(R.id.ivRecipeImage)
        btnEdit = view.findViewById(R.id.btnEdit)
        btnDelete = view.findViewById(R.id.btnDelete)
        btnSaveEdit = view.findViewById(R.id.btnSaveEdit)
        btnCancelEdit = view.findViewById(R.id.btnCancelEdit)
        btnChangePhoto = view.findViewById(R.id.btnChangePhoto)
        btnRemovePhoto = view.findViewById(R.id.btnRemovePhoto)
        llNormalButtons = view.findViewById(R.id.llNormalButtons)
        llEditButtons = view.findViewById(R.id.llEditButtons)
        llPhotoButtons = view.findViewById(R.id.llPhotoButtons)
    }

    // Зарежда рецепта от базата данни по ID
    private fun loadRecipe(recipeId: Int) {
        viewModel.getRecipeById(recipeId).observe(viewLifecycleOwner) { recipe ->
            if (recipe != null) {
                currentRecipe = recipe
                currentPhotoPath = recipe.photoPath
                displayRecipe(recipe)
            } else {
                Toast.makeText(requireContext(), "Рецептата не беше намерена", Toast.LENGTH_SHORT).show()
                findNavController().navigateUp()
            }
        }
    }

    // Показва данните на рецептата в UI (режим четене)
    private fun displayRecipe(recipe: Recipe) {
        etTitle.setText(recipe.title)
        etIngredients.setText(recipe.ingredients)
        etInstructions.setText(recipe.instructions)
        loadImage(recipe.photoPath)
        setFieldsEditable(false)
    }

    // Зарежда снимка от пътя (photoPath) и я показва в ImageView.
    private fun loadImage(photoPath: String?) {
        if (!photoPath.isNullOrEmpty()) {
            val file = File(photoPath)
            if (file.exists()) {
                val uri = FileProvider.getUriForFile(
                    requireContext(),
                    "${requireContext().packageName}.fileprovider",
                    file
                )
                ivRecipeImage.setImageURI(uri)
            } else {
                ivRecipeImage.setImageResource(R.drawable.ic_add_photo)
            }
        } else {
            ivRecipeImage.setImageResource(R.drawable.ic_add_photo)
        }
    }

    // Режим на редакцията (editMode)
    private fun enableEditMode(enabled: Boolean) {
        isEditMode = enabled
        setFieldsEditable(enabled)

        llNormalButtons.visibility = if (enabled) View.GONE else View.VISIBLE
        llEditButtons.visibility = if (enabled) View.VISIBLE else View.GONE
        llPhotoButtons.visibility = if (enabled) View.VISIBLE else View.GONE

        btnSaveEdit.isEnabled = false // неактивен до първата промяна
    }

    // Разрешава или забранява редактирането на текстовите полета.
    private fun setFieldsEditable(editable: Boolean) {
        etTitle.isEnabled = editable
        etIngredients.isEnabled = editable
        etInstructions.isEnabled = editable
    }

    // Когато потребителят промени нещо, активира бутона "Запази"
    private fun setupTextWatchers() {
        val textWatcher = { _: android.text.Editable? ->
            if (isEditMode) {
                btnSaveEdit.isEnabled = hasChanges()
            }
        }
        etTitle.doAfterTextChanged(textWatcher)
        etIngredients.doAfterTextChanged(textWatcher)
        etInstructions.doAfterTextChanged(textWatcher)
    }

    // Проверява дали има направени промени
    private fun hasChanges(): Boolean {
        currentRecipe?.let { original ->
            return original.title != etTitle.text.toString() ||
                    original.ingredients != etIngredients.text.toString() ||
                    original.instructions != etInstructions.text.toString() ||
                    original.photoPath != currentPhotoPath
        }
        return false
    }

    // Отказва промените – възстановява старите данни и излиза от режима на редактиране
    private fun cancelEdit() {
        currentRecipe?.let { recipe ->
            etTitle.setText(recipe.title)
            etIngredients.setText(recipe.ingredients)
            etInstructions.setText(recipe.instructions)
            currentPhotoPath = recipe.photoPath
            loadImage(recipe.photoPath)
        }
        enableEditMode(false)
        Toast.makeText(requireContext(), "Промените бяха отхвърлени", Toast.LENGTH_SHORT).show()
    }

    // Запазва промените в базата данни
    private fun saveChanges() {
        currentRecipe?.let { original ->
            val updatedRecipe = original.copy(
                title = etTitle.text.toString().trim(),
                ingredients = etIngredients.text.toString().trim(),
                instructions = etInstructions.text.toString().trim(),
                photoPath = currentPhotoPath
            )

            if (updatedRecipe.title.isEmpty()) {
                etTitle.error = "Заглавието е задължително"
                return
            }
            if (updatedRecipe.ingredients.isEmpty()) {
                etIngredients.error = "Съставките са задължителни"
                return
            }
            if (updatedRecipe.instructions.isEmpty()) {
                etInstructions.error = "Инструкциите са задължителни"
                return
            }

            try {
                viewModel.updateRecipe(updatedRecipe)
                Toast.makeText(requireContext(), "Рецептата е обновена", Toast.LENGTH_SHORT).show()
                currentRecipe = updatedRecipe
                enableEditMode(false)
                displayRecipe(updatedRecipe)
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Грешка: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    //Функция за изтриване
    // Потвърждение за изтриване
    private fun confirmDelete() {
        AlertDialog.Builder(requireContext())
            .setTitle("Изтриване на рецепта")
            .setMessage("Сигурни ли сте, че искате да изтриете \"${currentRecipe?.title}\"?")
            .setPositiveButton("Да") { _, _ -> deleteRecipe() }
            .setNegativeButton("Не", null)
            .show()
    }

    //Изтриване на рецепта
    private fun deleteRecipe() {
        currentRecipe?.let {
            try {
                viewModel.deleteRecipe(it)
                Toast.makeText(requireContext(), "Рецептата е изтрита", Toast.LENGTH_SHORT).show()
                findNavController().navigateUp()
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Грешка при изтриване", Toast.LENGTH_SHORT).show()
            }
        }
    }

    //Камера
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

    // Регистратор за искане на разрешение за камера
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            dispatchTakePictureIntent()
        } else {
            Toast.makeText(requireContext(), "Няма разрешение за камера", Toast.LENGTH_SHORT).show()
        }
    }

    // Отваря камерата, за да направи снимка
    private fun dispatchTakePictureIntent() {
        try {
            val photoFile = createImageFile()
            val photoURI = FileProvider.getUriForFile(
                requireContext(),
                "${requireContext().packageName}.fileprovider",
                photoFile
            )

            // Създава се Intent за отваряне на камерата
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
                putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
            }

            // Стартиране на камерата
            takePictureLauncher.launch(takePictureIntent)
        } catch (e: Exception) {
            Toast.makeText(requireContext(), "Грешка: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    // Регистратор за резултат от камерата
    private val takePictureLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            if (currentPhotoPath != null) {
                val file = File(currentPhotoPath!!)
                if (file.exists()) {
                    val uri = FileProvider.getUriForFile(
                        requireContext(),
                        "${requireContext().packageName}.fileprovider",
                        file
                    )
                    ivRecipeImage.setImageURI(uri)
                    Toast.makeText(requireContext(), "Снимката е обновена", Toast.LENGTH_SHORT).show()
                    btnSaveEdit.isEnabled = true
                }
            }
        } else {
            Toast.makeText(requireContext(), "Снимката не беше направена", Toast.LENGTH_SHORT).show()
        }
    }

    private fun createImageFile(): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val imageFileName = "JPEG_${timeStamp}_"
        val storageDir = requireContext().getExternalFilesDir("Pictures")
        if (storageDir != null && !storageDir.exists()) {
            storageDir.mkdirs()
        }
        return File.createTempFile(imageFileName, ".jpg", storageDir).apply {
            currentPhotoPath = absolutePath
        }
    }
}