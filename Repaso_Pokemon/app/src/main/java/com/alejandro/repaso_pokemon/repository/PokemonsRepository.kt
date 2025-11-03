package com.alejandro.repaso_pokemon.repository

import com.alejandro.repaso_pokemon.enums.Types
import com.alejandro.repaso_pokemon.model.Pokemon

object PokemonsRepository {

    private val pokemons = mutableListOf<Pokemon>()

    init {
        pokemons.addAll(listOf(
            Pokemon(1, "Pikachu", Types.ELECTRICO, 2, 5, 5, 15),
            Pokemon(2, "Chikorita", Types.PLANTA, 1, 3, 7, 15),
            Pokemon(3, "Totodile", Types.AGUA, 1, 2, 4, 15),
            Pokemon(4, "Charmander", Types.FUEGO, 2, 5, 5, 15)
        ))
    }

    /**
     * Return the List of pokemons
     */
    fun getPokemons(): List<Pokemon> = pokemons

    /**
     * Add a Pokemon into the list
     */
    fun addPokemon(pokemon: Pokemon): Boolean {
        var isAdded: Boolean = false
        if(!pokemons.contains(pokemon)) {
            pokemons.add(pokemon)
            isAdded = true
        }
        return isAdded
    }

    /**
     * Delete a Pokemon from the List
     */
    fun deletePokemon(pokemon: Pokemon): Boolean {
        var isDeleted = false
        if(pokemons.contains(pokemon)) {
            pokemons.remove(pokemon)
            isDeleted = true
        }
        return isDeleted
    }

    /**
     * Update a pokemon from the list
     */
    fun updatePokemon(pokemon: Pokemon): Boolean {
        var isUpdated: Boolean = false;
        if(pokemons.contains(pokemon)) {
            pokemons[pokemons.indexOf(pokemon)] = pokemon
            isUpdated = true
        }
        return isUpdated
    }
}