package com.alejandro.examen02.data

import androidx.lifecycle.LiveData

class RepositorioVuelo(private val daoVuelo: DaoVuelo) {
    val daoVuelos: LiveData<List<Vuelo>> = daoVuelo.getAll()

    suspend fun insert(vuelo: Vuelo) {
        daoVuelo.insert(vuelo)
    }

    suspend fun delete(vuelo: Vuelo) {
        daoVuelo.delete(vuelo)
    }
}