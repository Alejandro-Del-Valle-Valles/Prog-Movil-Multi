package com.alejandro.notas.data.repository

import androidx.lifecycle.LiveData
import com.alejandro.notas.data.dao.DaoCategory
import com.alejandro.notas.model.Category

class CategoryRepository(private val daoCategory: DaoCategory) {
    val allCategories: LiveData<List<Category>> = daoCategory.getAllCategories()

    suspend fun insert(category: Category): Long {
        return daoCategory.createCategory(category)
    }

    suspend fun update(oldCategory: Category, newCategory: Category): Int {
        return daoCategory.updateCategory(oldCategory.name, newCategory.name, newCategory.color)
    }

    suspend fun delete(category: String): Int {
        return daoCategory.deleteCategory(category)
    }
}
