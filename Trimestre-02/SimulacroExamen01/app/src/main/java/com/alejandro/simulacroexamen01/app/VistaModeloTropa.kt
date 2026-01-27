package com.alejandro.simulacroexamen01.app

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.alejandro.simulacroexamen01.data.BaseDatosApp
import com.alejandro.simulacroexamen01.data.RepositorioTropa
import com.alejandro.simulacroexamen01.data.Tropa
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class VistaModeloTropa(application: Application) :
    AndroidViewModel(application) {
    private val repositorio : RepositorioTropa
    val todasTropas: LiveData<List<Tropa>> = BaseDatosApp.getBaseDatos(getApplication()).daoTropa().getAll()
    init {
        val dao = BaseDatosApp.getBaseDatos(application).daoTropa()
        repositorio = RepositorioTropa(dao)
    }
    fun insert(tarea: Tropa) = viewModelScope.launch(Dispatchers.IO) {
        repositorio.insert(tarea)
    }
    fun delete(tarea: Tropa) = viewModelScope.launch(Dispatchers.IO) {
        repositorio.delete(tarea)
    }

}