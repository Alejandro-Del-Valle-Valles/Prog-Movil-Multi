package com.alejandro.notas.data.repository

import androidx.lifecycle.LiveData
import com.alejandro.notas.data.dao.DaoCategory
import com.alejandro.notas.model.Category

class CategoryRepository(private val daoCategory: DaoCategory) {
    val allCategories: LiveData<List<Category>> = daoCategory.getAllCategories()

    suspend fun insert(category: String): Long {
        return daoCategory.createCategory(Category(category))
    }

    suspend fun update(oldCategory: String, newCategory: String): Int {
        return daoCategory.updateCategory(oldCategory, newCategory)
    }

    suspend fun delete(category: String): Int {
        return daoCategory.deleteCategory(category)
    }
}
