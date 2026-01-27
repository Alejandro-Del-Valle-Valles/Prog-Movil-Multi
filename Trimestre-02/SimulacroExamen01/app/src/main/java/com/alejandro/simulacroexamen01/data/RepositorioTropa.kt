package com.alejandro.simulacroexamen01.data

import androidx.lifecycle.LiveData

class RepositorioTropa(private val daoTropa: DaoTropa) {

    val todasTropas: LiveData<List<Tropa>> = daoTropa.getAll()
    suspend fun insert(tarea: Tropa){
        daoTropa.insert(tarea)
    }
    suspend fun delete(tarea: Tropa){
        daoTropa.delete(tarea)
    }
}