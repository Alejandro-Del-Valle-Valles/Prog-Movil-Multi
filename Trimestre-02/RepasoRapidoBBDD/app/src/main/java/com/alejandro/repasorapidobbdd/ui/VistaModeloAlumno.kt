package com.alejandro.repasorapidobbdd.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.alejandro.repasorapidobbdd.data.Alumno
import com.alejandro.repasorapidobbdd.data.BaseDatosApp
import com.alejandro.repasorapidobbdd.data.RepositorioAlumno
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class VistaModeloAlumno(application: Application) :
    AndroidViewModel(application) {
    private val repositorio : RepositorioAlumno
    val todasTareas = BaseDatosApp.getBaseDatos(getApplication()).daoAlumno().getAll()

    init {
        val dao = BaseDatosApp.getBaseDatos(application).daoAlumno()
        repositorio = RepositorioAlumno(dao)
    }

    fun insert(alumno: Alumno) = viewModelScope.launch(Dispatchers.IO) {
        repositorio.insert(alumno)
    }

    fun delete(alumno: Alumno) = viewModelScope.launch(Dispatchers.IO) {
        repositorio.delete(alumno)
    }

    fun deleteAll() = viewModelScope.launch(Dispatchers.IO) {
        repositorio.deleteAll()
    }
}