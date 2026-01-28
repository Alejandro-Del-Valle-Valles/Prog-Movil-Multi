package com.alejandro.repasorapidobbdd.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface DaoAlumno {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(alumno: Alumno) : Long

    @Delete
    suspend fun delete(alumno: Alumno)

    @Query("SELECT * FROM alumnos")
    fun getAll(): LiveData<List<Alumno>>

    @Query("DELETE FROM alumnos")
    suspend fun deleteAll()
}