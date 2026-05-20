package com.alejandro.notas.activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.alejandro.notas.databinding.ActivityMainBinding
import com.alejandro.notas.helpers.NoteAdapter

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val notesViewModel: NoteViewModel by viewModels()
    private val categoryViewModel: CategoryViewModel by viewModels()
    private lateinit var noteAdapter: NoteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()

        notesViewModel.getAllNotesLiveData().observe(this) { list ->
            noteAdapter.updateNotes(list)
        }

        binding.btAdd.setOnClickListener {
            //TODO Añadir nota
        }
    }

    private fun setupRecyclerView() {
        noteAdapter = NoteAdapter { item ->
            //TODO Abrir la nota pulsada
        }
        binding.rvNotes.apply {
            adapter = noteAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }
    }
}