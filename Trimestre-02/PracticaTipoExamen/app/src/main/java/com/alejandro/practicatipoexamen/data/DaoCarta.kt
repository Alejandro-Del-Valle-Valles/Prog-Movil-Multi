package com.alejandro.practicatipoexamen.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface DaoCarta {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(carta: Carta): Long

    @Delete
    suspend fun delete(carta: Carta)

    @Query("SELECT * FROM cartas")
    fun getCartas(): LiveData<List<Carta>>

    @Query("DELETE FROM cartas")
    suspend fun deleteCartas()
}