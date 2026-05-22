package com.alejandro.notas.data.repository

import androidx.lifecycle.LiveData
import com.alejandro.notas.data.dao.DaoNote
import com.alejandro.notas.model.Category
import com.alejandro.notas.model.Note
import com.alejandro.notas.model.NoteCategoryCrossRef

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

    suspend fun insertWithCategories(note: Note, categories: List<Category>) {
        val noteId = daoNote.createNote(note).toInt()
        categories.forEach { category ->
            daoNote.insertNoteCategoryCrossRef(NoteCategoryCrossRef(noteId, category.name))
        }
    }

    suspend fun updateWithCategories(note: Note, categories: List<Category>) {
        daoNote.updateNote(note)
        daoNote.deleteCategoriesForNote(note.id)
        categories.forEach { category ->
            daoNote.insertNoteCategoryCrossRef(NoteCategoryCrossRef(note.id, category.name))
        }
    }
}