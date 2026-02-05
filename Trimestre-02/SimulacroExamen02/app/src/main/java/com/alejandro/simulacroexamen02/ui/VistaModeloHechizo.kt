package com.alejandro.simulacroexamen02.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.alejandro.simulacroexamen02.data.BaseDatosApp
import com.alejandro.simulacroexamen02.data.Hechizo
import com.alejandro.simulacroexamen02.data.RepositorioHechizo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class VistaModeloHechizo (application: Application) :
    AndroidViewModel(application) {
    private val repositorio : RepositorioHechizo
    val todosHechizo = BaseDatosApp.getBaseDatos(getApplication()).daoTarea().getAll()

    init {
        val dao = BaseDatosApp.getBaseDatos(application).daoTarea()
        repositorio = RepositorioHechizo(dao)
    }

    fun inserta(hechizo: Hechizo) = viewModelScope.launch(Dispatchers.IO) {
        repositorio.insert(hechizo)
    }
}