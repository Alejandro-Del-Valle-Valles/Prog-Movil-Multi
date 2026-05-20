package com.alejandro.notas.data.repository

import androidx.lifecycle.LiveData
import com.alejandro.notas.data.dao.DaoNote
import com.alejandro.notas.model.Note

class NoteRepository(private val daoNote: DaoNote) {
    val allNotes: LiveData<List<Note>> = daoNote.getAllNotes()

    suspend fun insert(note: Note): Long {
        return daoNote.createNote(note)
    }

    suspend fun update(note: Note): Int {
        return daoNote.updateNote(note)
    }

    suspend fun getById(id: Int): Note? {
        return daoNote.getNoteById(id)
    }

    suspend fun delete(id: Int): Int {
        return daoNote.deleteNoteById(id)
    }
}