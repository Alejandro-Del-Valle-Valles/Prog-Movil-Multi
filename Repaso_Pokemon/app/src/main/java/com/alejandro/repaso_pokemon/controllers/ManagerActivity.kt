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

    /*TODO: Implementar la característica para permitir
       editar pokemons pulsando sobre ellos*/
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