package com.alejandro.simulacroexamen02.data

import androidx.lifecycle.LiveData

class RepositorioHechizo (private val daoHechizo: DaoHechizo) {
    val todasTareas: LiveData<List<Hechizo>> = daoHechizo.getAll()

    suspend fun insert(hechizo: Hechizo) {
        daoHechizo.insert(hechizo)
    }
}