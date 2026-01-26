package com.alejandro.examen02.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface DaoVuelo {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(vuelo: Vuelo) : Long

    @Delete
    suspend fun delete(vuelo: Vuelo)

    @Query("SELECT * FROM Vuelos ORDER BY id DESC")
    fun getAll(): LiveData<List<Vuelo>>
}