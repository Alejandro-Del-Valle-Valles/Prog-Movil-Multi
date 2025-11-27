package com.alejandro.repaso_pokemon.controllers

import android.content.Intent
import android.os.Bundle
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.alejandro.repaso_pokemon.R
import com.alejandro.repaso_pokemon.databinding.ActivityMainBinding
import com.alejandro.repaso_pokemon.databinding.ActivityManagerBinding
import com.alejandro.repaso_pokemon.model.Pokemon
import com.alejandro.repaso_pokemon.repository.PokemonsRepository

class ManagerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityManagerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityManagerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        showPokemons()
        binding.btCreate.setOnClickListener {
            val intent = Intent(this, CreateAndModifyActivity::class.java)
            intent.putExtra("IsEdited", false)
            startActivity(intent)
        }

        binding.btExit.setOnClickListener {
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        refreshTable()
    }

    /**
     * Refresh table content: remove previous rows (preserving first row if it's a header)
     * and repopulate from repository.
     */
    private fun refreshTable() {
        val total = binding.tbPokemons.childCount
        if (total > 1) {
            binding.tbPokemons.removeViews(1, total - 1)
        } else if (total == 0) { }
        showPokemons()
    }

    /**
     * Show all pokemons form the repository in the table
     */
    private fun showPokemons() {
        var pokemons = PokemonsRepository.getPokemons()
        pokemons.forEach { createNewRow(it) }
    }

    /**
     * Creates and add a new row with the Pokemon data to the table
     */
    private fun createNewRow(pokemon: Pokemon) {
        val tableRow = TableRow(this).apply {
            layoutParams = TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT
            )
        }

        tableRow.addView(createTableCell(pokemon.id.toString()))
        tableRow.addView(createTableCell(pokemon.name))
        tableRow.addView(createTableCell(pokemon.type.toString()))
        tableRow.addView(createTableCell(pokemon.level.toString()))
        tableRow.addView(createTableCell(pokemon.strength.toString()))
        tableRow.addView(createTableCell(pokemon.defense.toString()))
        tableRow.addView(createTableCell(pokemon.health.toString()))

        tableRow.setOnClickListener {
            val intent = Intent(this, CreateAndModifyActivity::class.java).apply {
                putExtra("IsEdited", true)
                putExtra("PokemonId", pokemon.id)
            }
            startActivity(intent)
        }

        binding.tbPokemons.addView(tableRow)
    }

    /**
     * Create a cell for a table with the specified data
     */
    private fun createTableCell(data: String): TextView {
        val textView = TextView(this)

        textView.layoutParams = TableRow.LayoutParams(
            TableRow.LayoutParams.WRAP_CONTENT,
            TableRow.LayoutParams.WRAP_CONTENT
        )

        textView.text = data
        textView.setPadding(10, 10, 10, 10)
        return textView
    }
}