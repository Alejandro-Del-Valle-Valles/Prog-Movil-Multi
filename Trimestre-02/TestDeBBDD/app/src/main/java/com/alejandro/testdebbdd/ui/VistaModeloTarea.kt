package com.alejandro.testdebbdd.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.alejandro.testdebbdd.data.BaseDatosApp
import com.alejandro.testdebbdd.data.RepositorioTarea
import com.alejandro.testdebbdd.data.Tarea
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class VistaModeloTarea(application: Application): AndroidViewModel(application) {

    private val repositorio: RepositorioTarea
    val allTareas = mutableListOf<Tarea>()

    init {
        val dao = BaseDatosApp.getBaseDatos(application).daoTarea()
        repositorio = RepositorioTarea(dao)
    }

    fun insert(tarea: Tarea) = viewModelScope.launch(Dispatchers.IO) {
        repositorio.insert(tarea)
    }

    fun delete(tarea: Tarea) = viewModelScope.launch(Dispatchers.IO) {
        repositorio.delete(tarea)
    }

    fun update(tarea: Tarea) = viewModelScope.launch(Dispatchers.IO) {
        repositorio.update(tarea)
    }

    fun deleteAll() = viewModelScope.launch(Dispatchers.IO) {
        repositorio.deleteAll()
    }

    fun getAllTareasLiveData() = BaseDatosApp.getBaseDatos(getApplication()).daoTarea()
        .getAllTareas()
}