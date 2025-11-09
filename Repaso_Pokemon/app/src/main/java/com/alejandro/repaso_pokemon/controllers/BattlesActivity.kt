package com.alejandro.repaso_pokemon.controllers

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.alejandro.repaso_pokemon.databinding.ActivityBattlesBinding
import com.alejandro.repaso_pokemon.model.Pokemon
import com.alejandro.repaso_pokemon.repository.PokemonsRepository
import kotlin.random.Random

class BattlesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBattlesBinding
    private lateinit var pokemonsOne: MutableList<Pokemon>
    private lateinit var pokemonsTwo: MutableList<Pokemon>

    private val rn = Random

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityBattlesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fillSpinners()
        binding.btExitBattles.setOnClickListener { finish() }
        binding.btStartBattle.setOnClickListener {
            if(checkPokemonsSelected()) showBattleLog()
            else Toast.makeText(this,
                "Debes seleccionar Pokemons distintos.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showBattleLog() {
        binding.tvBattleLog.text = ""
        val pokemonOne = binding.spPokemonOne.selectedItem as Pokemon
        val pokemonTwo = binding.spPokemonTwo.selectedItem as Pokemon
        binding.tvBattleLog.text = makeBattle(pokemonOne, pokemonTwo)
    }

    private fun makeBattle(pokemonOne: Pokemon, pokemonTwo: Pokemon) : String {
        val log = StringBuilder()
        var damage = 0
        var healthPokemonOne = pokemonOne.health
        var healthPokemonTwo = pokemonTwo.health
        while (true) {
            if(healthPokemonOne <= 0) {
                log.append("¡${pokemonTwo.name} gana la batalla!\n")
                break
            }

            damage = pokemonOne.strength * rn.nextInt(1, 4) - pokemonTwo.defense
            if(damage <= 0) damage = 1
            healthPokemonTwo = pokemonTwo.health - damage
            if(healthPokemonTwo < 0) healthPokemonTwo = 0
            log.append("${pokemonOne.name} ataca a ${pokemonTwo.name} haciendole $damage puntos de daño y dejándole a $healthPokemonTwo\n")
            if(healthPokemonTwo <= 0) {
                log.append("¡${pokemonOne.name} gana la batalla!\n")
                break
            }

            damage = pokemonTwo.strength * rn.nextInt(1, 4) - pokemonOne.defense
            if(damage <= 0) damage = 1
            healthPokemonOne = pokemonOne.health - damage
            if(healthPokemonOne < 0) healthPokemonOne = 0
            log.append("${pokemonTwo.name} ataca a ${pokemonOne.name} haciendole $damage puntos de daño y dejándole a $healthPokemonOne\n")
        }
        return log.toString()
    }

    private fun checkPokemonsSelected() : Boolean {
        return binding.spPokemonOne.selectedItem as Pokemon != binding.spPokemonTwo.selectedItem as Pokemon
    }

    private fun fillSpinners() {
        pokemonsOne = PokemonsRepository.getPokemons().toMutableList()
        pokemonsTwo = PokemonsRepository.getPokemons().toMutableList()
        binding.spPokemonOne.adapter = ArrayAdapter(this,
            android.R.layout.simple_spinner_item,
            pokemonsOne)

        binding.spPokemonTwo.adapter = ArrayAdapter(this,
            android.R.layout.simple_spinner_item,
            pokemonsTwo)
    }
}