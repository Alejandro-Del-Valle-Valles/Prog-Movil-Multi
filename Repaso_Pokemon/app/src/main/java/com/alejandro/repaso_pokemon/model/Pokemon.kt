package com.alejandro.repaso_pokemon.model

import com.alejandro.repaso_pokemon.enums.Types

class Pokemon(initialId: Int, initialName: String, var type: Types, initialLevel: Int,
              initialBaseStrength: Int, initialBaseDefense: Int, initialBaseHealth: Int)
    : Comparable<Pokemon> {
    companion object {
        const val MIN_LEVEL = 1
        const val MAX_LEVEL = 100
        const val MIN_BASE_STRENGTH = 1
        const val MAX_BASE_STRENGTH = 15
        const val MIN_BASE_DEFENSE = 1
        const val MAX_BASE_DEFENSE = 15
        const val MIN_BASE_HEALTH = 1
        const val MAX_BASE_HEALTH = 15
    }

    val id: Int = if (initialId > 0) initialId else 1

    /** Name with trimming and fallback to "Desconocido" */
    var name: String = initialName
        get() = field
        set(value) {
            field = value.trim().ifEmpty { "Desconocido" }
        }

    /** Level, constrained between MIN_LEVEL and MAX_LEVEL */
    var level: Int = initialLevel
        get() = field
        set(value) {
            field = if (value in MIN_LEVEL..MAX_LEVEL) value else MIN_LEVEL
        }

    /** Base Strength, constrained between min and max */
    var baseStrength: Int = initialBaseStrength
        get() = field
        set(value) {
            field = value.coerceIn(MIN_BASE_STRENGTH, MAX_BASE_STRENGTH)
        }

    /** Base Defense, constrained between min and max */
    var baseDefense: Int = initialBaseDefense
        get() = field
        set(value) {
            field = value.coerceIn(MIN_BASE_DEFENSE, MAX_BASE_DEFENSE)
        }

    /** Base Health, constrained between min and max */
    var baseHealth: Int = initialBaseHealth
        get() = field
        set(value) {
            field = value.coerceIn(MIN_BASE_HEALTH, MAX_BASE_HEALTH)
        }

    //CALCULATED ATTRIBUTES
    val strength: Int
        get() = baseStrength * level

    val defense: Int
        get() = baseDefense * level

    val health: Int
        get() = baseHealth * level

    init {
        name = initialName
        level = initialLevel
        baseStrength = initialBaseStrength
        baseDefense = initialBaseDefense
        baseHealth = initialBaseHealth
    }

    override fun toString(): String {
        return name
    }

    /** Two Pokemons are equal if they have the same reference or the same Id*/
    override fun equals(other: Any?): Boolean {
        if(this === other) return true
        if(other !is Pokemon) return false
        return id == other.id
    }

    /** The hash code is based on the Id*/
    override fun hashCode(): Int {
        return id.hashCode()
    }

    /**
     * Compare by Id, then by name, then by type, then by level and then by base attributes
     */
    override fun compareTo(other: Pokemon): Int {
        var comparation: Int = id.compareTo(other.id)
        if(comparation == 0) comparation = name.compareTo(other.name)
        if(comparation == 0) comparation = type.compareTo(other.type)
        if(comparation == 0) comparation = level.compareTo(other.level)
        if(comparation == 0) comparation = baseStrength.compareTo(other.baseStrength)
        if(comparation == 0) comparation = baseDefense.compareTo(other.baseDefense)
        if(comparation == 0) comparation = baseHealth.compareTo(other.baseHealth)
        return comparation
    }
}