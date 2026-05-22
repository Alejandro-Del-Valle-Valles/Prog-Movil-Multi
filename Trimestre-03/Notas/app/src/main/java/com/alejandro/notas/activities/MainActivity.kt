package com.alejandro.notas.activities

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu // Added for the filter menu
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.alejandro.notas.R
import com.alejandro.notas.databinding.ActivityMainBinding
import com.alejandro.notas.helpers.NoteAdapter
import com.alejandro.notas.model.Category // Make sure this is imported
import com.alejandro.notas.model.Note
import com.alejandro.notas.model.NoteWithCategories

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val notesViewModel: NoteViewModel by viewModels()
    private val categoryViewModel: CategoryViewModel by viewModels()
    private var allNotesList: List<NoteWithCategories> = emptyList()
    private var availableCategories: List<Category> = emptyList()
    private lateinit var noteAdapter: NoteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        notesViewModel.getAllNotesLiveData().observe(this) { list ->
            allNotesList = list
            noteAdapter.updateNotes(list)
        }

        categoryViewModel.getAllCategoriesLiveData().observe(this) { list ->
            availableCategories = list
        }

        binding.etSearchBar.addTextChangedListener { text ->
            val query = text.toString()
            val filteredList = if (query.isEmpty()) {
                allNotesList
            } else {
                allNotesList.filter { it.note.title.contains(query, ignoreCase = true) }
            }
            noteAdapter.updateNotes(filteredList)
        }

        binding.ibFilter.setOnClickListener { view ->
            val popup = PopupMenu(this, view)
            popup.menu.add(0, 0, 0, "Todas las categorías")

            availableCategories.forEachIndexed { index, category ->
                popup.menu.add(0, index + 1, 0, category.name)
            }

            popup.setOnMenuItemClickListener { item ->
                val selectedCategory = item.title.toString()

                if (selectedCategory == "Todas las categorías") {
                    noteAdapter.updateNotes(allNotesList)
                } else {
                    val filteredNotes = allNotesList.filter { itemNote ->
                        itemNote.categories.any { it.name == selectedCategory }
                    }
                    noteAdapter.updateNotes(filteredNotes)
                }
                true
            }
            popup.show()
        }

        binding.btAdd.setOnClickListener {
            showNoteDialog(null)
        }
    }

    private fun setupRecyclerView() {
        noteAdapter = NoteAdapter { item ->
            showNoteDialog(item)
        }

        binding.rvNotes.apply {
            adapter = noteAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }
    }

    /**
     * Displays an AlertDialog to create or edit a note.
     * @param noteToEdit The note to edit, or null if creating a new one.
     */
    private fun showNoteDialog(noteToEdit: NoteWithCategories?) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_edit_note, null)
        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .create()

        val etTitle = dialogView.findViewById<EditText>(R.id.etDialogTitle)
        val etContent = dialogView.findViewById<EditText>(R.id.etDialogContent)
        val btnSave = dialogView.findViewById<Button>(R.id.btnSave)

        var selectedColor = "#FFFFFF"

        if (noteToEdit != null) {
            etTitle.setText(noteToEdit.note.title)
            etContent.setText(noteToEdit.note.content)
            selectedColor = noteToEdit.note.color
        }

        btnSave.setOnClickListener {
            val title = etTitle.text.toString()
            val content = etContent.text.toString()

            if (noteToEdit == null) {
                val newNote = Note(title = title, content = content, color = selectedColor)
                notesViewModel.insert(newNote)
            } else {
                val updatedNote = noteToEdit.note.copy(title = title, content = content, color = selectedColor)
                notesViewModel.update(updatedNote)
            }
            dialog.dismiss()
        }
        dialog.show()
    }
}