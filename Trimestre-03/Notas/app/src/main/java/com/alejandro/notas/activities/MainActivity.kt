package com.alejandro.notas.activities

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alejandro.notas.R
import com.alejandro.notas.databinding.ActivityMainBinding
import com.alejandro.notas.helpers.ColorAdapter
import com.alejandro.notas.helpers.NoteAdapter
import com.alejandro.notas.model.Category
import com.alejandro.notas.model.Note
import com.alejandro.notas.model.NoteWithCategories
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val notesViewModel: NoteViewModel by viewModels()
    private val categoryViewModel: CategoryViewModel by viewModels()
    private var allNotesList: List<NoteWithCategories> = emptyList()
    private var availableCategories: List<Category> = emptyList()
    private lateinit var noteAdapter: NoteAdapter

    //Naranja, Rojo, Morado, Azul, Amarillo, Rosa, Verde
    private val colorPalette = listOf(
        "#FF9800", "#F44336", "#9C27B0", "#2196F3",
        "#FFEB3B", "#E91E63", "#4CAF50"
    )

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
        val dialog = AlertDialog.Builder(this).setView(dialogView).create()

        val etTitle = dialogView.findViewById<EditText>(R.id.etDialogTitle)
        val etContent = dialogView.findViewById<EditText>(R.id.etDialogContent)
        val ibMenu = dialogView.findViewById<ImageButton>(R.id.ibMenu)
        val cgCategories = dialogView.findViewById<ChipGroup>(R.id.cgDialogCategories)
        val rvColorPicker = dialogView.findViewById<RecyclerView>(R.id.rvColorPicker)
        val btnSave = dialogView.findViewById<Button>(R.id.btnSave)

        var selectedNoteColor = "#FFFFFF"
        val currentCategories = noteToEdit?.categories?.toMutableList() ?: mutableListOf()

        /**
         * Refreshes the ChipGroup UI based on currentCategories.
         */
        fun updateChips() {
            cgCategories.removeAllViews()
            currentCategories.forEach { category ->
                val chip = Chip(this).apply {
                    text = category.name
                    chipBackgroundColor = ColorStateList.valueOf(Color.parseColor(category.color))
                    setTextColor(Color.WHITE)
                    setOnClickListener {
                        currentCategories.remove(category)
                        updateChips()
                    }
                }
                cgCategories.addView(chip)
            }
        }

        if (noteToEdit != null) {
            etTitle.setText(noteToEdit.note.title)
            etContent.setText(noteToEdit.note.content)
            selectedNoteColor = noteToEdit.note.color
        }
        updateChips()

        rvColorPicker.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rvColorPicker.adapter = ColorAdapter(colorPalette) { color ->
            selectedNoteColor = color
            dialogView.setBackgroundColor(Color.parseColor(color))
        }

        ibMenu.setOnClickListener { view ->
            val popup = PopupMenu(this, view)
            popup.menu.add("Añadir etiqueta")
            if (noteToEdit != null) popup.menu.add("Eliminar nota")

            popup.setOnMenuItemClickListener { item ->
                when (item.title) {
                    "Eliminar nota" -> {
                        notesViewModel.delete(noteToEdit!!.note.id)
                        dialog.dismiss()
                    }
                    "Añadir etiqueta" -> showAddCategoryDialog { newCategory ->
                        currentCategories.add(newCategory)
                        updateChips()
                    }
                }
                true
            }
            popup.show()
        }

        btnSave.setOnClickListener {
            val title = etTitle.text.toString()
            val content = etContent.text.toString()

            if (noteToEdit == null) {
                val newNote = Note(
                    title = title,
                    content = content,
                    color = selectedNoteColor
                )
                notesViewModel.insert(newNote)
            } else {
                // Es una actualización
                val updatedNote = noteToEdit.note.copy(
                    title = title,
                    content = content,
                    color = selectedNoteColor,
                    editedAt = java.time.LocalDateTime.now()
                )
                notesViewModel.update(updatedNote)
            }
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun showAddCategoryDialog(onCategoryAdded: (Category) -> Unit) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_category, null)
        val dialog = AlertDialog.Builder(this).setView(dialogView).create()

        val etName = dialogView.findViewById<EditText>(R.id.etCategoryName)
        val rvColors = dialogView.findViewById<RecyclerView>(R.id.rvCategoryColorPicker)
        val btnAdd = dialogView.findViewById<Button>(R.id.btnAddCategory)

        var selectedCategoryColor = "#4CAF50" // Default Green

        rvColors.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rvColors.adapter = ColorAdapter(colorPalette) { color ->
            selectedCategoryColor = color
        }

        btnAdd.setOnClickListener {
            val name = etName.text.toString().trim()
            if (name.isNotEmpty()) {
                val newCat = Category(name = name, color = selectedCategoryColor)
                categoryViewModel.insert(newCat)
                onCategoryAdded(newCat)
                dialog.dismiss()
            }
        }

        dialog.show()
    }
}