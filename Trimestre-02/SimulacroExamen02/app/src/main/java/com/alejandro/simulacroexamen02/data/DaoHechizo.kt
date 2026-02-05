package com.alejandro.simulacroexamen02.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface DaoHechizo {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(hechizo: Hechizo) : Long

    @Query("SELECT * FROM hechizos")
    fun getAll(): LiveData<List<Hechizo>>
}