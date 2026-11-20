package com.example.recipebook

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.database.Cursor

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "recipe_book.db"
        private const val DATABASE_VERSION = 1

        const val TABLE_NAME = "recipes"
        const val COLUMN_ID = "_id"
        const val COLUMN_TITLE = "title"
        const val COLUMN_INGREDIENTS = "ingredients"
        const val COLUMN_CONTENT = "content"
        const val COLUMN_TIME = "cooking_time"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        // Создаем таблицу строго по структуре из плана
        val createTableQuery = """
            CREATE TABLE $TABLE_NAME (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_TITLE TEXT NOT NULL,
                $COLUMN_INGREDIENTS TEXT,
                $COLUMN_CONTENT TEXT NOT NULL,
                $COLUMN_TIME INTEGER
            )
        """.trimIndent()
        db?.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    // --- CRUD операции ---

    // CREATE (Добавление)
    fun addRecipe(recipe: Recipe): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TITLE, recipe.title)
            put(COLUMN_INGREDIENTS, recipe.ingredients)
            put(COLUMN_CONTENT, recipe.content)
            put(COLUMN_TIME, recipe.cookingTime)
        }
        return db.insert(TABLE_NAME, null, values)
    }

    // READ (Получение всех рецептов)
    fun getAllRecipes(): List<Recipe> {
        val recipeList = mutableListOf<Recipe>()
        val db = this.readableDatabase
        val cursor: Cursor = db.query(TABLE_NAME, null, null, null, null, null, null)

        while (cursor.moveToNext()) {
            val id = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_ID))
            val title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE))
            val ingredients = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_INGREDIENTS)) ?: ""
            val content = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTENT))
            val cookingTime = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_TIME))

            val recipe = Recipe(id, title, ingredients, content, cookingTime)
            recipeList.add(recipe)
        }
        cursor.close()
        return recipeList
    }

    // READ еще один метод чтения. Получаем элемент по айди
    fun getRecipeById(id: Long): Recipe? {
        val db = this.readableDatabase
        val cursor = db.query(TABLE_NAME, null, "$COLUMN_ID = ?", arrayOf(id.toString()), null, null, null)
        var recipe: Recipe? = null

        if (cursor.moveToFirst()) {
            val title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE))
            val ingredients = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_INGREDIENTS)) ?: ""
            val content = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTENT))
            val cookingTime = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_TIME))
            recipe = Recipe(id, title, ingredients, content, cookingTime)
        }
        cursor.close()
        db.close()
        return recipe
    }

    // UPDATE (Обновление)
    fun updateRecipe(recipe: Recipe): Int {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TITLE, recipe.title)
            put(COLUMN_INGREDIENTS, recipe.ingredients)
            put(COLUMN_CONTENT, recipe.content)
            put(COLUMN_TIME, recipe.cookingTime)
        }
        val rowsUpdated = db.update(TABLE_NAME, values, "$COLUMN_ID = ?", arrayOf(recipe.id.toString()))
        db.close()
        return rowsUpdated
    }

    // DELETE (Удаление)
    fun deleteRecipe(recipeId: Long) {
        val db = this.writableDatabase
        db.delete(TABLE_NAME, "$COLUMN_ID = ?", arrayOf(recipeId.toString()))
        db.close()
    }
}