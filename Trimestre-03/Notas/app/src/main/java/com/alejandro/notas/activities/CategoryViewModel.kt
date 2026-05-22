package com.alejandro.notas.activities

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.alejandro.notas.data.DataBaseApp
import com.alejandro.notas.data.repository.CategoryRepository
import com.alejandro.notas.model.Category
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CategoryViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: CategoryRepository
    val allCategories = mutableListOf<String>()

    init {
        val categoryDao = DataBaseApp.getDataBase(application).daoCategory()
        repository = CategoryRepository(categoryDao)
    }

    fun insert(category: Category) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(category)
    }

    fun update(oldCategory: Category, newCategory: Category) = viewModelScope.launch(Dispatchers.IO) {
        repository.update(oldCategory, newCategory)
    }

    fun delete(category: String) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(category)
    }

    fun getAllCategoriesLiveData() = DataBaseApp.getDataBase(getApplication()).daoCategory().getAllCategories()
}