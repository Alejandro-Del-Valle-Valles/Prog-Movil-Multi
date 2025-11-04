package com.alejandro.repaso_pokemon.controllers

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.alejandro.repaso_pokemon.R
import com.alejandro.repaso_pokemon.databinding.ActivityCreateAndModifyBinding
import com.alejandro.repaso_pokemon.enums.Types
import com.alejandro.repaso_pokemon.model.Pokemon
import com.alejandro.repaso_pokemon.repository.PokemonsRepository

class CreateAndModifyActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateAndModifyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityCreateAndModifyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        addTypeOptions()
        binding.btExitCreation.setOnClickListener { finish() }
        if(intent.getBooleanExtra("IsEdited", false)) {
            binding.btCreatePokemon.text = "Editar"
            autocompleteData()
        }
    }

    /**
     * When the Create/Edit button is clicked, this adds or update the Pokemon from the Repository
     */
    fun onCreateClicked(view: View) {
        val isEdit = intent.getBooleanExtra("IsEdited", false)
        try {
            if(checkDataIsNotEmpty()) {
                val pokemons = PokemonsRepository.getPokemons()
                if (isEdit) {
                    val pokemon = createPokemon()
                    if(pokemons.contains(pokemon))
                        PokemonsRepository.updatePokemon(pokemon)
                    else Toast.makeText(this,
                        "No se ha podido actualizar el pokemon porque no se ha encontrado.",
                        Toast.LENGTH_SHORT).show()
                } else {
                    val pokemon = createPokemon()
                    if(!pokemons.contains(pokemon))
                        PokemonsRepository.addPokemon(pokemon)
                    else Toast.makeText(this,
                        "Un pokemon con este código ya existe.",
                        Toast.LENGTH_SHORT).show()
                }
            }
        } catch (ex: Exception) {
            Toast.makeText(this,
                "Ocurrión un error y no se pudo " +
                        if(isEdit) "editar" else "crear",
                Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Check that the edit texts aren't empty or invalid value
     */
    private fun checkDataIsNotEmpty(): Boolean {
        var isEmpty: Boolean = false
        val level = binding.etLevel.text.toString().toIntOrNull() ?: 0
        val strength = binding.etStrength.text.toString().toIntOrNull() ?: 0
        val defense = binding.etDefense.text.toString().toIntOrNull() ?: 0
        val health = binding.etHealt.text.toString().toIntOrNull() ?: 0

        if(binding.etId.text.isEmpty()) {
            isEmpty = false
            Toast.makeText(this, "El ID está vacío",
                Toast.LENGTH_SHORT).show()
        }
        if(binding.etName.text.isEmpty()) {
            isEmpty = false
            Toast.makeText(this, "El Nombre está vacío",
                Toast.LENGTH_SHORT).show()
        }
        if(level < Pokemon.MIN_LEVEL
            || level > Pokemon.MAX_LEVEL) {
            isEmpty = false
            Toast.makeText(this,
                "El Nivel no está dentro del rango permitido ${Pokemon.MIN_LEVEL}-${Pokemon.MAX_LEVEL}",
                Toast.LENGTH_SHORT).show()
        }
        if(strength < Pokemon.MIN_BASE_STRENGTH
                || strength > Pokemon.MAX_BASE_STRENGTH) {
            isEmpty = false
            Toast.makeText(this,
                "La fuerza no está dentro del rango permitido ${Pokemon.MIN_BASE_STRENGTH}-${Pokemon.MAX_BASE_STRENGTH}",
                Toast.LENGTH_SHORT).show()
        }
        if(defense < Pokemon.MIN_BASE_DEFENSE
            || defense > Pokemon.MAX_BASE_DEFENSE) {
            isEmpty = false
            Toast.makeText(this,
                "La defensa no está dentro del rango permitido ${Pokemon.MIN_BASE_DEFENSE}-${Pokemon.MAX_BASE_DEFENSE}",
                Toast.LENGTH_SHORT).show()
        }
        if(health < Pokemon.MIN_BASE_HEALTH
            || health > Pokemon.MAX_BASE_HEALTH) {
            isEmpty = false
            Toast.makeText(this,
                "La vida no está dentro del rango permitido ${Pokemon.MIN_BASE_HEALTH}-${Pokemon.MAX_BASE_HEALTH}",
                Toast.LENGTH_SHORT).show()
        }
        return isEmpty
    }

    /**
     * Create and return a Pokemon from the data of the edit texts and spinner.
     */
    private fun createPokemon(): Pokemon =
        Pokemon(binding.etId.text.toString().toIntOrNull() ?: 0,
            binding.etName.text.toString().trim(),
            binding.spTypes.selectedItem as Types,
            binding.etLevel.text.toString().toIntOrNull() ?: 0,
            binding.etStrength.text.toString().toIntOrNull() ?: 0,
            binding.etDefense.text.toString().toIntOrNull() ?: 0,
            binding.etHealt.text.toString().toIntOrNull() ?: 0)


    /**
     * When a pokemon is going to be edited, this autocomplete the data
     */
    private fun autocompleteData() {

    }

    /**
     * Add the types of pokemon to the spinner.
     */
    private fun addTypeOptions() {
        binding.spTypes.adapter = ArrayAdapter(this,
            android.R.layout.simple_spinner_item,
            Types.entries.toTypedArray()
        )
    }
}