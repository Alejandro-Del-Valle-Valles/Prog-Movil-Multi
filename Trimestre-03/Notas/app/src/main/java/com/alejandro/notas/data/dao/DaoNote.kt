package com.alejandro.notas.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.alejandro.notas.model.Note
import com.alejandro.notas.model.NoteCategoryCrossRef
import com.alejandro.notas.model.NoteWithCategories

@Dao
interface DaoNote {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun createNote(note: Note): Long

    @Update(onConflict = OnConflictStrategy.IGNORE)
    suspend fun updateNote(note: Note): Int

    @Query("SELECT * FROM notas ORDER BY editedAt DESC")
    fun getAllNotes(): LiveData<List<Note>>

    @Query("SELECT * FROM notas WHERE id = :id")
    suspend fun getNoteById(id: Int): Note?

    @Query("DELETE FROM notas WHERE id = :id")
    suspend fun deleteNoteById(id: Int): Int

    @Query("DELETE FROM note_category_cross_ref WHERE id = :noteId")
    suspend fun deleteCategoriesForNote(noteId: Int)

    @Transaction
    @Query("SELECT * FROM notas ORDER BY editedAt DESC")
    fun getNotesWithCategories(): LiveData<List<NoteWithCategories>>

    @Transaction
    @Query("SELECT * FROM notas WHERE id = :id")
    suspend fun getNoteWithCategoriesById(id: Int): NoteWithCategories?

    /**
     * Inserts the relationship between a note and a category.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNoteCategoryCrossRef(crossRef: NoteCategoryCrossRef)
}