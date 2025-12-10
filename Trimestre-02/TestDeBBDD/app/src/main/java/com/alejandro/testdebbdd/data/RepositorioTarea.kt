package com.alejandro.testdebbdd.data

import androidx.lifecycle.LiveData

class RepositorioTarea(private val daoTarea: DaoTarea) {

    val allTareas: LiveData<List<Tarea>> = daoTarea.getAllTareas()

    suspend fun insert(tarea: Tarea) {
        daoTarea.insert(tarea)
    }

    suspend fun delete(tarea: Tarea) {
        daoTarea.delete(tarea)
    }

    suspend fun update(tarea: Tarea) {
        daoTarea.update(tarea)
    }

    suspend fun deleteAll() {
        daoTarea.deleteAll()
    }
}