package com.alejandro.notas.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.alejandro.notas.model.Note

@Dao
interface DaoNote {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun createNote(note: Note): Int

    @Update(onConflict = OnConflictStrategy.IGNORE)
    suspend fun updateNote(note: Note): Int

    @Query("SELECT * FROM notas ORDER BY editedAt DESC")
    fun getAllNotes(): LiveData<List<Note>>

    @Query("SELECT * FROM notas WHERE id = :id")
    suspend fun getNoteById(id: Int): Note?

    @Query("DELETE FROM notas WHERE id = :id")
    suspend fun deleteNoteById(id: Int): Int
}