package com.alejandro.notas.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.alejandro.notas.model.Category

@Dao
interface DaoCategory {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun createCategory(category: Category): Long

    @Query("UPDATE categorias SET name = :newCategoryName, color = :newCategoryColor WHERE name = :oldCategory")
    suspend fun updateCategory(oldCategory: String, newCategoryName: String, newCategoryColor: String): Int

    @Query("SELECT * FROM categorias")
    fun getAllCategories(): LiveData<List<Category>>

    @Query("DELETE FROM categorias WHERE name = :category")
    suspend fun deleteCategory(category: String): Int
}
