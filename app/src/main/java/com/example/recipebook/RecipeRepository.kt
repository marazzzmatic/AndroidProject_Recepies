package com.example.recipebook

class RecipeRepository(context: android.content.Context) {
    private val dbHelper = DatabaseHelper(context)

    fun addRecipe(recipe: Recipe) = dbHelper.addRecipe(recipe)
    fun updateRecipe(recipe: Recipe) = dbHelper.updateRecipe(recipe)
    fun deleteRecipe(recipeId: Long) = dbHelper.deleteRecipe(recipeId)
    fun getAllRecipes(): List<Recipe> = dbHelper.getAllRecipes()
    fun getRecipeById(id: Long): Recipe? = dbHelper.getRecipeById(id)
}