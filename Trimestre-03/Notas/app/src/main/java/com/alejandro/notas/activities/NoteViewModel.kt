package com.alejandro.notas.activities

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.alejandro.notas.data.DataBaseApp
import com.alejandro.notas.data.repository.NoteRepository
import com.alejandro.notas.model.Note
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NoteViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: NoteRepository
    val allNotes = mutableListOf<Note>()

    init {
        val noteDao = DataBaseApp.getDataBase(application).daoNote()
        repository = NoteRepository(noteDao)
    }

    /**
     * Retrieves a note by its ID. This function runs in the IO dispatcher to avoid blocking the main thread, and the result is returned through a callback on the main thread.
     */
    fun getById(id: Int, onResult: (Note?) -> Unit) = viewModelScope.launch(Dispatchers.IO) {
        val note = repository.getById(id)
        withContext(Dispatchers.Main) {
            onResult(note)
        }
    }

    /**
     * Inserts a new note into the database. This function runs in the IO dispatcher to avoid blocking the main thread.
     */
    fun insert(note: Note) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(note)
    }

    /**
     * Updates an existing note. This function runs in the IO dispatcher to avoid blocking the main thread.
     */
    fun update(note: Note) = viewModelScope.launch(Dispatchers.IO) {
        repository.update(note)
    }

    /**
     * Deletes a note by its ID. This function runs in the IO dispatcher to avoid blocking the main thread.
     */
    fun delete(id: Int) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(id)
    }

    /**
     * Get all notes as LiveData to observe changes in the UI.
     */
    fun getAllNotesLiveData() = DataBaseApp.getDataBase(getApplication()).daoNote().getNotesWithCategories()
}