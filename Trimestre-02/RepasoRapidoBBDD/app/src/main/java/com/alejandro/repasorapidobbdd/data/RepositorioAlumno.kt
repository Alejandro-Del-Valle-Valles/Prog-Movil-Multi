package com.alejandro.repasorapidobbdd.data

import androidx.lifecycle.LiveData

class RepositorioAlumno(private val daoAlumno: DaoAlumno) {

    val todasTareas: LiveData<List<Alumno>> = daoAlumno.getAll()

    suspend fun insert(alumno: Alumno){
        daoAlumno.insert(alumno)
    }

    suspend fun delete(alumno: Alumno){
        daoAlumno.delete(alumno)
    }

    suspend fun deleteAll(){
        daoAlumno.deleteAll()
    }
}