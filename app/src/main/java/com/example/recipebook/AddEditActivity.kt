package com.example.recipebook

import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class AddEditActivity : AppCompatActivity() {

    private lateinit var etTitle: EditText
    private lateinit var etIngredients: EditText
    private lateinit var etContent: EditText
    private lateinit var btnSave: Button
    private lateinit var repository: RecipeRepository

    private var recipeId: Long = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit)

        etTitle = findViewById(R.id.etTitle)
        etIngredients = findViewById(R.id.etIngredients)
        etContent = findViewById(R.id.etContent)
        btnSave = findViewById(R.id.btnSave)

        repository = RecipeRepository(this)

        recipeId = intent.getLongExtra("RECIPE_ID", -1)

        if (recipeId != -1L) {
            loadRecipeData()
        }

        btnSave.setOnClickListener {
            saveRecipe()
        }
    }

    private fun loadRecipeData() {
        val recipe = repository.getRecipeById(recipeId)
        if (recipe != null) {
            etTitle.setText(recipe.title)
            etIngredients.setText(recipe.ingredients)
            etContent.setText(recipe.content)
        }
    }

    private fun saveRecipe() {
        val title = etTitle.text.toString().trim()
        val ingredients = etIngredients.text.toString().trim()
        val content = etContent.text.toString().trim()

        if (title.isEmpty() || content.isEmpty()) {
            Toast.makeText(this, "Заполните название и описание рецепта", Toast.LENGTH_SHORT).show()
            return
        }

        val recipe = Recipe(
            id = if (recipeId == -1L) 0 else recipeId,
            title = title,
            ingredients = ingredients,
            content = content,
            cookingTime = 0 // временно
        )

        if (recipeId == -1L) {
            repository.addRecipe(recipe)
            Toast.makeText(this, "Рецент добавлен", Toast.LENGTH_SHORT).show()
        } else {
            repository.updateRecipe(recipe)
            Toast.makeText(this, "Рецепт обновлен", Toast.LENGTH_SHORT).show()
        }

        finish()
    }
}