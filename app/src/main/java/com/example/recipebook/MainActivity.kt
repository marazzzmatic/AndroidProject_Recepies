package com.example.recipebook

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var adapter: RecipeAdapter
    private lateinit var repository: RecipeRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val rvRecipes: RecyclerView = findViewById(R.id.rvRecipes)
        val btnAdd: Button = findViewById(R.id.btnAdd)

        repository = RecipeRepository(this)

        adapter = RecipeAdapter(mutableListOf()) { recipe ->
            repository.deleteRecipe(recipe.id)
            loadRecipes() // Обновляем список после удаления
        }

        rvRecipes.layoutManager = LinearLayoutManager(this)
        rvRecipes.adapter = adapter

        loadRecipes()

        btnAdd.setOnClickListener {
            val intent = Intent(this, AddEditActivity::class.java)
            startActivity(intent)
        }

    }

    override fun onResume() {
        super.onResume()
        loadRecipes()
    }

    private fun loadRecipes() {
        val recipes = repository.getAllRecipes()
        android.util.Log.d("RecipeDebug", "Loaded ${recipes.size}: ${recipes.map { it.title }}")
        adapter.updateData(recipes)
    }

}