package com.alejandro.examen02.app

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.alejandro.examen02.data.BaseDatosApp
import com.alejandro.examen02.data.RepositorioVuelo
import com.alejandro.examen02.data.Vuelo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class VistaModeloVuelo(application: Application) : AndroidViewModel(application) {

    private val repositorio : RepositorioVuelo

    val todosVuelos = mutableListOf<Vuelo>()
    init {
        val dao = BaseDatosApp.getBaseDatos(application).daoVuelo()
        repositorio = RepositorioVuelo(dao)
    }

    fun insert(vuelo: Vuelo) = viewModelScope.launch(Dispatchers.IO) {
        repositorio.insert(vuelo)
    }

    fun delete(vuelo: Vuelo) = viewModelScope.launch(Dispatchers.IO) {
        repositorio.delete(vuelo)
    }

    fun getAll() = BaseDatosApp.getBaseDatos(getApplication())
        .daoVuelo().getAll()
}