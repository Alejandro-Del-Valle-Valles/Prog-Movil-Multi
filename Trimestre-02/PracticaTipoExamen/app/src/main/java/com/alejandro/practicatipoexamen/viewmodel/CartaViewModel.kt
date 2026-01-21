package com.alejandro.practicatipoexamen.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.alejandro.practicatipoexamen.data.BaseDatosApp
import com.alejandro.practicatipoexamen.data.Carta
import com.alejandro.practicatipoexamen.data.CartaRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CartaViewModel(application: Application): AndroidViewModel(application) {

    private val repositorio: CartaRepository
    val tareas = mutableListOf<Carta>()

    init {
        val dao = BaseDatosApp.getBaseDatos(application).daoCarta()
        repositorio = CartaRepository(dao)
    }

    fun insert(carta: Carta) = viewModelScope.launch(Dispatchers.IO) {
        repositorio.inserta(carta)
    }

    fun delete(carta: Carta) = viewModelScope.launch(Dispatchers.IO) {
        repositorio.delete(carta)
    }

    fun deleteAll() = viewModelScope.launch(Dispatchers.IO) {
        repositorio.deleteAll()
    }

    fun getAll() = BaseDatosApp.getBaseDatos(getApplication())
        .daoCarta().getCartas()
}