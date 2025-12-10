package com.alejandro.testdebbdd.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface DaoTarea {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(tarea: Tarea): Long

    @Delete
    suspend fun delete(tarea: Tarea)

    @Update
    suspend fun update(tarea: Tarea)

    @Query("SELECT * FROM tareas ORDER BY id DESC")
    fun getAllTareas(): LiveData<List<Tarea>>

    @Query("DELETE FROM tareas")
    suspend fun deleteAll()
}