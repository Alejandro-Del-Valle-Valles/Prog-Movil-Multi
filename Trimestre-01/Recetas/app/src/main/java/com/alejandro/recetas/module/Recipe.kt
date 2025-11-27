package com.alejandro.recetas.module

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class Recipe(
    @SerialName("titulo") var title: String,
    @SerialName("ingredientes") var ingredients: Array<String>,
    @SerialName("descripcion") var description: String
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Recipe

        if (title != other.title) return false
        if (!ingredients.contentEquals(other.ingredients)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = title.hashCode()
        result = 31 * result + ingredients.contentHashCode()
        return result
    }
}